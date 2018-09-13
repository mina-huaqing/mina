package com.chinadovey.parking.core.supports.mybatis.pagination;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

public class Pagination<T> implements java.io.Serializable {

	private static final long serialVersionUID = -3435038120094008463L;
	private int page = 1; // 当前页
	private int rows=10;//每页大小,名字必须为rows
	private int totalPages = 0; // 总页数
	private int totalRows = 0; // 总数据数
	private int pageStartRow = 0; // 每页的起始行数
	private int pageEndRow = 0; // 每页显示数据的终止行数
	private boolean hasNextPage = false; // 是否有下一页
	private boolean hasPreviousPage = false; // 是否有前一页
	private List<T> data;// 传过一个list，就可以对list进行分页
	private RowBounds rowBounds=null;
	
	public RowBounds getRowBounds() {
		return rowBounds;
	}

	//public Pagination(){}
	
	public Pagination(int page, int rows){
		this.page = (page == 0 ? 1 : page);
		this.rows = (rows == 0 ? 10 : rows);
		this.pageStartRow = (page - 1) * rows;
		this.pageEndRow = pageStartRow + rows - 1;
		this.rowBounds=new RowBounds((page-1)*rows, rows);
	}
	
	public void setTotal(int totalRows){
		this.totalRows = totalRows;
		this.totalPages = Math.max(totalRows/rows+(totalRows%rows==0?0:1), 1);
		this.pageStartRow = (page-1)*rows+1;
		this.pageEndRow = Math.min(page*rows, totalRows);
		this.hasNextPage = page<totalPages;
		this.hasPreviousPage = page>1;
	}

	public int getPage() {
		return page;
	}

	public int getRows() {
		return rows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getPageStartRow() {
		return pageStartRow;
	}

	public int getPageEndRow() {
		return pageEndRow;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public List<T> getData() {
		return data;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}

