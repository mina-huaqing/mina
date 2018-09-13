package com.chinadovey.parking.core.supports.mybatis.pagination;


/**
 * 
* @ClassName: PageContext 
* @Description: 页面类扩展，通过ThreadLocal控制页面参数
* @Author "曾季伟"
* @Date 2012-2-22 下午4:19:46 
*
 */
public class PageContext<T> extends Pagination<T>{
	
	private static final long serialVersionUID = 438707583621326425L;

	private PageContext(int page, int rows) {
		super(page, rows);
	}
	/***
	 * ThreadLocal设置每次查询的查询参数，通过mybatis的拦截，从线程中获取查询参数，并将查询的总数放在参数中，注意的问题是如果一个功能需要查询多个分页时，
	 * 则需要每次都重新设置context中PageContext的参数值
	 */
	
	private static ThreadLocal<Pagination<?>> context = new ThreadLocal<Pagination<?>>();

	public static <T> Pagination<T> getContext() {
		@SuppressWarnings("unchecked")
		Pagination<T> pc = (Pagination<T>) context.get();
		return pc;
	}

	public static void removeContext() {
		context.remove();
	}

	/**
	 * 
	 * @param page 当前页码
	 * @param rows 显示条目数
	 * @return
	 */
	public static <T> Pagination<T> initialize(int page, int rows) {
		PageContext<T> pc = new PageContext<T>(page,rows);
		context.set(pc);
		return pc;
	}
	/**
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public static <T> Pagination<T> initialize(Pagination<T> pc) {
		context.set(pc);
		return pc;
	}

}
