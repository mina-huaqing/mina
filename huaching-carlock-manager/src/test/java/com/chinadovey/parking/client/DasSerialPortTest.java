package com.chinadovey.parking.client;

import org.junit.Test;

import com.chinadovey.parking.webapps.mina.client.ParkCloudClient;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.DASSerialPortPackets;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class DasSerialPortTest {

	
	
	@Test
	public void conf() throws InterruptedException {
		try {
			DASSerialPortPackets dspp = new DASSerialPortPackets();
			dspp.setDasTag(ProtocolConst.CMD_CONF_DAS_PORT);
			String dasIdStr = "00000001";
			dspp.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String port1Str = "00000001";
			dspp.setPort1(ByteUtils.writeInt4(Integer.parseInt(port1Str,16), 0));
			String wire1Str = "0001";
			dspp.setWire1(ByteUtils.writeInt2(Integer.parseInt(wire1Str,16), 0));
			String channel1Str = "F1";
			dspp.setChannel1((byte)Integer.parseInt(channel1Str,16));
			String port2Str = "00000002";
			dspp.setPort2(ByteUtils.writeInt4(Integer.parseInt(port2Str,16), 0));
			String wire2Str = "0002";
			dspp.setWire2(ByteUtils.writeInt2(Integer.parseInt(wire2Str,16), 0));
			String channel2Str = "F2";
			dspp.setChannel2((byte)Integer.parseInt(channel2Str,16));
			
			ParkCloudClient cc = new ParkCloudClient("192.168.1.56:9005");
			byte[] tag = new byte[]{0x02,0x05};
			RESPackets res = cc.execute(tag, dspp, 1000l * 30);

			byte value[] = res.getValue();
			byte res1 = value[4];
			byte res2 = value[9];
			System.err.println("串口1========"+res1+"串口2=========="+res2);
			
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
		}

	}
	
	
	@Test
	public void simulateData(){
		try {
			RESPackets res = new RESPackets();
			res.setTag(new byte[]{0x00,0x05});
			String dasIdStr = "00000001";
			res.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String appId = "00000001";
			res.setAppId(ByteUtils.writeInt4(Integer.parseInt(appId,16), 0));
			byte[] value = new byte[]{0x00,0x00, 0x00 ,0x01 ,0x00 ,0x00 ,0x00 ,0x00 ,0x02, 0x00};
			res.setValue(value);
			
			ParkCloudClient cc = new ParkCloudClient("192.168.1.56:9005");
			byte[] tag = new byte[]{0x00,0x03};
			RESPackets re= cc.execute(tag, res, 1000l * 30);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
