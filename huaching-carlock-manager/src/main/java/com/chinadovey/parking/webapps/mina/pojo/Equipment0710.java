package com.chinadovey.parking.webapps.mina.pojo;


public class Equipment0710 extends Equipment {
	/**
	 * 车位锁开启状态<br>
	 * 0H 车位锁打开 挡臂处于平躺状态<br>
	 * 1H 车位锁关闭 挡臂处于竖起状态 <br>
	 * 2H 正在打开 挡臂正在倒下 <br>
	 * 3H 正在关闭 挡臂正在竖起 <br>
	 * EH 未知状态
	 */
	private Integer	openState;
	/**
	 * 车位锁车辆状态<br>
	 * 0H 车位上有车<br>
	 * 1H 车位上无车<br>
	 * 2H 预留<br>
	 * 3H 预留<br>
	 * EH 未知状态
	 */
	private Integer	carState;
	/**
	 * 电压
	 */
	private Float	voltage;

	/**
	 * 设备状态<br>
	 * 00H 正常 无故障和警告<br>
	 * 01H 电量低<br>
	 * 02H 挡臂故障 挡臂被人为扳动<br>
	 * 04H 电机故障<br>
	 * 08H 机壳被打开<br>
	 * 10H 行程开关损坏<br>
	 * 20H 车辆非法闯入<br>
	 */
	private Byte	equiState;

	/**
	 * 上报周期
	 */
	private Short	cycle;
	
	
	private Integer  source;
	
	
	private Integer carStatus;
	
	
	private Integer hx;
	
	private Integer lx;
	
	private Integer  hy;
	
	private Integer ly;
	
	private Integer hz;
	
	private Integer lz;
	
	private byte[]  collectTime;
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t\tOPEN_STATE:	").append(openState).append('\n');
		sb.append("\t\tCAR_STATE:	").append(carState).append('\n');
		sb.append("\t\tVOLTAGE:		").append(voltage).append('\n');
		sb.append("\t\tEQUI_STATE:	").append(equiState).append('\n');
		sb.append("\t\tCYCLE:		").append(cycle).append('\n');
		return sb.toString();
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

	public Byte getEquiState() {
		return equiState;
	}

	public void setEquiState(Byte equiState) {
		this.equiState = equiState;
	}

	public Short getCycle() {
		return cycle;
	}

	public void setCycle(Short cycle) {
		this.cycle = cycle;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(Integer carStatus) {
		this.carStatus = carStatus;
	}

	public Integer getHx() {
		return hx;
	}

	public void setHx(Integer hx) {
		this.hx = hx;
	}

	public Integer getLx() {
		return lx;
	}

	public void setLx(Integer lx) {
		this.lx = lx;
	}

	public Integer getHy() {
		return hy;
	}

	public void setHy(Integer hy) {
		this.hy = hy;
	}

	public Integer getLy() {
		return ly;
	}

	public void setLy(Integer ly) {
		this.ly = ly;
	}

	public Integer getHz() {
		return hz;
	}

	public void setHz(Integer hz) {
		this.hz = hz;
	}

	public Integer getLz() {
		return lz;
	}

	public void setLz(Integer lz) {
		this.lz = lz;
	}

	public byte[] getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(byte[] collectTime) {
		this.collectTime = collectTime;
	}
	
	
	
}
