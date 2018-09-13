package com.chinadovey.parking.webapps.mappers.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;


public interface BaseMapper<T> {
	
	/**
	 * 插入
	 * @author 王生栋
	 * @param t
	 * @return
	 */
	int insert(T entity);
	
	/**
	 * 更新
	 * @author 王生栋
	 * @param entity
	 * @return
	 */
	int updateByPrimaryKeySelective(T entity);
	
	/**
	 * 根据id查找
	 * @author 王生栋
	 * @param id
	 * @return
	 */
	T selectByPrimaryKey(int id);
	
	/**
	 * 删除
	 * @author 王生栋
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(int id);
	
	/**
	 * 按条件查询
	 * @author 王生栋
	 * @param example
	 * @return
	 */
	List<T> selectByExample(T example);
	
	/**
	 * 分页查询
	 * @author 王生栋
	 * @param example
	 * @param rowBounds
	 * @return
	 */
	List<T> selectByExample(T example, RowBounds rowBounds);
	
	/**
	 * 查询所有数量
	 * @author 王生栋
	 * @param example
	 * @return
	 */
	int countByExample(T example);
	
	
}
