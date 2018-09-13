package com.chinadovey.parking.webapps.mina.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.mina.proxy.utils.ByteUtilities;

import com.chinadovey.parking.webapps.mina.RTUProcess;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0611;

/**
 * <li>0611H 带控制的状态传感器 <li>市政路灯、开关等，详见带控制的状态型传感器
 * 
 * @author Bean
 * 
 */
public class RTUProcess0611 implements RTUProcess {

	@Override
	public List<Equipment0611> process(RTUPackets rtuPackets) {

		rtuPackets.getValue();

		List<Equipment0611> list = new ArrayList<Equipment0611>();
		Equipment0611 data;
		int i = 0;
		while (i < rtuPackets.getValue().length) {
			data = new Equipment0611();
			data.setId(UUID.randomUUID().toString());
			data.setRtuId(ByteUtilities.asHex(rtuPackets.getRtuId()));
			data.setEquiId(ByteUtilities.asHex(Arrays.copyOfRange(rtuPackets.getValue(), i, i += 2)));
			//
			data.setStatus(rtuPackets.getValue()[i += 2]);
			i++;
			list.add(data);
		}

		return list;
	}
}
