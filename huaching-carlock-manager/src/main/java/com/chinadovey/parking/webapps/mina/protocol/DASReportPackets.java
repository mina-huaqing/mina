package com.chinadovey.parking.webapps.mina.protocol;

import java.util.Arrays;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.util.ByteUtils;

/**
 * TAG 0x0004H
 * 网关 首次上报
 * @author Administrator
 * 
 */
public class DASReportPackets implements ValuePackets {
	private byte[] dasTag;
	private byte[] dasId;
	private byte[] value;
	
	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {

		try {
			setDasTag(Arrays.copyOfRange(buf, 0, 2));
			setDasId(Arrays.copyOfRange(buf, 2, 6));
			if (buf != null && buf.length == 11) {
				setValue(Arrays.copyOfRange(buf, 6, 11));//das版本上报   7.3.3之后版本新加入的
			} else {
				setValue(Arrays.copyOfRange(buf, 6, 7));//之前版本  没有版本号  赋值0x00
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ProtocolParseErrorException("解析DAS Packets时发生数组下标越界");
		}

	}

	public byte[] getDasTag() {
		return dasTag;
	}

	public void setDasTag(byte[] dasTag) {
		this.dasTag = dasTag;
	}

	public byte[] getDasId() {
		return dasId;
	}

	public void setDasId(byte[] dasId) {
		this.dasId = dasId;
	}
	
	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DAS TAG:	");
		sb.append(ByteUtils.asHex(getDasTag()));
		sb.append('\n');
		sb.append("DAS ID:		");
		sb.append(ByteUtils.asHex(getDasId()));
		sb.append('\n');
		sb.append("Value:      ");
		sb.append(ByteUtils.asHex(getValue()));
		sb.append('\n');
		return sb.toString();
	}

	public int getLength() {
		int length = 0;
		return length += dasTag.length + dasId.length+value.length;
	}

	@Override
	public byte[] getBytes() {
		byte[] val = new byte[getLength()];
		System.arraycopy(dasTag, 	0, val, 0, 			dasTag.length);
		System.arraycopy(dasId, 	0, val, dasTag.length, dasId.length);
		System.arraycopy(value, 	0, val, dasTag.length+dasId.length, value.length);
		return val;
	}
}
