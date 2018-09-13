package com.chinadovey.parking.webapps.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.mappers.gen.CarlockMapper;
import com.chinadovey.parking.webapps.pojo.Carlock;
import com.chinadovey.parking.webapps.pojo.CarlockExample;
@Service
public class CarLockBizImpl  implements CarLockBiz{

	@Autowired
	private CarlockMapper mapper;
	
	@Override
	public Carlock getBySlaveid(String slaveId) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andSlaveIdEqualTo(slaveId);
		List<Carlock> list = mapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public boolean isSlaveIdExit(String slaveId) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andSlaveIdEqualTo(slaveId);
		return mapper.countByExample(example) > 0;
	}
	@Override
	public boolean isSlaveIdExitById(String slaveId, int id) {
		if (mapper.selectByPrimaryKey(id).getSlaveId().equals(slaveId)) {
			return false;
		}
		return isSlaveIdExit(slaveId);
	}
	
	@Override
	public void update(Carlock carlock) {
		mapper.updateByPrimaryKey(carlock);
	}
	@Override
	public Integer getAll(List<String> dasIds) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andDasIdIn(dasIds);
		return mapper.countByExample(example);
	}
	@Override
	public Integer countByStatus(Integer status) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andRunStatusEqualTo(status);
		return mapper.countByExample(example);
	}
	@Override
	public Integer countByCarStatus(Integer status) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andCarStatusEqualTo(status);
		return mapper.countByExample(example);
	}
	@Override
	public List<Carlock> getAll(Integer status) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andCarStatusEqualTo(status);
		return mapper.selectByExample(example);
	}
	@Override
	public List<Carlock> getAllByIds(List<Integer> ids) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andIdIn(ids);
		List<Carlock> list = mapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list;
		}
		return null;
	}
	@Override
	public List<Carlock> getAllBySlaveIds(List<String> ids) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andSlaveIdIn(ids);
		List<Carlock> list = mapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list;
		}
		return null;
	}
	
	@Override
	public Integer countBySwitchStatus(Integer status) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andSwitchStatusEqualTo(status);
		return mapper.countByExample(example);
	}
	@Override
	public List<Carlock> getByCompanyNo(String companyNo) {
		CarlockExample example = new CarlockExample();
		example.createCriteria().andCompanyNoEqualTo(companyNo);
		List<Carlock> list = mapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list;
		}
		return null;
	}
	
}
