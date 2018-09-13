package com.chinadovey.parking.client;


import org.junit.Test;

import com.chinadovey.parking.webapps.mina.client.CloudClient;
import com.chinadovey.parking.webapps.mina.protocol.DASAllReturnPackets;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;

public class CtrlAllTest {

	@Test
	public void test() throws InterruptedException {
		try {
			DASAllReturnPackets packets = new DASAllReturnPackets();
			packets.setAppId(new byte[]{0x00,0x00,0x00,0x00});
			packets.setDasId(new byte[]{0x00,0x00,0x00,0x01});
			packets.setDasTag(new byte[]{0x00,0x05});
			packets.setValue(new byte[]{0x00,0x00,0x00,0x00,0x00,0x01,0x00,
					                    0x00,0x00,0x00,0x00,0x00,0x02,0x00,
					                    0x00,0x00,0x00,0x00,0x00,0x03,0x00
			                             });
			
			CloudClient cc = new CloudClient("192.168.1.56:9005");
			RESPackets res = cc.execute(packets, 1000l * 30);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
