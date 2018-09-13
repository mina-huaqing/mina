package com.chinadovey.parking.webapps.biz;

import java.util.List;

import com.chinadovey.parking.webapps.pojo.Carlock;

public interface CarLockBiz{

	Carlock getBySlaveid(String slaveId);

	boolean isSlaveIdExit(String slaveId);

	boolean isSlaveIdExitById(String slaveId, int id);

    void update(Carlock carlock);

	Integer getAll(List<String> dasIds);
	
	Integer countByStatus(Integer status);
	
	Integer countByCarStatus(Integer status);

	List<Carlock> getAll(Integer status);
	
	List<Carlock> getAllByIds(List<Integer> ids);
	
	List<Carlock> getAllBySlaveIds(List<String> ids);
	
	Integer countBySwitchStatus(Integer status);
	
	List<Carlock> getByCompanyNo(String companyNo);
	
}
