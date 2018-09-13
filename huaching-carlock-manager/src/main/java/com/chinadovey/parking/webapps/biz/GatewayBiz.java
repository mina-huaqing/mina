package com.chinadovey.parking.webapps.biz;

import java.util.List;
import java.util.Map;

import com.chinadovey.parking.webapps.biz.base.BaseBiz;
import com.chinadovey.parking.webapps.pojo.Gateway;

public interface GatewayBiz extends BaseBiz<Gateway>{
	/**
	 * 网管在线监控
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> getOnlineList(Integer page, Integer rows);
	Gateway getByDasId(String dasId);

	Map<String, Object> getList(int page, int rows, String search, String sort, String order,String companyNo);

	boolean isDasIdExitById(String dasId, int id);

	boolean isDasIdExit(String dasId);

	List<Gateway> getGateway(String companyNo);

	List<Gateway> getAll(List<String> companys);
	
	Integer countByStatus(Integer status);

}
