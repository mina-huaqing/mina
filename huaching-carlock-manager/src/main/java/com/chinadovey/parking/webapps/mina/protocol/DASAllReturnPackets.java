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
public class DASAllReturnPackets implements ValuePackets {
	
	private byte[] dasTag;
	
	private byte[] appId;
	
	private byte[] dasId;
	
	private byte[] value;
	
	private List<Slave> slaves;
	
	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {

		try {
			setDasTag(Arrays.copyOfRange(buf, 0, 2));
			setAppId(Arrays.copyOfRange(buf, 2, 6));
			setDasId(Arrays.copyOfRange(buf, 6, 10));
			value = Arrays.copyOfRange(buf, 10, buf.length);
			slaves = new ArrayList<Slave>();
			for(int i=0 ;i<value.length;i+=7){
				Slave slave = new Slave();
				byte[] slaveId = Arrays.copyOfRange(value, i, i+6);
				byte[] res = Arrays.copyOfRange(value, i+6, i+7);
				slave.setSlaveId(ByteUtils.asHex(slaveId));
				slave.setRes(ByteUtils.makeIntFromByte(res));
				slaves.add(slave);
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
	
	public byte[] getAppId() {
		return appId;
	}

	public void setAppId(byte[] appId) {
		this.appId = appId;
	}

	public List<Slave> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<Slave> slaves) {
		this.slaves = slaves;
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
		sb.append(ByteUtils.asHex(getDasId()));
		sb.append('\n');
		sb.append("appId:      ");
		sb.append(ByteUtils.asHex(getAppId()));
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * 长度，方法目前没有用到，不完整
	 * @return
	 */
	public int getLength() {
		int length = 0;
		return length += dasTag.length + dasId.length+appId.length+value.length;
	}

	/**
	 * 获取实体字节码，方法目前没有用到，不完整
	 */
	@Override
	public byte[] getBytes() {
		byte[] val = new byte[getLength()];
		System.arraycopy(dasTag, 	0, val, 0, 			dasTag.length);
		System.arraycopy(appId, 	0, val, dasTag.length, appId.length);
		System.arraycopy(dasId, 	0, val, dasTag.length+appId.length, dasId.length);
		System.arraycopy(value, 	0, val, dasTag.length+appId.length+dasId.length, value.length);
		return val;
	}
}
