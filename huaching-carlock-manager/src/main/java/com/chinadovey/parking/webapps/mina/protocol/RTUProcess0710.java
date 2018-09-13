package com.chinadovey.parking.webapps.mina.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.mina.proxy.utils.ByteUtilities;

import com.chinadovey.parking.webapps.mina.RTUProcess;
import com.chinadovey.parking.webapps.mina.exception.RTUProcessException;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0710;

public class RTUProcess0710 implements RTUProcess {

	@Override
	public List<Equipment0710> process(RTUPackets rtuPackets) throws RTUProcessException {

		List<Equipment0710> list = new ArrayList<Equipment0710>();
		Equipment0710 data;
		int i = 0;
		while (i < rtuPackets.getValue().length) {
			try {
				data = new Equipment0710();
				data.setId(UUID.randomUUID().toString());
				data.setRtuId(ByteUtilities.asHex(rtuPackets.getRtuId()));
				data.setEquiId(ByteUtilities.asHex(Arrays.copyOfRange(rtuPackets.getValue(), i,
						i += 2)));

				/*data.setOpenState(rtuPackets.getValue()[i] & 0x0f);// 地位
				data.setCarState(rtuPackets.getValue()[i] >> 4);// 高位
*/				
				data.setOpenState((int)rtuPackets.getValue()[i]);
				i+=1;
				data.setVoltage(Float.intBitsToFloat(ByteUtilities.makeIntFromByte4(
						rtuPackets.getValue(), i)));
				i += 4;
				data.setEquiState(rtuPackets.getValue()[i]);
				i+=1;
				data.setCycle((short) ByteUtilities.makeIntFromByte2(rtuPackets.getValue(), i));
				i += 2;
				data.setSource((int)rtuPackets.getValue()[i]);
				data.setCarStatus((int)rtuPackets.getValue()[i+=1]);
				data.setHx((int)rtuPackets.getValue()[i+=1]);
				data.setLx((int)rtuPackets.getValue()[i+=1]);
				data.setHy((int)rtuPackets.getValue()[i+=1]);
				data.setLy((int)rtuPackets.getValue()[i+=1]);
				data.setHz((int)rtuPackets.getValue()[i+=1]);
				data.setLz((int)rtuPackets.getValue()[i+=1]);
				data.setCollectTime(Arrays.copyOfRange(rtuPackets.getValue(), i, i += 4));
				i++;
				list.add(data);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new RTUProcessException("解析RES Packets时发生数组下标越界");
			}
		}

		return list;
	}
}
