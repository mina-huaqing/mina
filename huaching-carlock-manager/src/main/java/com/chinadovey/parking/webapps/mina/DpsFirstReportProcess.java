package com.chinadovey.parking.webapps.mina;

import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.chinadovey.parking.webapps.biz.GatewayLogBiz;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.protocol.DASReportPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.pojo.GatewayLog;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class DpsFirstReportProcess {
	
	private static Log logger = LogFactory.getLog(DPSProcess.class);
	
	private String localAddress;
	private MongoOperations mongoOps;
	private CloudNodeIoHandler ioHandler;
	private GatewayLogBiz gatewayLogBiz;
	
	public DpsFirstReportProcess(String localAddress , MongoOperations mongoOps , CloudNodeIoHandler ioHandler,GatewayLogBiz gatewayLogBiz) {
		this.localAddress = localAddress;
		this.mongoOps = mongoOps;
		this.ioHandler = ioHandler;
		this.gatewayLogBiz = gatewayLogBiz;
	}
	
	/**
	 * tag:0x0004H DAS上报数据包
	 * @throws SQLException 
	 */
	public void p0x0004(IoSession session, PacketsEntity packets) throws SQLException {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x0001H,DAS首次上报数据包");
		}
		// 解析DAS数据包
		DASReportPackets dasPackets = (DASReportPackets) packets.getValuePackets();
		

		try {
			// 刷新 mongoDB 中 DAS socket 的长连接信息
			if (logger.isDebugEnabled()) {
				logger.debug(
					"刷新 mongoDB 中 DAS socket 的长连接信息：" + localAddress + "@" + session.getId());
			}
			DASRealTime dasRelaTime = new DASRealTime();
			dasRelaTime.setId(ByteUtils.asHex(dasPackets.getDasId()));
			dasRelaTime.setDasIdBytes(dasPackets.getDasId());
			dasRelaTime.setSessionId(session.getId());
			dasRelaTime.setSlaveAddress(localAddress);
			dasRelaTime.setDasVersion(ByteUtils.asHex(dasPackets.getValue()));//网关版本号  7.3.3之后新增
			
			String dasId =  ByteUtils.asHex(dasPackets.getDasId());
			GatewayLog gatewayLog = new GatewayLog();
			gatewayLog.setDasId(dasId);
			gatewayLog.setStatus(2);
			gatewayLog.setTime(new Date());
			gatewayLogBiz.save(gatewayLog);
			
			Query query = Query.query(
					Criteria.where("id").is(dasRelaTime.getId()));
			Update update = Update
					.update("slaveAddress", dasRelaTime.getSlaveAddress())
					.set("sessionId", dasRelaTime.getSessionId())
					.set("dasIdBytes", dasRelaTime.getDasIdBytes())
					.set("dasVersion", dasRelaTime.getDasVersion());

			mongoOps.upsert(query, update, DASRealTime.class);
			
			
			

		} catch (Exception e) {
			logger.error(e.getMessage() + "\n DasPackets:\n" + dasPackets, e);
			e.printStackTrace();
		}
	}
}
