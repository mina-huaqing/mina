package com.chinadovey.parking.webapps.biz;

import java.util.Map;

import com.chinadovey.parking.webapps.biz.base.BaseBiz;
import com.chinadovey.parking.webapps.pojo.GatewayLog;

public interface GatewayLogBiz extends BaseBiz<GatewayLog>{

	Map<String, Object> getList(int page, int rows, String search, String sort, String order, String start,
			String end);

}
