package com.chinadovey.parking.webapps.mina;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.ERRPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class ConfDpsProcess {
	
	private static Log logger = LogFactory.getLog(DPSProcess.class);
	
	private String localAddress;
	private MongoOperations mongoOps;
	private CloudNodeIoHandler ioHandler;
	
	public ConfDpsProcess(String localAddress , MongoOperations mongoOps , CloudNodeIoHandler ioHandler) {
		this.localAddress = localAddress;
		this.mongoOps = mongoOps;
		this.ioHandler = ioHandler;
	}
	
	public void p0x0203(IoSession session, PacketsEntity packets) {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x02XXH,DAS的下行命令包");
		}
		// 解析CMD数据包
		CMDPackets cmdParkets = (CMDPackets) packets.getValuePackets();
		if (logger.isDebugEnabled()) {
			logger.debug("成功解析CMD数据包：\n" + cmdParkets);
		}

		// 寻找DAS长连接
		Query query = Query.query(
				Criteria.where("id").is(ByteUtils.asHex(cmdParkets.getDasId()))
				.and("slaveAddress").is(localAddress));
		DASRealTime dasRealTime = mongoOps.findOne(query, DASRealTime.class);
		if (logger.isDebugEnabled()) {
			logger.debug("成功获取到 DASRealTime：" + dasRealTime);
		}
		
		if (dasRealTime != null) {
			int cacheId = ioHandler.cacheSession(session);
			byte[] b = new byte[4];
			ByteUtils.writeInt4(cacheId, b, 0);
			if (logger.isDebugEnabled()) {
				logger.debug("成功缓存发送者会话 AppId="+cacheId+" "+ByteUtils.asHex(b));
			}
			
			cmdParkets.setAppId(b);
			cmdParkets.setDasId(dasRealTime.getDasIdBytes());
			
			// 发送命令
			IoSession targetSession = ioHandler.getSession(dasRealTime.getSessionId());
			if (logger.isDebugEnabled()) {
				logger.debug("成功获取到目标IoSession：targetSession = " + targetSession);
			}
			
			if (targetSession != null) {
				packets.setTag(new byte[]{0x01,0x01}); 
				packets.finishFilling();
					targetSession.write(packets);
					if (logger.isInfoEnabled()) {
						logger.info("发送下行包：\n" + packets + "\n==命令包==\n" + cmdParkets);
					}
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
	
	
	
	public void p0x0204(IoSession session, PacketsEntity packets) {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x02XXH,DAS的下行命令包");
		}
		// 解析CMD数据包
		CMDPackets cmdParkets = (CMDPackets) packets.getValuePackets();
		if (logger.isDebugEnabled()) {
			logger.debug("成功解析CMD数据包：\n" + cmdParkets);
		}

		// 寻找DAS长连接
		Query query = Query.query(
				Criteria.where("id").is(ByteUtils.asHex(cmdParkets.getDasId()))
				.and("slaveAddress").is(localAddress));
		DASRealTime dasRealTime = mongoOps.findOne(query, DASRealTime.class);
		if (logger.isDebugEnabled()) {
			logger.debug("成功获取到 RTURealTime：" + dasRealTime);
		}
		
		if (dasRealTime != null) {
			
			int cacheId = ioHandler.cacheSession(session);
			byte[] b = new byte[4];
			ByteUtils.writeInt4(cacheId, b, 0);
			if (logger.isDebugEnabled()) {
				logger.debug("成功缓存发送者会话 AppId="+cacheId+" "+ByteUtils.asHex(b));
			}
			
			cmdParkets.setAppId(b);
			cmdParkets.setDasId(dasRealTime.getDasIdBytes());
			
			// 发送命令
			IoSession targetSession = ioHandler.getSession(dasRealTime.getSessionId());
			if (logger.isDebugEnabled()) {
				logger.debug("成功获取到目标IoSession：targetSession = " + targetSession);
			}
			
			if (targetSession != null) {
				packets.setTag(new byte[]{0x01,0x01}); 
				packets.finishFilling();
					targetSession.write(packets);
					if (logger.isInfoEnabled()) {
						logger.info("发送下行包：\n" + packets + "\n==命令包==\n" + cmdParkets);
					}
			}
			return;
		}
		
		// TODO 长连接没有找到，将来需要尝试转发到其他Node节点，现在先返回连接失败信息
		if (logger.isInfoEnabled()) {
			logger.info("没有找到RTU[" + ByteUtils.asHex(cmdParkets.getRtuId()) + "]的连接信息，估计连接已断开!");
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
	
	
	/*public void p0x0203(IoSession session, PacketsEntity packets) {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x02XXH,DAS的下行命令包");
		}
		// 解析CMD数据包
		CMDPackets cmdParkets = (CMDPackets) packets.getValuePackets();
		if (logger.isDebugEnabled()) {
			logger.debug("成功解析CMD数据包：\n" + cmdParkets);
		}

		// 寻找DAS长连接
		Query query = Query.query(
				Criteria.where("id").is(ByteUtils.asHex(cmdParkets.getRtuId()))
				.and("slaveAddress").is(localAddress));
		RTURealTime rtuRealTime = mongoOps.findOne(query, RTURealTime.class);
		if (logger.isDebugEnabled()) {
			logger.debug("成功获取到 RTURealTime：" + rtuRealTime);
		}
		
		if (rtuRealTime != null) {
			
			int cacheId = ioHandler.cacheSession(session);
			byte[] b = new byte[4];
			ByteUtils.writeInt4(cacheId, b, 0);
			if (logger.isDebugEnabled()) {
				logger.debug("成功缓存发送者会话 AppId="+cacheId+" "+ByteUtils.asHex(b));
			}
			
			cmdParkets.setAppId(b);
			cmdParkets.setDasId(rtuRealTime.getDasIdBytes());
			
			// 发送命令
			IoSession targetSession = ioHandler.getSession(rtuRealTime.getSessionId());
			if (logger.isDebugEnabled()) {
				logger.debug("成功获取到目标IoSession：targetSession = " + targetSession);
			}
			
			if (targetSession != null) {
				packets.setTag(new byte[]{0x01,0x01}); 
				packets.finishFilling();
					targetSession.write(packets);
					if (logger.isInfoEnabled()) {
						logger.info("发送下行包：\n" + packets + "\n==命令包==\n" + cmdParkets);
					}
			}
			return;
		}
		
		// TODO 长连接没有找到，将来需要尝试转发到其他Node节点，现在先返回连接失败信息
		if (logger.isInfoEnabled()) {
			logger.info("没有找到RTU[" + ByteUtils.asHex(cmdParkets.getRtuId()) + "]的连接信息，估计连接已断开!");
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
	
	
	
	public void p0x0204(IoSession session, PacketsEntity packets) {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x02XXH,DAS的下行命令包");
		}
		// 解析CMD数据包
		CMDPackets cmdParkets = (CMDPackets) packets.getValuePackets();
		if (logger.isDebugEnabled()) {
			logger.debug("成功解析CMD数据包：\n" + cmdParkets);
		}

		// 寻找DAS长连接
		Query query = Query.query(
				Criteria.where("id").is(ByteUtils.asHex(cmdParkets.getRtuId()))
				.and("slaveAddress").is(localAddress));
		RTURealTime rtuRealTime = mongoOps.findOne(query, RTURealTime.class);
		if (logger.isDebugEnabled()) {
			logger.debug("成功获取到 RTURealTime：" + rtuRealTime);
		}
		
		if (rtuRealTime != null) {
			
			int cacheId = ioHandler.cacheSession(session);
			byte[] b = new byte[4];
			ByteUtils.writeInt4(cacheId, b, 0);
			if (logger.isDebugEnabled()) {
				logger.debug("成功缓存发送者会话 AppId="+cacheId+" "+ByteUtils.asHex(b));
			}
			
			cmdParkets.setAppId(b);
			cmdParkets.setDasId(rtuRealTime.getDasIdBytes());
			
			// 发送命令
			IoSession targetSession = ioHandler.getSession(rtuRealTime.getSessionId());
			if (logger.isDebugEnabled()) {
				logger.debug("成功获取到目标IoSession：targetSession = " + targetSession);
			}
			
			if (targetSession != null) {
				packets.setTag(new byte[]{0x01,0x01}); 
				packets.finishFilling();
					targetSession.write(packets);
					if (logger.isInfoEnabled()) {
						logger.info("发送下行包：\n" + packets + "\n==命令包==\n" + cmdParkets);
					}
			}
			return;
		}
		
		// TODO 长连接没有找到，将来需要尝试转发到其他Node节点，现在先返回连接失败信息
		if (logger.isInfoEnabled()) {
			logger.info("没有找到RTU[" + ByteUtils.asHex(cmdParkets.getRtuId()) + "]的连接信息，估计连接已断开!");
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
	}*/
}
