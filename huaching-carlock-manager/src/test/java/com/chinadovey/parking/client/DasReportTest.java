package com.chinadovey.parking.client;


import org.junit.Test;

import com.chinadovey.parking.webapps.mina.client.CloudClient;
import com.chinadovey.parking.webapps.mina.protocol.DASReportPackets;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;

public class DasReportTest {
	
	
	@Test
	public void firstReport(){
		try {
			DASReportPackets  packets = new DASReportPackets();
			packets.setDasId(new byte[]{0x00,0x00,0x00,0x01});
			packets.setDasTag(new byte[]{0x00,0x00});
			packets.setValue(new byte[]{0x00});
			
			CloudClient cc = new CloudClient("192.168.1.56:9005");
			RESPackets res = cc.execute(packets, 1000l * 30);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
