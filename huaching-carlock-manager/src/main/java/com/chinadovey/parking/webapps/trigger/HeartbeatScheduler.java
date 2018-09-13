package com.chinadovey.parking.webapps.trigger;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.chinadovey.parking.core.supports.uitls.DateUtils;
import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.pojo.Carlock;
import com.chinadovey.parking.webapps.util.ConfUtils;
import com.chinadovey.parking.webapps.util.HttpClientUtil;
import com.chinadovey.parking.webapps.util.HttpsHeaderUtil;

import net.sf.json.JSONObject;

/**
 * 宁波 优泊  定时推送车位锁相关的信息
 * @author feng
 */
@Service
public class HeartbeatScheduler {
	
	final Logger logger = Logger.getLogger(HeartbeatScheduler.class);
	@Resource
	private   CarLockBiz carLockBiz;
	
	public void autopush(){
		try{
			String companyNingbo = ConfUtils.getUrlByName("companyNingbo");
			List<Carlock> carLocks = carLockBiz.getByCompanyNo(companyNingbo);
			for (Carlock carLock : carLocks) {
				Map<String,String> headMap = HttpsHeaderUtil.getHeader();
				JSONObject json = new JSONObject();
				json.put("slave_id", carLock.getSlaveId());
				json.put("bind_no", carLock.getBindNo());
				Integer openState = 0;
				if (carLock.getSwitchStatus() == 1) {
					openState = 0;//平躺
				}else if (carLock.getSwitchStatus() == 2) {
					openState = 1;//竖起
				}
				json.put("open_state", openState);
				json.put("car_state", carLock.getCarStatus());
				json.put("voltage", carLock.getVoltage());
				json.put("equi_state", carLock.getRunStatus());
				json.put("heartbeat_time", DateUtils.dateConvertString(new Date(), 3));
				String apiBaseUrl = ConfUtils.getUrlByName("API_BASE_URL");
				String url = apiBaseUrl+"/SensorStatusChanged";
				String res = HttpClientUtil.getInstance().httpPostString(url, json.toString(), headMap);
				JSONObject res2 = JSONObject.fromObject(res);
				System.err.println(res2.getInt("StatusCode"));
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("宁波优泊主动推送车位锁信息失败",e);
		}
	}
}
