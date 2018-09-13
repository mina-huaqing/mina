package com.chinadovey.parking.core.supports.mybatis.pagination.dialect;
/**
 * Copyright ©2015 中瑞华清（北京）智能科技有限公司  http://zhrhq.com
 * @author 王生栋
 * 2015-6-18 下午3:05:11
 */
public class MySQLDialect extends Dialect{

	@Override
	public boolean supportsLimitOffset(){
		return true;
	}

	@Override
    public boolean supportsLimit() {   
        return true;   
    }
	
	@Override
	public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
        if (offset > 0) {   
        	return sql + " limit "+offsetPlaceholder+","+limitPlaceholder; 
        } else {   
            return sql + " limit "+limitPlaceholder;
        }  
	}   
  
}
