package com.chinadovey.parking.webapps.biz.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinadovey.parking.core.supports.mybatis.pagination.PageContext;
import com.chinadovey.parking.core.supports.mybatis.pagination.Pagination;
import com.chinadovey.parking.webapps.mappers.base.BaseMapper;

public abstract class BaseBizImpl<T> implements BaseBiz<T>{
	
	public abstract BaseMapper<T> getMapper();
	

	@Override
	public void save(T entity) {
		getMapper().insert(entity);
	}
	
	@Override
	public void update(T entity){
		getMapper().updateByPrimaryKeySelective(entity);
	}
	
	@Override
	public T getOne(int id){
		return getMapper().selectByPrimaryKey(id);
	}
	
	@Override
	public List<T> getAll(){
		return getMapper().selectByExample(null);
	}
	
	@Override
	public Map<String , Object> getPageList(int page , int rows){
		Map<String , Object> map = new HashMap<String, Object>();
		Pagination<?> pagin = PageContext.initialize(page,rows);
		List<T> list =  getMapper().selectByExample(null,pagin.getRowBounds());
	    map.put("rows",list);
		map.put("total", pagin.getTotalRows());
		return map;
	}
	
	@Override
	public void delete(int id){
		getMapper().deleteByPrimaryKey(id);
	}
	
	@Override
	public int getCount(){
		return getMapper().countByExample(null);
	}

}
