package com.chinadovey.parking.webapps.mina;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.biz.GatewayLogBiz;
import com.chinadovey.parking.webapps.mina.Heart.HeartResProcess;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.pojo.RTURealTime;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.DASPackets;
import com.chinadovey.parking.webapps.mina.protocol.ERRPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.mina.protocol.RTUPackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class DPSProcess {

	private static Log logger = LogFactory.getLog(DPSProcess.class);

	private String localAddress;
	private MongoOperations mongoOps;
	private RTUProcessFactory rtuProcessFactory;
	private GatewayLogBiz gatewayLogBiz;
	private CarLockBiz carLockBiz;

	private CloudNodeIoHandler ioHandler;

	public void messageProcess(IoSession session, PacketsEntity packets) throws SQLException, IOException {

		if (packets.getTag().length < 2) {
			logger.warn("TranPackets 的 TAG 为"
					+ ByteUtilities.asHex(packets.getTag()));
			return;
		}

		switch (packets.getTag()[0]) {
		case 0x00:
			/*
			 * tag:0x00XXH DAS => DPS
			 */
			switch (packets.getTag()[1]) {
			case 0x01:
				// 数据上报
				//p0x0001(session, packets);
				DpsReportProcess rrp = new DpsReportProcess(localAddress, mongoOps, ioHandler,rtuProcessFactory,carLockBiz);
				rrp.p0x0001(session,packets);
				return;
			//case 0x02:
				// 异常上报
				//p0x0003(session, packets);
			//	return;
			case 0x04:
				//网关首次数据上报
				//p0x0004(session, packets);
				DpsFirstReportProcess rfrp = new DpsFirstReportProcess(localAddress, mongoOps, ioHandler,gatewayLogBiz);
				rfrp.p0x0004(session,packets);
				return;
			case 0x03:
				// 控制回应
				p0x0003(session, packets);
				return;
			case 0x05:
				HeartResProcess hrp = new HeartResProcess(localAddress, mongoOps);
				hrp.p0x0005(session,packets);
				return;
			case 0x06:
				DpsReportMacProcess drmp = new DpsReportMacProcess( mongoOps);
				drmp.messageHandle(session,packets);
				return;
			}
		//case 0x01:
			/*
			 * tag:0x01XXH DPS => DAS 如果需要在DPS直接转发的消息，在这里处理
			 */
		//	return;
		case 0x02:
			/*
			 * tag:0x02XXH APP => DPS 控制命令
			 */
			switch (packets.getTag()[1]) {
			case 0x01://车位锁开关控制
				p0x02XX(session, packets);
				break;
			case 0x03://配置车位锁
				ConfDpsProcess conf = new ConfDpsProcess(localAddress, mongoOps, ioHandler);
				conf.p0x0203(session,packets);
				break;
			case 0x04://删除车位锁配置
				ConfDpsProcess delete = new ConfDpsProcess(localAddress, mongoOps, ioHandler);
				delete.p0x0204(session,packets);
				break;
			case 0x05://网关配置
				ConfDasPortProcess cdpp = new ConfDasPortProcess(localAddress, mongoOps, ioHandler);
				cdpp.p0x0205(session,packets);
				break;
			case 0x06://网关重启
				RebootPortProcess rpp = new RebootPortProcess(localAddress, mongoOps, ioHandler);
				rpp.p0x0206(session,packets);
				break;
			default:
					break;
			}
			break;
		//case 0x03:
			/*
			 * tag:0x03XXH DPS => APP 基本用不上
			 */
		//	return;
		default:
			logger.error("未知的Packets TAG" + ByteUtils.asHex(packets.getTag()));
			break;
		}
	}

	/**
	 * tag:0x02XXH DAS的下行命令包
	 */
	private void p0x02XX(IoSession session, PacketsEntity packets) {
		
		CMDPackets cmdParkets = (CMDPackets) packets.getValuePackets();
		Query query = Query.query(
				Criteria.where("id").is(ByteUtils.asHex(cmdParkets.getDasId()))
				.and("slaveAddress").is(localAddress));
		DASRealTime dasRealTime = mongoOps.findOne(query, DASRealTime.class);
		if (dasRealTime != null) {
			int cacheId = ioHandler.cacheSession(session);
			byte[] b = new byte[4];
			ByteUtils.writeInt4(cacheId, b, 0);
			if (logger.isDebugEnabled()) {
				logger.debug("成功缓存发送者会话 AppId="+ByteUtils.asHex(b));
			}
			
			cmdParkets.setAppId(b);
			cmdParkets.setDasId(dasRealTime.getDasIdBytes());
			
			// 发送命令
			IoSession targetSession = ioHandler.getSession(dasRealTime.getSessionId());
			if (targetSession != null) {
				packets.getTag()[0] = 0x01;
				packets.finishFilling();
				targetSession.write(packets);
			}
			return;
		}
		
		// TODO 长连接没有找到，将来需要尝试转发到其他Node节点，现在先返回连接失败信息
		if (logger.isInfoEnabled()) {
			logger.info("没有找到RTU[" + ByteUtils.asHex(cmdParkets.getDasId()) + "]的连接信息，估计连接已断开!");
		}
		// TODO 发送错误包
		PacketsEntity entity = new PacketsEntity();
		entity.setTag(new byte[] { 0x03, 0x02 });
		entity.setIndex(new byte[] { 0x00, 0x00 });

		ERRPackets err = new ERRPackets();
		err.setTag(new byte[] { 0x7F, 0x7F });
		err.setDasId(cmdParkets.getDasId());
		err.setTime(new byte[] { 0x00, 0x00, 0x00, 0x00 });
		err.setCode(new byte[] { 0x7F, 0x7F });

		entity.setValuePackets(err);
		entity.finishFilling();

		session.write(entity);
		session.close(false);
	}

	/**
	 * tag:0x0003H DAS的下行命令反馈包
	 */
	private void p0x0003(IoSession session, PacketsEntity packets) {
		if(packets.getValuePackets() instanceof RESPackets){
			RESPackets resPackets = (RESPackets) packets.getValuePackets();
			int appId = ByteUtils.makeIntFromByte(resPackets.getAppId());
			IoSession targetSession = ioHandler.getCachedSession(appId,false);
			if (logger.isDebugEnabled()) {//AppId=131655 00000247
				logger.debug("尝试获取命令发送者会话 AppId="+ByteUtils.asHex(resPackets.getAppId()));
			}
			if (targetSession != null) {
				logger.debug("向命令发送者转发命令反馈");
				packets.getTag()[0] = 0x03;
				targetSession.write(packets);
				logger.debug("等待客户端关闭会话...");
//				targetSession.getCloseFuture().awaitUninterruptibly(5000);
//				logger.info("断开命令发送者会话 SESSION=" + targetSession.getId());
			} else {
				logger.debug("命令发送者会话未找到，估计已断开");
			}
		}
		
	}
	
	
	
	/**
	 * tag:0x0003H DAS的下行命令反馈包
	 */
	private void p0x0005(IoSession session, PacketsEntity packets) {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x0003H,DAS的下行命令反馈包");
		}
		if(packets.getValuePackets() instanceof RESPackets){
			RESPackets resPackets = (RESPackets) packets.getValuePackets();
			int appId = ByteUtils.makeIntFromByte(resPackets.getAppId());
			IoSession targetSession = ioHandler.getCachedSession(appId,false);
			if (logger.isDebugEnabled()) {//AppId=131655 00000247
				logger.debug("尝试获取命令发送者会话 AppId="+appId+" "+ByteUtils.asHex(resPackets.getAppId()));
			}
			if (targetSession != null) {
				logger.debug("向命令发送者转发命令反馈");
				packets.getTag()[0] = 0x03;
				targetSession.write(packets);
				logger.debug("等待客户端关闭会话...");
//				targetSession.getCloseFuture().awaitUninterruptibly(5000);
//				logger.info("断开命令发送者会话 SESSION=" + targetSession.getId());
			} else {
				logger.debug("命令发送者会话未找到，估计已断开");
			}
		}
		
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public void setMongoOps(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	public void setRtuProcessFactory(RTUProcessFactory rtuProcessFactory) {
		this.rtuProcessFactory = rtuProcessFactory;
	}

	public void setIoHandler(CloudNodeIoHandler ioHandler) {
		this.ioHandler = ioHandler;
	}

	public GatewayLogBiz getGatewayLogBiz() {
		return gatewayLogBiz;
	}

	public void setGatewayLogBiz(GatewayLogBiz gatewayLogBiz) {
		this.gatewayLogBiz = gatewayLogBiz;
	}

	public CarLockBiz getCarLockBiz() {
		return carLockBiz;
	}

	public void setCarLockBiz(CarLockBiz carLockBiz) {
		this.carLockBiz = carLockBiz;
	}
	
	
}
