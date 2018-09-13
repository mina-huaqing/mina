package com.chinadovey.parking.webapps.mina.Heart;

import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.chinadovey.parking.core.supports.uitls.DateUtils;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.protocol.HeartPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;

public class HeartResProcess {
	
	private static Log logger = LogFactory.getLog(HeartResProcess.class);
	
	private String localAddress;
	private MongoOperations mongoOps;
	
	public HeartResProcess(String localAddress , MongoOperations mongoOps) {
		this.localAddress = localAddress;
		this.mongoOps = mongoOps;
	}
	
	/**
	 * tag:0x0005H DAS回应心跳包
	 * @throws SQLException 
	 */
	public void p0x0005(IoSession session, PacketsEntity packets) throws SQLException {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x0005H,DAS回应心跳包");
		}
		// 解析DAS数据包
		HeartPackets heartPackets = (HeartPackets) packets.getValuePackets();
		try {
			// 刷新 mongoDB 中 DAS socket 的长连接信息
			if (logger.isDebugEnabled()) {
				logger.debug(
					"刷新 mongoDB 中心跳包的信息：" + localAddress + "@" + session.getId());
			}
			Query find = Query.query(Criteria.where("sessionId").is(session.getId()));
			DASRealTime dasRealTime = mongoOps.findOne(find, DASRealTime.class);
			
			HeartRes heartRes = new HeartRes();
			if (dasRealTime != null) {
				heartRes.setDasId(dasRealTime.getId());
			}
			heartRes.setSessionId(session.getId());
			heartRes.setSlaveAddress(localAddress);
			heartRes.setLastTime(DateUtils.dateConvertString(new Date(), 3));
			
			Query query = Query.query(
					Criteria.where("id").is(session.getId()));
			Update update = Update
					.update("slaveAddress", heartRes.getSlaveAddress())
					.set("dasId", heartRes.getDasId())
					.set("lastTime", heartRes.getLastTime());

			mongoOps.upsert(query, update, HeartRes.class);
			
			
			

		} catch (Exception e) {
			logger.error(e.getMessage() + "\n DasPackets:\n" + heartPackets, e);
			e.printStackTrace();
		}
	}
}
