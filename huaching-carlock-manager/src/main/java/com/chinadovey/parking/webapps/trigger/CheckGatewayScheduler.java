package com.chinadovey.parking.webapps.trigger;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.chinadovey.parking.webapps.biz.GatewayBiz;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.pojo.Gateway;

@Service
public class CheckGatewayScheduler {
	final Logger logger = Logger.getLogger(CheckGatewayScheduler.class);
	 
	 @Resource
	 private  GatewayBiz gatewayBiz;
	 
	 public void  check(){
	     try{	
			List<Gateway> list = gatewayBiz.getAll();
			/*for(Gateway gate : list){
				Query query = Query.query(Criteria.where("_id").is(gate.getDasId().toLowerCase()));
				DASRealTime dasRealTime = mongoOps.findOne(query, DASRealTime.class);
				if(dasRealTime==null){
					gate.setGatewayStatus(0);
					gatewayBiz.update(gate);
				}else{
					gate.setGatewayStatus(1);
					gatewayBiz.update(gate);
				}
			}*/
		}catch(Exception e){
			e.printStackTrace();
			logger.error("更新车锁状态失败",e);
		}
	}
}
