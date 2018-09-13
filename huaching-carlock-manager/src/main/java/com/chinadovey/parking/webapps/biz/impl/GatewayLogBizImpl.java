package com.chinadovey.parking.webapps.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinadovey.parking.core.supports.mybatis.pagination.PageContext;
import com.chinadovey.parking.core.supports.mybatis.pagination.Pagination;
import com.chinadovey.parking.core.supports.uitls.DateUtils;
import com.chinadovey.parking.webapps.biz.GatewayLogBiz;
import com.chinadovey.parking.webapps.biz.base.BaseBizImpl;
import com.chinadovey.parking.webapps.mappers.base.BaseMapper;
import com.chinadovey.parking.webapps.mappers.gen.GatewayLogMapper;
import com.chinadovey.parking.webapps.pojo.GatewayLog;
import com.chinadovey.parking.webapps.pojo.GatewayLogExample;
import com.chinadovey.parking.webapps.util.StringUtil;
@Service
public class GatewayLogBizImpl extends BaseBizImpl<GatewayLog> implements GatewayLogBiz{

	@Autowired
	private GatewayLogMapper mapper;
	@Override
	public BaseMapper<GatewayLog> getMapper() {
		return mapper;
	}
	@Override
	public Map<String, Object> getList(int page, int rows, String search, String sort, String order, String start,
			String end) {
		GatewayLogExample example = new GatewayLogExample();
		if (page <= 0 || rows <= 0) {
			page = 0;
			rows = 10;
		}
		if (sort != null && !sort.isEmpty()) {
			example.setOrderByClause(StringUtil.changeOrderStr(sort) + " " + order);
		}
        GatewayLogExample.Criteria criteria = example.createCriteria();
        if (search != null && !search.isEmpty()) {
			criteria.andDasIdLike("%" + search + "%");
		}
        if (!start.equals("0")) {
        	criteria.andTimeBetween(DateUtils.stringConvertDate(start, 0), DateUtils.stringConvertDate(end, 0));
		}
        Map<String, Object> map = new HashMap<String, Object>();
		Pagination<?> pagin = PageContext.initialize(page, rows);
		List<GatewayLog> list = mapper.selectByExample(example, pagin.getRowBounds());
		map.put("rows", list);
		map.put("total", pagin.getTotalRows());
		return map;
	}
	
	
	

}
