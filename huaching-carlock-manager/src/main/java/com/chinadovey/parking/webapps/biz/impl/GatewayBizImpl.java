package com.chinadovey.parking.webapps.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.taglibs.standard.lang.jstl.BooleanLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.chinadovey.parking.core.supports.mybatis.pagination.PageContext;
import com.chinadovey.parking.core.supports.mybatis.pagination.Pagination;
import com.chinadovey.parking.webapps.biz.GatewayBiz;
import com.chinadovey.parking.webapps.biz.base.BaseBizImpl;
import com.chinadovey.parking.webapps.mappers.base.BaseMapper;
import com.chinadovey.parking.webapps.mappers.gen.GatewayMapper;
import com.chinadovey.parking.webapps.mina.client.ClientRealTime;
import com.chinadovey.parking.webapps.pojo.Gateway;
import com.chinadovey.parking.webapps.pojo.GatewayExample;

import com.chinadovey.parking.webapps.util.StringUtil;

@Service
public class GatewayBizImpl extends BaseBizImpl<Gateway> implements GatewayBiz{

	@Autowired
	private GatewayMapper mapper;
	@Autowired
	private MongoOperations mongoOps;
	@Override
	public BaseMapper<Gateway> getMapper() {
		return mapper;
	}
	@Override
	public Gateway getByDasId(String dasId) {
		GatewayExample example = new GatewayExample();
		example.createCriteria().andDasIdEqualTo(dasId);
		List<Gateway> list = mapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public Map<String, Object> getList(int page, int rows, String search, String sort, String order,String companyNo) {
		GatewayExample example = new GatewayExample();
		if (page <= 0 || rows <= 0) {
			page = 0;
			rows = 10;
		}
		if (sort != null && !sort.isEmpty()) {
			example.setOrderByClause(StringUtil.changeOrderStr(sort) + " " + order);
		}
        GatewayExample.Criteria criteria = example.createCriteria();
        if (search != null && !search.isEmpty()) {
			criteria.andGatewayNameLike("%" + search + "%");
			criteria.andDasIdEqualTo(search);
		}
        if (companyNo != null ) {
			criteria.andCompanyNoEqualTo(companyNo);
		}
        Map<String, Object> map = new HashMap<String, Object>();
		Pagination<?> pagin = PageContext.initialize(page, rows);
		List<Gateway> list = mapper.selectByExample(example, pagin.getRowBounds());
		map.put("rows", list);
		map.put("total", pagin.getTotalRows());
		return map;
	}
	@Override
	public boolean isDasIdExitById(String dasId, int id) {
		if (mapper.selectByPrimaryKey(id).getDasId().equals(dasId.toString())) {
			return false;
		}
		return isDasIdExit(dasId);
	}
	@Override
	public boolean isDasIdExit(String dasId) {
		GatewayExample example = new GatewayExample();
		example.createCriteria().andDasIdEqualTo(dasId.toString());
		return mapper.countByExample(example) > 0;
	}
	@Override
	public List<Gateway> getGateway(String companyNo) {
		GatewayExample example = new GatewayExample();
		example.createCriteria().andCompanyNoEqualTo(companyNo);
		return mapper.selectByExample(example);
	}
	@Override
	public List<Gateway> getAll(List<String> companys) {
		GatewayExample example = new GatewayExample();
		example.createCriteria().andCompanyNoIn(companys);
		return mapper.selectByExample(example);
	}
	@Override
	public Integer countByStatus(Integer status) {
		GatewayExample example = new GatewayExample();
		example.createCriteria().andGatewayStatusEqualTo(status);
		return mapper.countByExample(example);
	}
	@Override
	public Map<String, Object> getOnlineList(Integer page, Integer rows) {
		if (page <= 0 || rows <= 0) {
			page = 0;
			rows = 10;
		}
		GatewayExample example = new GatewayExample();
		Pagination<?> pagin = PageContext.initialize(page, rows);
		List<Gateway> gateway = mapper.selectByExample(example, pagin.getRowBounds());
		List<Integer> onLines = getOnlineList();
		int  oncount=0;
		int offcount=0;
		if (gateway != null && !gateway.isEmpty()) {
			for (Iterator it = gateway.iterator(); it.hasNext();) {
				Gateway gatway = (Gateway) it.next();
				if (onLines.contains(gatway.getId())) {
					oncount++;
				} else {
					offcount++;
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", gateway);
		map.put("oncount", oncount);
		map.put("offcount", offcount);
		map.put("total", pagin.getTotalRows());
		return map;
	}
	public List<Integer> getOnlineList() {
		List<ClientRealTime> clientRealTimes = mongoOps.findAll(ClientRealTime.class);
		List<Integer> onLineIds = new ArrayList<Integer>();
		for (ClientRealTime clientRealTime : clientRealTimes) {
			onLineIds.add(clientRealTime.getId());
		}
		return onLineIds;
	}
	
}
