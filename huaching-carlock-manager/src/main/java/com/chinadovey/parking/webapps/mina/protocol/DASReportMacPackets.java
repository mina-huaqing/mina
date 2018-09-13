package com.chinadovey.parking.webapps.mina.protocol;

import java.util.Arrays;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.util.ByteUtils;

/**
 * TAG 0x0006H
 * 网关 mac地址，局域网ip地址上报
 * @author jellard
 */
public class DASReportMacPackets implements ValuePackets {
	private byte[] dasTag;
	private byte[] dasId;
	private byte[] mlength;
	private byte[] dasIp;
	private byte[] dasMac;
	private byte[] value;
	
	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {

		try {
			setDasTag(Arrays.copyOfRange(buf, 0, 2));
			setDasId(Arrays.copyOfRange(buf, 2, 6));
			setMlength(Arrays.copyOfRange(buf, 6, 7));
			setDasIp(Arrays.copyOfRange(buf, 7, 11));
			setDasMac(Arrays.copyOfRange(buf, 11, 17));
			setValue(Arrays.copyOfRange(buf, 17, 41));
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ProtocolParseErrorException("解析DAS MacPackets时发生数组下标越界");
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
	
	public byte[] getDasIp() {
		return dasIp;
	}

	public void setDasIp(byte[] dasIp) {
		this.dasIp = dasIp;
	}

	public byte[] getDasMac() {
		return dasMac;
	}

	public void setDasMac(byte[] dasMac) {
		this.dasMac = dasMac;
	}

	public byte[] getMlength() {
		return mlength;
	}

	public void setMlength(byte[] mlength) {
		this.mlength = mlength;
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
		sb.append("Length:		");
		sb.append(ByteUtils.asHex(getMlength()));
		sb.append('\n');
		sb.append("DAS IP:		");
		sb.append(ByteUtils.asHex(getDasIp()));
		sb.append('\n');
		sb.append("DAS Mac:		");
		sb.append(ByteUtils.asHex(getDasMac()));
		sb.append('\n');
		sb.append("Value:      ");
		sb.append(ByteUtils.asHex(getValue()));
		sb.append('\n');
		return sb.toString();
	}

	public int getLength() {
		int length = 0;
		return length += dasTag.length + dasId.length+value.length+dasIp.length+dasMac.length+mlength.length;
	}

	@Override
	public byte[] getBytes() {
		byte[] val = new byte[getLength()];
		int i = 0;
		System.arraycopy(dasTag, 	0, val, 0, 			dasTag.length);
		System.arraycopy(dasId, 	0, val, i+=dasTag.length, dasId.length);
		System.arraycopy(mlength, 	0, val, i+=dasId.length, mlength.length);
		System.arraycopy(dasIp, 	0, val, i+=mlength.length, dasIp.length);
		System.arraycopy(dasMac, 	0, val, i+=dasIp.length, dasMac.length);
		System.arraycopy(value, 	0, val, i+=dasMac.length, value.length);
		return val;
	}
}
