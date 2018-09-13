package com.chinadovey.parking.webapps.mina.temp;

public class SensorData {
	private int id;
	private int ecuId;
	private long equiId;
	private int type;
	private float value;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEcuId() {
		return ecuId;
	}

	public void setEcuId(int ecuId) {
		this.ecuId = ecuId;
	}

	public long getEquiId() {
		return equiId;
	}

	public void setEquiId(long equiId) {
		this.equiId = equiId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
