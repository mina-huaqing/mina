package com.chinadovey.parking.webapps.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinadovey.parking.core.mvc.AbstractBaseController;
import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.biz.GatewayBiz;
import com.chinadovey.parking.webapps.pojo.Carlock;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping("/carlock")
@Controller
public class CarLockController extends AbstractBaseController{
	
	@Autowired
	private CarLockBiz carLockBiz;
	@Autowired
	private GatewayBiz gatewayBiz;
	
	
	@RequestMapping("/getByCarStatus")
	@ResponseBody
	public JSONObject getByCarStatus(Integer status){
		JSONObject json = new JSONObject();
		try {
			List<Carlock> list = carLockBiz.getAll(status);
			List<String> slaveIds = new ArrayList<String>();
			for (Carlock carLock : list) {
				slaveIds.add(carLock.getSlaveId());
			}
			JSONArray array = JSONArray.fromObject(slaveIds);
			json.put("slaveIds", array.toString());
			json.put("result", true);
			return json;
		} catch (Exception e) {
			json.put("result", false);
			return json;
		}
	}
	
	@RequestMapping("/getByIds")
	@ResponseBody
	public JSONObject getByIds(String ids){
		JSONObject json = new JSONObject();
		try {
			List<Integer> _ids = new ArrayList<Integer>();
			for(String id: ids.substring(1,ids.length()-1).split(",")){
				_ids.add(Integer.parseInt(id.trim()));
			}
			List<Carlock> carLocks = carLockBiz.getAllByIds(_ids);
			
			JSONArray array = JSONArray.fromObject(carLocks);
			json.put("carLocks", array.toString());
			json.put("result", true);
			return json;
		} catch (Exception e) {
			json.put("result", false);
			e.printStackTrace();
			return json;
		}
	}
	@RequestMapping("/getBySlaveIds")
	@ResponseBody
	public JSONObject getBySlaveIds(String ids){
		JSONObject json = new JSONObject();
		try {
			List<String> _ids = new ArrayList<String>();
			for(String id: ids.substring(1,ids.length()-1).split(",")){
				_ids.add(id.trim());
			}
			List<Carlock> carLocks = carLockBiz.getAllBySlaveIds(_ids);
			
			JSONArray array = JSONArray.fromObject(carLocks);
			json.put("carLocks", array.toString());
			json.put("result", true);
			return json;
		} catch (Exception e) {
			json.put("result", false);
			e.printStackTrace();
			return json;
		}
	}
	@RequestMapping("/findCompanyNo")
	@ResponseBody
	public JSONObject findCompanyNo(String slaveId){
		JSONObject json = new JSONObject();
		try {
			Carlock carLock = carLockBiz.getBySlaveid(slaveId);
			if (carLock != null) {
				json.put("companyNo", carLock.getCompanyNo());
				json.put("result", true);
			}
			return json;
		} catch (Exception e) {
			json.put("result", false);
			e.printStackTrace();
			return json;
		}
	}
}
