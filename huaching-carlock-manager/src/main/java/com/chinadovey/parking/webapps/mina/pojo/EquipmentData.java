package com.chinadovey.parking.webapps.mina.pojo;

public class EquipmentData {
	
	private String slaveId;
	
	private Integer openState;
	
	private Integer carState;
	
	private Float voltage;
	
	private Integer equiState;

	public String getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(String slaveId) {
		this.slaveId = slaveId;
	}

	public Integer getOpenState() {
		return openState;
	}

	public void setOpenState(Integer openState) {
		this.openState = openState;
	}

	public Integer getCarState() {
		return carState;
	}

	public void setCarState(Integer carState) {
		this.carState = carState;
	}

	public Float getVoltage() {
		return voltage;
	}

	public void setVoltage(Float voltage) {
		this.voltage = voltage;
	}

	public Integer getEquiState() {
		return equiState;
	}

	public void setEquiState(Integer equiState) {
		this.equiState = equiState;
	}
	
}
