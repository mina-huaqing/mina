package com.chinadovey.parking.client;

import org.junit.Test;

import com.chinadovey.parking.webapps.mina.client.ParkCloudClient;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class ConfDasTest {

	@Test
	public void add() throws InterruptedException {
		try {

			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_CONF_DAS_ADD);
			
			String dasIdStr = "00000001";
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			
			String equiStr = "000001070007";
			String rtuIdStr = equiStr.substring(0, 8);
			cmd.setRtuId(ByteUtils.writeInt4(Integer.parseInt(rtuIdStr,16), 0));

			byte[] b = new byte[11];
			String slaveIdStr = equiStr.substring(8, 12);
			ByteUtils.writeInt2(Integer.parseInt(slaveIdStr,16), b, 0);// 设备id
			int delay = 6000;
			ByteUtils.writeInt4(delay, b, 2);//延时（ms）
			b[6] = 0x01;
			b[7] = 0x07;
		    String wireIdStr = equiStr.substring(2,6);
			ByteUtils.writeInt2(Integer.parseInt(wireIdStr,16), b, 8);// 设备id
			String channelStr = equiStr.substring(6,8);
			ByteUtils.writeByte(Integer.parseInt(channelStr,16), b, 10);
			cmd.setCmdData(b);
			ParkCloudClient cc = new ParkCloudClient("projects.zhrhq.com:9005");
			byte[] tag = new byte[]{0x02,0x03};
			RESPackets res = cc.execute(tag, cmd, 1000l * 120);
			int status = res.getValue()[2]&0x0f;
			System.err.println(res.getValue());
			switch(status){
			case 0x00:
				System.out.println("配置成功！");
				break;
			case 0x01:
				System.out.println("配置失败！");
				break;
			}
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void delete() throws InterruptedException {
		try {
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_CONF_DAS_DEL);
			String dasIdStr = "00000105";
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String equiStr = "000001010005";
			String rtuIdStr = equiStr.substring(0, 8);
			cmd.setRtuId(ByteUtils.writeInt4(Integer.parseInt(rtuIdStr,16), 0));
			byte[] b = new byte[5];
			String slaveIdStr = equiStr.substring(8, 12);
			ByteUtils.writeInt2(Integer.parseInt(slaveIdStr,16), b, 0);// 设备id
			String wireIdStr = equiStr.substring(2,6);
			ByteUtils.writeInt2(Integer.parseInt(wireIdStr,16), b, 2);// 设备id
			String channelStr = equiStr.substring(6,8);
			ByteUtils.writeByte(Integer.parseInt(channelStr,16), b, 4);
			cmd.setCmdData(b);
			ParkCloudClient cc = new ParkCloudClient("192.168.1.56:9005");
			byte[] tag = new byte[]{0x02,0x04};
			RESPackets res = cc.execute(tag, cmd, 1000l * 30);

			int status = res.getValue()[2]&0x0f;
			switch(status){
			case 0x00:
				System.out.println("配置成功！");
				break;
			case 0x01:
				System.out.println("配置失败！");
				break;
			}
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
		}

	}
	
	
	@Test
	public void test(){
		try {
			String rtuIdStr = "00000202";
			Integer.parseInt(rtuIdStr,16);
			System.err.println(Integer.parseInt(rtuIdStr,16));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
