package com.chinadovey.parking.webapps.mina;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0713;
import com.chinadovey.parking.webapps.util.ConfUtils;
import com.chinadovey.parking.webapps.util.HttpClientUtil;


public class PushActiveInfo{
	
    Logger logger = Logger.getLogger(getClass());
    
    private Equipment0713 equipment0713;
    private CarLockBiz carLockBiz;
    
    public PushActiveInfo(Equipment0713 equipment0713,CarLockBiz carLockBiz) {
		this.equipment0713 = equipment0713;
		this.carLockBiz = carLockBiz;
	}
    public void push(){
		try {
			if (equipment0713 != null) {
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				String slaveId = equipment0713.getRtuId()+equipment0713.getEquiId();
				
				logger.error("锁编号："+slaveId+","+df.format(new Date())+":开始主动推送");
				
				Float voltage = equipment0713.getVoltage()==null?0:equipment0713.getVoltage();
				Integer carState = equipment0713.getCarStatus()==null?0:equipment0713.getCarStatus();
				Integer openState = equipment0713.getOpenState()==null?0:equipment0713.getOpenState();
				Byte b = equipment0713.getEquiState()==null?(byte)0:equipment0713.getEquiState();
				Integer equiState = (int)b;
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("slaveId", slaveId);
				params.put("voltage", voltage.toString());
				params.put("carState", carState.toString());
				params.put("openState", openState.toString());
				params.put("equiState", equiState.toString());
				int so =equipment0713.getSource()==null?10:10;
				params.put("source", so+"");
	        	String url = ConfUtils.getUrlByName("huaching.open.platform")+"/api/carLockManageApi/pushActiveInfo";
				String res = HttpClientUtil.getInstance().httpPost(url, params);
				/*System.out.println(res);
				logger.error("主动推送返回："+res);*/
			}
		} catch (Exception e) {
			logger.error("主动上报推送："+e);
		}
	}
   
   
	

}