package com.chinadovey.parking.webapps.mina.pojo;

public class EquimentRealTime {
	
	protected String rtuId;
	
	protected String equiId;

	private Equipment equipment;

	private RTURealTime rtuRealTime;

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public RTURealTime getRtuRealTime() {
		return rtuRealTime;
	}

	public void setRtuRealTime(RTURealTime rtuRealTime) {
		this.rtuRealTime = rtuRealTime;
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
