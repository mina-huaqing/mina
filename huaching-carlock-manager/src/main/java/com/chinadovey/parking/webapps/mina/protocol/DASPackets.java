package com.chinadovey.parking.webapps.mina.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;
import com.chinadovey.parking.webapps.util.ByteUtils;

/**
 * TAG 0x0001H
 * 
 * @author Administrator
 * 
 */
public class DASPackets implements ValuePackets {
	private byte[] dasTag;
	private byte[] dasId;
	private List<RTUPackets> rtus;
	
	private static final byte[] DEFAULT_APP_ID = new byte[4];

	@Override
	public void parsed(byte[] buf) throws ProtocolParseErrorException {

		try {
			setDasTag(Arrays.copyOfRange(buf, 0, 2));
			setDasId(Arrays.copyOfRange(buf, 2, 6));
			if(buf.length>7){
				setRtus(new ArrayList<RTUPackets>());
				RTUPackets rtu;
				int i = 6;
				while (i < buf.length) {
					rtu = new RTUPackets();
					rtu.setTag(Arrays.copyOfRange(buf, i, i += 2));
					rtu.setDasId(dasId);
					rtu.setAppId(DEFAULT_APP_ID);
					rtu.setRtuId(Arrays.copyOfRange(buf, i, i += 4));
					rtu.setLength(Arrays.copyOfRange(buf, i, i += 2));
					rtu.setValue(Arrays.copyOfRange(buf, i, i += ByteUtils.makeIntFromByte(rtu.getLength())));
					getRtus().add(rtu);
				}
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

	public List<RTUPackets> getRtus() {
		return rtus;
	}

	public void setRtus(List<RTUPackets> rtus) {
		this.rtus = rtus;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("DAS TAG:	");
		sb.append(ByteUtils.asHex(getDasTag()));
		sb.append('\n');
		sb.append("DAS ID:		");
		sb.append(ByteUtils.asHex(getDasId()));
		sb.append('\n');
		sb.append("RTU LIST:\n");
		int i = 0;
		for (RTUPackets rtu : getRtus()) {
			sb.append('\t');
			sb.append('=').append('=');
			sb.append(++i);
			sb.append('=').append('=');
			sb.append('\n');
			sb.append(rtu);
			sb.append('\n');
		}
		return sb.toString();
	}

	public int getLength() {
		int length = 0;
		for (RTUPackets rtu : rtus) {
			length += rtu.getTag().length;
			length += rtu.getRtuId().length;
			length += rtu.getLength().length;
			length += rtu.getValue().length;
		}
		return length += dasTag.length + dasId.length;
	}

	@Override
	public byte[] getBytes() {
		byte[] value = new byte[getLength()];
		System.arraycopy(dasTag, 	0, value, 0, 			dasTag.length);
		System.arraycopy(dasId, 	0, value, dasTag.length, dasId.length);

		int i = dasTag.length + dasId.length;
		for (RTUPackets rtu : rtus) {
			System.arraycopy(rtu.getTag(), 		0, value, i, rtu.getTag().length);		i+=rtu.getTag().length;
			System.arraycopy(rtu.getRtuId(), 	0, value, i, rtu.getRtuId().length);	i+=rtu.getRtuId().length;
			System.arraycopy(rtu.getLength(), 	0, value, i, rtu.getLength().length);	i+=rtu.getLength().length;
			System.arraycopy(rtu.getValue(), 	0, value, i, rtu.getValue().length);	i+=rtu.getValue().length;
		}
		return value;
	}
}
