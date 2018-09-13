package com.chinadovey.parking.webapps.mina.pojo;

public class Equipment0810 extends Equipment {
	// 传感器类型
	private Byte type;
	@Deprecated
	private byte[] rtuIdbytes;
	@Deprecated
	private byte[] equiIdbytes;
	// 数值
	private Byte status;
	// 数值
	private Float value;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t\tTYPE:	").append(type).append('\n');
		sb.append("\t\tID:	").append(equiId).append('\n');
		sb.append("\t\tSTATUS:	").append(status).append('\n');
		sb.append("\t\tVALUE:	").append(value);
		return sb.toString();
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public byte[] getRtuIdbytes() {
		return rtuIdbytes;
	}

	public void setRtuIdbytes(byte[] rtuIdbytes) {
		this.rtuIdbytes = rtuIdbytes;
	}

	public byte[] getEquiIdbytes() {
		return equiIdbytes;
	}

	public void setEquiIdbytes(byte[] equiIdbytes) {
		this.equiIdbytes = equiIdbytes;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}
