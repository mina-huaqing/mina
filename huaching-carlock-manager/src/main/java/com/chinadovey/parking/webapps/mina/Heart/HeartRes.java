package com.chinadovey.parking.webapps.mina.Heart;

import java.io.Serializable;

public class HeartRes implements Serializable{
	
	
	private static final long serialVersionUID = -9064500870898330981L;
	private String dasId;
	private String slaveAddress;
	private long   sessionId;
	private String lastTime;
	public String getDasId() {
		return dasId;
	}
	public void setDasId(String dasId) {
		this.dasId = dasId;
	}
	public String getSlaveAddress() {
		return slaveAddress;
	}
	public void setSlaveAddress(String slaveAddress) {
		this.slaveAddress = slaveAddress;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	

}
