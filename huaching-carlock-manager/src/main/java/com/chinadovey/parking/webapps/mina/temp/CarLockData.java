package com.chinadovey.parking.webapps.mina.temp;

public class CarLockData {
	private Long id;
	private Integer rtuId;
	private Long equiId;
	private Integer openState;
	private Integer carState;
	private Float voltage;
	private Integer equiState;
	private Integer cycle;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRtuId() {
		return rtuId;
	}
	public void setRtuId(Integer rtuId) {
		this.rtuId = rtuId;
	}
	public Long getEquiId() {
		return equiId;
	}
	public void setEquiId(Long equiId) {
		this.equiId = equiId;
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
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	
}
