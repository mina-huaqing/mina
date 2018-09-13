package com.chinadovey.parking.webapps.mina.protocol;

import java.util.Arrays;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;

/**
 * TAG 0x0002H
 * 
 * @author Administrator
 * 
 */
public class ERRPackets implements ValuePackets {

	private byte[] tag;
	private byte[] dasId;
	private byte[] time;
	private byte[] code;

	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {
		try {
			setTag(Arrays.copyOfRange(buf, 0, 2));
			setDasId(Arrays.copyOfRange(buf, 2, 6));
			setTime(Arrays.copyOfRange(buf, 6, 10));
			setCode(Arrays.copyOfRange(buf, 10, 12));
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ProtocolParseErrorException("解析CMD Packets时发生数组下标越界", e);
		}
	}

	@Override
	public byte[] getBytes() {
		int len = tag.length + dasId.length + time.length + code.length;
		byte[] value = new byte[len];
		System.arraycopy(tag, 0, value, 0, tag.length);
		System.arraycopy(dasId, 0, value, tag.length, dasId.length);
		System.arraycopy(time, 0, value, tag.length + dasId.length, time.length);
		System.arraycopy(code, 0, value, tag.length + dasId.length
				+ time.length, code.length);
		return value;
	}

	public byte[] getTag() {
		return tag;
	}

	public void setTag(byte[] tag) {
		this.tag = tag;
	}

	public byte[] getDasId() {
		return dasId;
	}

	public void setDasId(byte[] dasId) {
		this.dasId = dasId;
	}

	public byte[] getTime() {
		return time;
	}

	public void setTime(byte[] time) {
		this.time = time;
	}

	public byte[] getCode() {
		return code;
	}

	public void setCode(byte[] code) {
		this.code = code;
	}
}
