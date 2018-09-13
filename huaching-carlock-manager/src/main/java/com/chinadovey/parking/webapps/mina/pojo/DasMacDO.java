package com.chinadovey.parking.webapps.mina.pojo;

import java.util.Date;

public class DasMacDO {
	
	private String dasId;
	private String dasIp;
	private String dasMac;
	private Date  reportTime;
	public String getDasId() {
		return dasId;
	}
	public void setDasId(String dasId) {
		this.dasId = dasId;
	}
	public String getDasIp() {
		return dasIp;
	}
	public void setDasIp(String dasIp) {
		this.dasIp = dasIp;
	}
	public String getDasMac() {
		return dasMac;
	}
	public void setDasMac(String dasMac) {
		this.dasMac = dasMac;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	

}
