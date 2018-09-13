package com.chinadovey.parking.webapps.mina.protocol;

import java.util.Arrays;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.util.ByteUtils;

/**
 * 心跳包
 * @author feng
 * 
 */
public class HeartPackets implements ValuePackets {
	
	public static final byte CODE = 0X00;
	public static final byte[] CODES = new byte[] { CODE,CODE,CODE,CODE };
	
	private byte[] addCode = CODES;
	
	public byte[] getAddCode() {
		return addCode;
	}
    public void setAddCode(byte[] addCode) {
		this.addCode = addCode;
	}

	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {

		try {
			setAddCode(Arrays.copyOfRange(buf, 0, 4));
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ProtocolParseErrorException("解析DAS Packets时发生数组下标越界");
		}

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DAS TAG:	");
		sb.append(ByteUtils.asHex(getAddCode()));
		sb.append('\n');
		return sb.toString();
	}

	public int getLength() {
		int length = 0;
		return length += addCode.length;
	}

	@Override
	public byte[] getBytes() {
		byte[] val = new byte[getLength()];
		System.arraycopy(addCode, 	0, val, 0, 			addCode.length);
		return val;
	}
}
