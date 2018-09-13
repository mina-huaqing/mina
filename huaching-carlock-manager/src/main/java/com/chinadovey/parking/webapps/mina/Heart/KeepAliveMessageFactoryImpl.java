package com.chinadovey.parking.webapps.mina.Heart;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.chinadovey.parking.core.supports.uitls.DateUtils;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.protocol.HeartPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;

public class KeepAliveMessageFactoryImpl implements  KeepAliveMessageFactory { 
	
	private static final Logger logger = Logger.getLogger(KeepAliveMessageFactoryImpl.class);
	
	private MongoOperations mongoOps;
	
	public KeepAliveMessageFactoryImpl(MongoOperations mongoOps){
		this.mongoOps = mongoOps;
	}
	
	private final static byte[] tag = {0x01,0x02};
	private final static byte[] index = {0x00,0x00};
	
	
	private final static String minVersion = "00070303";
	
	@Override  
	public boolean isRequest(IoSession session, Object message) {
		try {
			if (message instanceof PacketsEntity) {
				PacketsEntity entity = (PacketsEntity) message;
				if (entity != null && entity.getValuePackets() instanceof HeartPackets) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;  
	}  

	@Override  
	public boolean isResponse(IoSession session, Object message) { 
		try {
			if (message instanceof PacketsEntity) {
				PacketsEntity entity = (PacketsEntity) message;
				if (entity != null && entity.getValuePackets() instanceof HeartPackets) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;  
	}
	
	@Override  
	public Object getRequest(IoSession session) {
		try {
			Long sessionId = session.getId();
			Query query = Query.query(Criteria.where("sessionId").is(sessionId));
			DASRealTime dasRealTime = mongoOps.findOne(query, DASRealTime.class);
			if(dasRealTime != null){
				String dasVersion = dasRealTime.getDasVersion();
				if (dasVersion != null && dasVersion.length() == 10) {
					String realVersion =  dasVersion.substring(2);
					if (realVersion.compareTo(minVersion) >= 0) {
						PacketsEntity entity = new PacketsEntity();
						entity.setTag(tag);
						entity.setIndex(index);
						HeartPackets heartPackets = new HeartPackets();
						entity.setValuePackets(heartPackets);
						entity.finishFilling();
						return entity;//版本在00070303之后 发送心跳包
					}
				}
			}else {
				logger.error(DateUtils.dateConvertString(new Date(), 3)+"清理无效的session:"+sessionId);
				session.close(true);// release invalid session
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}  
	
	@Override  
	public Object getResponse(IoSession session, Object request) {
		return null; 
	} 
	

}
