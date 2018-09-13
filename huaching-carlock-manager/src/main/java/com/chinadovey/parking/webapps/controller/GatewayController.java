package com.chinadovey.parking.webapps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinadovey.parking.core.mvc.AbstractBaseController;
import com.chinadovey.parking.webapps.biz.ControlBiz;
import com.chinadovey.parking.webapps.pojo.Gateway;

import net.sf.json.JSONObject;

@RequestMapping("/gateway")
@Controller
public class GatewayController extends AbstractBaseController{
	
	@Autowired 
	private ControlBiz controlBiz;
	
	/**
	 * 网关串口配置（车锁管控后台管理系统配置入口）
	 * @param data
	 * @return
	 */
	@RequestMapping("/gatewayConfig")
	@ResponseBody
	public JSONObject gatewayConfig(String data){
		JSONObject json = new JSONObject();
		try {
			Gateway _gateway = (Gateway) JSONObject.toBean(
					JSONObject.fromObject(data), Gateway.class);
			String dasId = _gateway.getDasId();
			String wireA = _gateway.getWirea();
			String channelA = _gateway.getChannela();
			String wireB = _gateway.getWireb();
			String channelB = _gateway.getChannelb();
			Integer stat = controlBiz.gatewayConfig(dasId, wireA, channelA, wireB, channelB);
			if (stat != -1) {
				json.put("result", true);
				json.put("msgCode", stat);
			}else {
				json.put("result", false);
			}
			return json;
		} catch (Exception e) {
			json.put("result", false);
			e.printStackTrace();
			return json;
		}
	}
	
	

}
