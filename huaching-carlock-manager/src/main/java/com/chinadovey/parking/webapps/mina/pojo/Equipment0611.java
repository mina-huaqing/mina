package com.chinadovey.parking.webapps.mina.pojo;

public class Equipment0611 extends Equipment {
	// 状态 1:开 2:关
	private Byte status;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t\tID:	").append(id).append('\n');
		sb.append("\t\tEQUI ID:	").append(equiId).append('\n');
		sb.append("\t\tVALUE:	").append(status);
		return sb.toString();
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
