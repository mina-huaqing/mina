package com.chinadovey.parking.webapps.mina.pojo;

public abstract class Equipment {

	protected String id;
	// 数值
	protected String rtuId;
	// 数值
	protected String equiId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRtuId() {
		return rtuId;
	}

	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}

	public String getEquiId() {
		return equiId;
	}

	public void setEquiId(String equiId) {
		this.equiId = equiId;
	}

}
