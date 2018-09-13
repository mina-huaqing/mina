package com.chinadovey.parking.webapps.mina.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.mina.pojo.Slave;
import com.chinadovey.parking.webapps.util.ByteUtils;

/**
 * TAG 0x0003H
 * 
 * @author Administrator
 * 
 */
public class RESPackets implements ValuePackets {
	
	private byte[] tag;
	private byte[] appId;
	private byte[] dasId;
	private byte[] rtuId;
	private byte[] value;
	List<Slave> slaves;

	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {
		try {
			setTag(Arrays.copyOfRange(buf, 0, 2));
			setAppId(Arrays.copyOfRange(buf, 2, 6));
			setDasId(Arrays.copyOfRange(buf, 6, 10));
			if(tag[1]==0x02){
				if(buf.length>10){
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
				}
			
			}else if(tag[1]==0x05){
				setValue(Arrays.copyOfRange(buf, 10, buf.length));
			}else{
				setRtuId(Arrays.copyOfRange(buf, 10, 14));
				setValue(Arrays.copyOfRange(buf, 14, buf.length));
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ProtocolParseErrorException("解析RES Packets时发生数组下标越界");
		}
	}

	public byte[] getTag() {
		return tag;
	}

	public void setTag(byte[] res) {
		this.tag = res;
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

	public byte[] getRtuId() {
		return rtuId;
	}

	public void setRtuId(byte[] rtuId) {
		this.rtuId = rtuId;
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

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("\tRES:		");
		sb.append(ByteUtils.asHex(getTag()));
		sb.append('\n');
		sb.append("\tAPP ID:		");
		sb.append(ByteUtils.asHex(getAppId()));
		sb.append('\n');
		sb.append("\tDAS ID:		");
		sb.append(ByteUtils.asHex(getDasId()));
		sb.append('\n');
		sb.append("\tRTU ID:		");
		sb.append(ByteUtils.asHex(getRtuId()));
		sb.append('\n');
		sb.append("\tRES DATA:	");
		sb.append(ByteUtils.asHex(getValue(), " "));
		return sb.toString();
	}

	@Override
	public byte[] getBytes() {
		if (dasId == null) dasId = new byte[4];
		if (appId == null) appId = new byte[4];
		
		int len = tag.length + appId.length + dasId.length;
		if(rtuId!=null){
			len+=rtuId.length;
		}
		if(value!=null){
			len+=value.length;
		}
		
		byte[] v = new byte[len];
		System.arraycopy(tag, 		0, v, 0, 														tag.length);
		System.arraycopy(appId, 	0, v, tag.length, 												appId.length);
		System.arraycopy(dasId, 	0, v, tag.length + appId.length, 								dasId.length);
		if(tag[1]==0x02 || tag[1]==0x05){
			if(value!=null){
				System.arraycopy(value, 	0, v, tag.length + appId.length + dasId.length , 	value.length);
			}
		}else{
			System.arraycopy(rtuId, 	0, v, tag.length + appId.length + dasId.length,					rtuId.length);
			System.arraycopy(value, 	0, v, tag.length + appId.length + dasId.length + rtuId.length, 	value.length);
			
		}
		
		return v;
	}
}
