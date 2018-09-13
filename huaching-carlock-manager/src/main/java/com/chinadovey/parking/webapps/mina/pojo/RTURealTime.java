package com.chinadovey.parking.webapps.mina.pojo;

import java.io.Serializable;

/**
 * mongoDB
 * 
 * @author Administrator
 * 
 */
public class RTURealTime implements Serializable {

	private static final long serialVersionUID = 1147022291137344834L;

	private String id;
	private byte[] rtuIdBytes;
	private byte[] dasIdBytes;
	private String slaveAddress;
	private long sessionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getDasIdBytes() {
		return dasIdBytes;
	}

	public void setDasIdBytes(byte[] dasIdBytes) {
		this.dasIdBytes = dasIdBytes;
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

	public byte[] getRtuIdBytes() {
		return rtuIdBytes;
	}

	public void setRtuIdBytes(byte[] rtuIdBytes) {
		this.rtuIdBytes = rtuIdBytes;
	}
}
