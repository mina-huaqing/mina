package com.chinadovey.parking.webapps.biz.base;

import java.util.List;
import java.util.Map;

public interface BaseBiz<T> {
	
	/**
	 * 保存
	 * @author 王生栋
	 * @param entity 对象实体
	 * @return
	 */
	void save(T entity);
	
	/**
	 * 更新
	 * @author 王生栋
	 * @param 对象实体
	 * @return
	 */
	void update(T entity);
	
	/**
	 * 查询单个
	 * @author 王生栋
	 * @param 主键id
	 */
	T getOne(int id);
	
	/**
	 * 删除单个
	 * @author 王生栋
	 * @param id
	 */
	void delete(int id);
	
	/**
	 * 查询所有
	 * @author 王生栋
	 */
	List<T> getAll();
	
	/**
	 * 查询所有数量
	 * @author 王生栋
	 * @return
	 */
	int getCount();
	
	/**
	 * 分页查询
	 * @author 王生栋
	 * @param page 当前页
	 * @param row 每页记录数
	 * @return
	 */
	Map<String , Object> getPageList(int page , int rows);
	
}
