package com.chinadovey.parking.webapps.mina.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.mina.pojo.Slave;
import com.chinadovey.parking.webapps.util.ByteUtils;

/**
 * TAG 0x0001H
 * 
 * @author Administrator
 * 
 */
public class DASSerialPortPackets implements ValuePackets {
	
	private byte[] dasTag;
	
	private byte[] appId;
	
	private byte[] dasId;
	
	private byte[] port1;
	
	private byte[] wire1;
	
	private byte channel1;
	
    private byte[] port2;
	
	private byte[] wire2;
	
	private byte channel2;
	
	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {
		try {
			setDasTag(Arrays.copyOfRange(buf, 0, 2));
			setAppId(Arrays.copyOfRange(buf, 2, 6));
			setDasId(Arrays.copyOfRange(buf, 6, 10));
			setPort1(Arrays.copyOfRange(buf, 10, 14));
			setWire1(Arrays.copyOfRange(buf, 14, 16));
			setChannel1(buf[16]);
			setPort2(Arrays.copyOfRange(buf, 17, 21));
			setWire2(Arrays.copyOfRange(buf, 21, 23));
			setChannel2(buf[23]);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ProtocolParseErrorException("解析DAS Packets时发生数组下标越界");
		}

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DAS TAG:	");
		sb.append(ByteUtils.asHex(getDasTag()));
		sb.append('\n');
		sb.append("DAS ID:		");
		sb.append(ByteUtils.asHex(getDasId()));
		sb.append('\n');
		sb.append("appId:      ");
		sb.append(ByteUtils.asHex(getAppId()));
		sb.append('\n');
		sb.append("port1:      ");
		sb.append(ByteUtils.asHex(getPort1()));
		sb.append('\n');
		sb.append("wire1:      ");
		sb.append(ByteUtils.asHex(getWire1()));
		sb.append('\n');
		sb.append("channel1:      ");
		sb.append(ByteUtils.asHex(getChannel1()));
		sb.append('\n');
		sb.append("port2:      ");
		sb.append(ByteUtils.asHex(getPort2()));
		sb.append('\n');
		sb.append("wire2:      ");
		sb.append(ByteUtils.asHex(getWire2()));
		sb.append('\n');
		sb.append("channel2:      ");
		sb.append(ByteUtils.asHex(getChannel2()));
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * 长度，方法目前没有用到，不完整
	 * @return
	 */
	public int getLength() {
		int length = 0;
		if (appId == null) appId = new byte[4];
		return length += dasTag.length + dasId.length+appId.length+port1.length+wire1.length+port2.length+wire2.length+2;
	}

	/**
	 * 获取实体字节码，方法目前没有用到，不完整
	 */
	@Override
	public byte[] getBytes() {
		byte[] val = new byte[getLength()];
		if (appId == null) appId = new byte[4];
		System.arraycopy(dasTag, 	0, val, 0, 			dasTag.length);
		System.arraycopy(appId, 	0, val, dasTag.length, appId.length);
		System.arraycopy(dasId, 	0, val, dasTag.length+appId.length, dasId.length);
		System.arraycopy(port1, 	0, val, dasTag.length+appId.length+dasId.length, port1.length);
		System.arraycopy(wire1, 	0, val, dasTag.length+appId.length+dasId.length+port1.length, wire1.length);
		val[dasTag.length+appId.length+dasId.length+port1.length+wire1.length]=channel1;
		System.arraycopy(port2, 	0, val, dasTag.length+appId.length+dasId.length+port1.length+wire1.length+1, port2.length);
		System.arraycopy(wire2, 	0, val, dasTag.length+appId.length+dasId.length+port1.length+wire1.length+1+port2.length,wire2.length);
		val[dasTag.length+appId.length+dasId.length+port1.length+wire1.length+1+port2.length+wire2.length]=channel2;
		return val;
	}

	public byte[] getDasTag() {
		return dasTag;
	}

	public void setDasTag(byte[] dasTag) {
		this.dasTag = dasTag;
	}

	public byte[] getAppId() {
		return appId;
	}

	public void setAppId(byte[] appId) {
		this.appId = appId;
	}

	public byte[] getDasId() {
		return dasId;
	}

	public void setDasId(byte[] dasId) {
		this.dasId = dasId;
	}

	public byte[] getPort1() {
		return port1;
	}

	public void setPort1(byte[] port1) {
		this.port1 = port1;
	}

	public byte[] getWire1() {
		return wire1;
	}

	public void setWire1(byte[] wire1) {
		this.wire1 = wire1;
	}

	public byte getChannel1() {
		return channel1;
	}

	public void setChannel1(byte channel1) {
		this.channel1 = channel1;
	}

	public byte[] getPort2() {
		return port2;
	}

	public void setPort2(byte[] port2) {
		this.port2 = port2;
	}

	public byte[] getWire2() {
		return wire2;
	}

	public void setWire2(byte[] wire2) {
		this.wire2 = wire2;
	}

	public byte getChannel2() {
		return channel2;
	}

	public void setChannel2(byte channel2) {
		this.channel2 = channel2;
	}
}
