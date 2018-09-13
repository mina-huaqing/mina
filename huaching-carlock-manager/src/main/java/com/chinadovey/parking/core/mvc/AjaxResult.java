package com.chinadovey.parking.core.mvc;

/**
 * Ajax请求的默认返回对象
 * @author Bean
 *
 */
public class AjaxResult {

	/**
	 * 返回结果标识
	 */
	private Result result;
	/**
	 * 消息
	 */
	private String msg;

	public enum Result {
		/**
		 * 成功，返回true
		 */
		SUCCESS("true"), 
		/**
		 * 失败，返回false
		 */
		FAIL("true");

		private String value;

		Result(String value) {
			this.value = value;
		}

		public String toString() {
			return value;
		}
	}

	public AjaxResult(Result result) {
		this.result = result;
	}

	public AjaxResult(Result result,String msg) {
		this.result = result;
		this.msg = msg;
	}

	public Result getResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}
}
