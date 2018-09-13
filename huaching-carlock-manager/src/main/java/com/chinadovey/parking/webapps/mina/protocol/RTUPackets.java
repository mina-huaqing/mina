package com.chinadovey.parking.webapps.mina.protocol;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class RTUPackets extends RESPackets {
	private byte[] length;


	public byte[] getLength() {
		return length;
	}

	public void setLength(byte[] length) {
		this.length = length;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TAG:	");
		sb.append(ByteUtils.asHex(getTag()));
		sb.append('\n');
		sb.append("RTU ID:	");
		sb.append(ByteUtils.asHex(getRtuId()));
		sb.append('\n');
		sb.append("LENGTH:	");
		sb.append(ByteUtils.asHex(getLength()));
		sb.append('\n');
		sb.append("VALUES:	");
		sb.append(ByteUtils.asHex(getValue()));
		return sb.toString();
	}

	/**
	 * The parsed method has been implemented in DASPackets
	 * @see DASPackets#parsed(byte[])
	 * @throws RuntimeException
	 */
	@Deprecated
	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {
		throw new RuntimeException("The parsed method has been implemented in DASPackets");
	}

	@Override
	public byte[] getBytes() {
		
		int len = getTag().length + getRtuId().length + getLength().length + getValue().length;
		byte[] value = new byte[len];
		System.arraycopy(getTag(), 	0, value, 0, 											 			getTag().length);
		System.arraycopy(getRtuId(),0, value, getTag().length,											getRtuId().length);
		System.arraycopy(length, 	0, value, getTag().length + getRtuId().length, 						getLength().length);
		System.arraycopy(value, 	0, value, getTag().length + getRtuId().length + getLength().length,	getValue().length);
		return value;
	}
}
