package com.chinadovey.parking.webapps.mina;

import java.util.List;

import com.chinadovey.parking.webapps.mina.exception.RTUProcessException;
import com.chinadovey.parking.webapps.mina.pojo.Equipment;
import com.chinadovey.parking.webapps.mina.protocol.RTUPackets;

public interface RTUProcess {
	public List<? extends Equipment> process(RTUPackets rtuPackets)
			throws RTUProcessException;
}
