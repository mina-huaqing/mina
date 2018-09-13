package com.chinadovey.parking.webapps.mina;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0710;
import com.chinadovey.parking.webapps.util.ConfUtils;
import com.chinadovey.parking.webapps.util.HttpClientUtil;


public class PushInfoThread extends Thread{
	
    Logger logger = Logger.getLogger(getClass());
    
    private Equipment0710 equipment0710;
    private CarLockBiz carLockBiz;
    
    public PushInfoThread(Equipment0710 equipment0710,CarLockBiz carLockBiz) {
		this.equipment0710 = equipment0710;
		this.carLockBiz = carLockBiz;
	}
    public void run(){
		try {
			if (equipment0710 != null) {
				String slaveId = equipment0710.getRtuId()+equipment0710.getEquiId();
				Float voltage = equipment0710.getVoltage();
				Integer carState = equipment0710.getCarStatus();
				Integer openState = equipment0710.getOpenState();
				Integer equiState = (int)equipment0710.getEquiState();
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("slaveId", slaveId);
				params.put("voltage", voltage.toString());
				params.put("carState", carState.toString());
				params.put("openState", openState.toString());
				params.put("equiState", equiState.toString());
				params.put("source", equipment0710.getSource().toString());
	        	String url = ConfUtils.getUrlByName("huaching.open.platform")+"/api/carLockManageApi/getPushInfo";
				String res = HttpClientUtil.getInstance().httpPost(url, params);
				System.out.println(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
   
   
	

}