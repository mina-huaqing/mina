package com.chinadovey.parking.webapps.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinadovey.parking.core.mvc.AbstractBaseController;
import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.biz.ControlBiz;
import com.chinadovey.parking.webapps.biz.GatewayBiz;
import com.chinadovey.parking.webapps.mina.client.CtrlAllThread;
import com.chinadovey.parking.webapps.mina.pojo.Slave;
import com.chinadovey.parking.webapps.pojo.Carlock;
import com.chinadovey.parking.webapps.pojo.Gateway;
import com.chinadovey.parking.webapps.util.ConfUtils;

import net.sf.json.JSONObject;
/**
 * 车位锁 控制，配置等 接口
 * @author feng
 *
 */
@RequestMapping("/control")
@Controller
public class ControlController extends AbstractBaseController{
	
	@Autowired
	private CarLockBiz carLockBiz;
	@Autowired
	private GatewayBiz gatewayBiz;
	@Autowired
	private ControlBiz controlBiz;
	
	
	/**
	 * 车位锁控制
	 * @param slaveId
	 * @param action  1：下降   2：上升 
	 * @return
	 */
	@RequestMapping("/operate")
	@ResponseBody
	public JSONObject operate(String slaveId, Integer action){
		JSONObject json = new JSONObject();
		try {
			Integer stat = controlBiz.operate(slaveId, action);
			if (stat == 14 ) {
				stat = controlBiz.operate(slaveId, action);
				if(stat==14){
					json.put("result", false);
					json.put("msg", "车位锁操作失败");
				}else if(stat==-1){
					json.put("result", false);
					json.put("msg", "车位锁操作失败");
				}else if(stat==0){
					json.put("result", true);
					json.put("msg", "车位锁下降成功");
				}else if(stat==1){
					json.put("result", true);
					json.put("msg", "车位锁升起成功");
				}
			}else if(stat==-1){
				json.put("result", false);
				json.put("msg", "车位锁操作失败");
			}else if(stat==0){
				json.put("result", true);
				json.put("msg", "车位锁下降成功");
			}else if(stat==1){
				json.put("result", true);
				json.put("msg", "车位锁升起成功");
			}
			json.put("stat", stat);
			return json;
		} catch (Exception e) {
			json.put("stat", -1);
			json.put("result", false);
			return json;
		}
	}
	/**
	 * 车位锁自动 手动切换
	 * @param slaveId
	 * @param action 3自动 4手动 
	 * @return
	 */
	@RequestMapping("/autoOrHand")
	@ResponseBody
	public JSONObject autoOrHand(String slaveId, Integer action){
		JSONObject json = new JSONObject();
		try {
			Integer stat = controlBiz.autoOrHand(slaveId, action);
			if(stat == -1){
				json.put("result", false);
			}else{
				json.put("result", true);
			}
			json.put("stat", stat);
			return json;
		} catch (Exception e) {
			json.put("stat", -1);
			json.put("result", false);
			return json;
		}
	}
	@RequestMapping("/config")
	@ResponseBody
	public JSONObject config(String dasId,String slaveId,String serial){
		JSONObject json = new JSONObject();
		try {
			Integer status = controlBiz.carlockConfig(dasId, slaveId, serial);
			json.put("result", true);
			json.put("status", status);
			return json;
		} catch (Exception e) {
			json.put("result", false);
			return json;
		}
	}
	
	@RequestMapping("/allUp")
	@ResponseBody
	public JSONObject allup(String companyNo){
		JSONObject json = new JSONObject();
		try {
			List<String> dasIds = new ArrayList<String>();
			List<Gateway> list = gatewayBiz.getGateway(companyNo);
			for (Gateway gateway : list) {
				dasIds.add(gateway.getDasId());
			}
			String ip = ConfUtils.getControlAddress();
			
			List<Slave> slaves =  Collections.synchronizedList(new ArrayList<Slave>());
            CountDownLatch latch=new CountDownLatch(dasIds.size());
            for(String dasId : dasIds){
        	   CtrlAllThread t = new CtrlAllThread(dasId,2,ip,slaves,latch);
        	   t.start();
            }
            latch.await();
            Integer total =0;
            total = carLockBiz.getAll(dasIds);
            Integer success = 0;
            for (Slave slave : slaves) {
				if (slave.getRes() == 0) {
					success++;
					logger.info(slave.getSlaveId());
					Carlock carLock2 = carLockBiz.getBySlaveid(slave.getSlaveId());
					carLock2.setIsAuto(4);//手动打开
					carLock2.setSwitchStatus(2);
					carLockBiz.update(carLock2);
				}
			}
            Integer fail = total - success;
            String msg = "成功了"+success+"个"+"失败了"+fail+"个";
			json.put("result", true);
			json.put("msg", msg);
			return json;
		} catch (Exception e) {
			json.put("result", false);
			return json;
		}
	}
	@RequestMapping("/allDown")
	@ResponseBody
	public JSONObject alldown(String companyNo){
		JSONObject json = new JSONObject();
		try {
			List<String> dasIds = new ArrayList<String>();
			List<Gateway> list = gatewayBiz.getGateway(companyNo);
			for (Gateway gateway : list) {
				dasIds.add(gateway.getDasId());
			}
			String ip = ConfUtils.getControlAddress();
			List<Slave> slaves =  Collections.synchronizedList(new ArrayList<Slave>());
            CountDownLatch latch=new CountDownLatch(dasIds.size());
            for(String dasId : dasIds){
        	   CtrlAllThread t = new CtrlAllThread(dasId,1,ip,slaves,latch);
        	   t.start();
            }
            latch.await();
            Integer total =0;
            total = carLockBiz.getAll(dasIds);
            Integer success = 0;
            for (Slave slave : slaves) {
				if (slave.getRes() == 0) {
					success++;
					logger.info(slave.getSlaveId());
					Carlock carLock2 = carLockBiz.getBySlaveid(slave.getSlaveId());
					carLock2.setIsAuto(3);
					carLock2.setSwitchStatus(1);
					carLockBiz.update(carLock2);
				}
			}
	        Integer fail = total - success;   
			String msg = "成功了"+success+"个"+"失败了"+fail+"个";
			json.put("result", true);
			json.put("msg", msg);
			return json;
		} catch (Exception e) {
			json.put("result", false);
			return json;
		}
	}
	/**
	 * 车位锁车位状态设置
	 * @param slaveId
	 * @param action  0：无车   1：有车
	 * @return
	 */
	@RequestMapping("/carStateReset")
	@ResponseBody
	public JSONObject carStateReset(String dasId, String slaveId, Integer action){
		JSONObject json = new JSONObject();
		try {
			Integer stat = controlBiz.updateCarState(dasId, slaveId, action);
			if(stat == 0){
				json.put("result", true);
				json.put("msg", "网关返回重置成功");
			}else if(stat == 1){
				json.put("result", false);
				json.put("msg", "网关返回重置失败");
			}else if(stat== -1){
				json.put("result", false);
				json.put("msg", "业务异常");
			}else {
				json.put("result", false);
				json.put("msg", "未定义的返回值");
			}
			json.put("stat", stat);
			return json;
		} catch (Exception e) {
			json.put("stat", -2);
			json.put("result", false);
			json.put("msg", "控制器异常");
			return json;
		}
	}
	/**
	 * 车位锁车位状态设置
	 * @param dasId
	 * @return
	 */
	@RequestMapping("/dasReboot") 
	@ResponseBody
	public JSONObject dasReboot(String dasId){
		JSONObject json = new JSONObject();
		try {
			Integer stat = controlBiz.rebootGateway(dasId);
			if(stat == 0){
				json.put("result", true);
				json.put("msg", "网关重启成功");
			}else if(stat == 1){
				json.put("result", false);
				json.put("msg", "网关重启失败");
			}else if(stat== -1){
				json.put("result", false);
				json.put("msg", "业务异常");
			}else {
				json.put("result", false);
				json.put("msg", "未定义的返回值");
			}
			json.put("stat", stat);
			return json;
		} catch (Exception e) {
			json.put("stat", -2);
			json.put("result", false);
			json.put("msg", "控制器异常");
			return json;
		}
	}
	/**
	 * 车位锁喇叭 灯效控制
	 * @param slaveId
	 * @param action  0：控制
	 * @param type 1：喇叭 2：灯效 3：喇叭和灯效
	 * @return
	 */
	@RequestMapping("/lightSound")
	@ResponseBody
	public JSONObject lightSound(String dasId, String slaveId, Integer action,Integer type){
		JSONObject json = new JSONObject();
		try {
			Integer stat = 0;
			if (type == 1) {
				stat = controlBiz.soundControl(dasId, slaveId, action);
			} else if(type == 2){
				stat = controlBiz.lightControl(dasId, slaveId, action);
			}else {
				stat = controlBiz.soundLightControl(dasId, slaveId, action);
			}
			
			if(stat == 0){
				json.put("result", true);
				json.put("msg", "网关返回成功");
			}else if(stat == 1){
				json.put("result", false);
				json.put("msg", "网关返回失败");
			}else if(stat== -1){
				json.put("result", false);
				json.put("msg", "连接失败或者业务异常");
			}else {
				json.put("result", false);
				json.put("msg", "未定义的返回值");
			}
			json.put("stat", stat);
			return json;
		} catch (Exception e) {
			json.put("stat", -2);
			json.put("result", false);
			json.put("msg", "控制器异常");
			return json;
		}
	}
	
	
	
	

}
