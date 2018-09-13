package com.chinadovey.parking.webapps.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.biz.ControlBiz;
import com.chinadovey.parking.webapps.biz.GatewayBiz;
import com.chinadovey.parking.webapps.mina.client.ParkCloudClient;
import com.chinadovey.parking.webapps.mina.protocol.CMDDasPackets;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.DASSerialPortPackets;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.pojo.Carlock;
import com.chinadovey.parking.webapps.pojo.Gateway;
import com.chinadovey.parking.webapps.util.ByteUtils;
import com.chinadovey.parking.webapps.util.ConfUtils;
import com.mysql.jdbc.log.Log;
@Service
public class ControlBizImpl implements ControlBiz{

	@Autowired 
	private CarLockBiz carLockBiz;
	@Autowired
	private  GatewayBiz gatewayBiz;
	
	@Override
	public Integer operate(String slaveId, int action) {
		try {
			Carlock carlock = carLockBiz.getBySlaveid(slaveId);
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);//开关通断命令
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(carlock.getDasId(),16), 0));
			String rtuIdStr = slaveId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));//05060708 RTU ID
			byte[] b = new byte[13];
			String equiStr = slaveId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);//0A0B 设备ID
			ByteUtils.writeByte(action, b, 2);//1表示开2 表示关
			ByteUtils.writeInt2(0x00230012, b, 3);//修改上报周期：1-65535秒，0表示不修改上报时间
			ByteUtils.writeInt4(0x00000000, b, 5);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);//保留字段
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			RESPackets res = cc.execute(cmd, 32 * 1000l);
			int stat = res.getValue()[2] & 0x0f;
			switch (stat) {
			case 0x00:
				System.out.println("车位锁打开");
				carlock.setSwitchStatus(1);
				carLockBiz.update(carlock);
				break;
			case 0x01:
				System.out.println("车位锁关闭");
				carlock.setSwitchStatus(2);
				carLockBiz.update(carlock);
				break;
			case 0x02:
				System.out.println("正在打开");
				break;
			case 0x03:
				System.out.println("正在关闭");
				break;
			}
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer autoOrHand(String slaveId, int action) {
		try {
			Carlock carlock = carLockBiz.getBySlaveid(slaveId);
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);//开关通断命令
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(carlock.getDasId(),16), 0));
			String rtuIdStr = slaveId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));//05060708 RTU ID
			byte[] b = new byte[13];
			String equiStr = slaveId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);//0A0B 设备ID
			ByteUtils.writeByte(action, b, 2);//1表示开2 表示关
			ByteUtils.writeInt2(0x00230012, b, 3);//修改上报周期：1-65535秒，0表示不修改上报时间
			ByteUtils.writeInt4(0x00000000, b, 5);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);//保留字段
			cmd.setCmdData(b);
			
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			RESPackets res = cc.execute(cmd, 32 * 1000l);
			int stat = res.getValue()[2] & 0x0f;
			if (stat == 0x00) {
				carlock.setIsAuto(action);
				carLockBiz.update(carlock);
			}
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer carlockConfig(String dasId, String slaveId,String serial) {
		try {
			Carlock carlock = carLockBiz.getBySlaveid(slaveId);
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_CONF_DAS_ADD);
			String dasIdStr = dasId;//8位网关id
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			
			String equiStr = slaveId;
			String rtuIdStr = equiStr.substring(0, 8);
			cmd.setRtuId(ByteUtils.writeInt4(Integer.parseInt(rtuIdStr,16), 0));
			
			byte[] b = new byte[11];
			String slaveIdStr = equiStr.substring(8, 12);
			ByteUtils.writeInt2(Integer.parseInt(slaveIdStr,16), b, 0);// 设备id

			int delay = 6000;
			ByteUtils.writeInt4(delay, b, 2);//延时（ms）
			if (serial.equals("00000001")) {
				b[6] = 0x00;
			}else{
				b[6] = 0x01;
			}
			b[7] = 0x07;
			String wireIdStr = equiStr.substring(2,6);
			ByteUtils.writeInt2(Integer.parseInt(wireIdStr,16), b, 8);
			String channelStr = equiStr.substring(6,8);
			ByteUtils.writeByte(Integer.parseInt(channelStr,16), b, 10);
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			byte[] tag = new byte[]{0x02,0x03};
			RESPackets res = cc.execute(tag, cmd, 1000l * 120);

			int status = res.getValue()[2]&0x0f;
			System.err.println(res.getValue());
			switch(status){
			case 0x00:
				if (serial.equals("00000001")) {
					carlock.setConfigStatus(0);
				}else{
					carlock.setConfigStatus(1);
				}
				carLockBiz.update(carlock);
				System.out.println("配置成功！");
				break;
			case 0x01:
				System.out.println("配置失败！");
				break;
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer carlockDelConfig(String dasId, String slaveId) {
		try {
			Carlock carlock = carLockBiz.getBySlaveid(slaveId);
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_CONF_DAS_DEL);
			String dasIdStr = dasId;//8位网关id
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			
			String equiStr = slaveId;
			String rtuIdStr = equiStr.substring(0, 8);
			cmd.setRtuId(ByteUtils.writeInt4(Integer.parseInt(rtuIdStr,16), 0));
			
			byte[] b = new byte[11];
			String slaveIdStr = equiStr.substring(8, 12);
			ByteUtils.writeInt2(Integer.parseInt(slaveIdStr,16), b, 0);// 设备id

			int delay = 6000;
			ByteUtils.writeInt4(delay, b, 2);//延时（ms）
			Integer flag = carlock.getConfigStatus();
			if (flag == 0) {
				b[6] = 0x00;//串口1
			}else {
				b[6] = 0x01;
			}
			b[7] = 0x07;
			String wireIdStr = equiStr.substring(2,6);
			ByteUtils.writeInt2(Integer.parseInt(wireIdStr,16), b, 8);
			String channelStr = equiStr.substring(6,8);
			ByteUtils.writeByte(Integer.parseInt(channelStr,16), b, 10);
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			byte[] tag = new byte[]{0x02,0x04};
			RESPackets res = cc.execute(tag, cmd, 1000l * 30);

			int status = res.getValue()[2]&0x0f;
			switch(status){
			case 0x00:
				carlock.setConfigStatus(2);
				carLockBiz.update(carlock);
				System.out.println("删除配置成功！");
				break;
			case 0x01:
				System.out.println("删除配置失败！");
				break;
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer gatewayConfig(String dasId, String wireA, String channelA, String wireB, String channelB) {
		try {
			Gateway gateway = gatewayBiz.getByDasId(dasId);
			gateway.setWirea(wireA);
			gateway.setChannelb(channelB);
			gateway.setWireb(wireB);
			gateway.setChannela(channelA);
			gateway.setSeriala("00000001");
			gateway.setSerialb("00000002");
			DASSerialPortPackets dspp = new DASSerialPortPackets();
			dspp.setDasTag(ProtocolConst.CMD_CONF_DAS_PORT);
			String dasIdStr = dasId;
			dspp.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String port1Str = "00000001";
			dspp.setPort1(ByteUtils.writeInt4(Integer.parseInt(port1Str,16), 0));
			String wire1Str = wireA;
			dspp.setWire1(ByteUtils.writeInt2(Integer.parseInt(wire1Str,16), 0));
			String channel1Str = channelA;
			dspp.setChannel1((byte)Integer.parseInt(channel1Str,16));
			String port2Str = "00000002";
			dspp.setPort2(ByteUtils.writeInt4(Integer.parseInt(port2Str,16), 0));
			String wire2Str = wireB;
			dspp.setWire2(ByteUtils.writeInt2(Integer.parseInt(wire2Str,16), 0));
			String channel2Str = channelB;
			dspp.setChannel2((byte)Integer.parseInt(channel2Str,16));
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			byte[] tag = new byte[]{0x02,0x05};
			RESPackets res = cc.execute(tag, dspp, 1000l * 30);
            byte value[] = res.getValue();
            
            byte res1 = value[4];
			byte res2 = value[9];
			System.err.println("串口1========"+res1+"串口2=========="+res2);
			gateway.setSerialaStatus((int)res1);
			gateway.setSerialbStatus((int)res2);
			Integer msgCode = 0;
			if((int)res1 == 0){
				if((int)res2 == 0){
					msgCode = 0;//0表示 串口1,2都配置成功
				}else{
					msgCode = 1;//1表示 串口1配置成功
				}
			}else{
				if((int)res2 == 0){
					msgCode = 2;//2表示 串口2配置成功
				}else{
					msgCode = 3;//3表示 串口1,2都配置失败
				}
			}
			gatewayBiz.update(gateway);
			return msgCode;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	@Override
	public Integer updateCarState(String dasId, String slaveId, int action) {
		try {
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_CAR_STATE_UPDATE);//修改有车、无车命令
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasId,16), 0));
			String rtuIdStr = slaveId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));
			byte[] b = new byte[13];
			String equiStr = slaveId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);//0A0B 设备ID
			ByteUtils.writeByte(action, b, 2);//1表示开2 表示关
			ByteUtils.writeInt2(0x0000, b, 3);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 5);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);//保留字段
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			RESPackets res = cc.execute(cmd, 32 * 1000l);
			int stat = res.getValue()[2] & 0x0f;
			switch (stat) {
			case 0x00:
				System.out.println("车位锁更新车位状态成功");
				Carlock carlock = carLockBiz.getBySlaveid(slaveId);
				carlock.setCarStatus(action);
				carLockBiz.update(carlock);
				break;
			case 0x01:
				System.out.println("车位锁更新车位状态失败");
				break;
			}
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer rebootGateway(String dasId) {
		try {
			CMDDasPackets cmd = new CMDDasPackets();
			cmd.setCmd(ProtocolConst.CMD_DAS_REBOOT);//网关重启命令
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasId,16), 0));
			byte[] b = new byte[12];
			ByteUtils.writeInt4(0x00000000, b, 0);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 4);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 8);//保留字段
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			byte[] tag = new byte[]{0x02,0x06};
			RESPackets res = cc.execute(tag, cmd, 1000l * 16);
			int stat = res.getValue()[2] & 0x0f;
			/*switch (stat) {
			case 0x00:
				System.out.println("重启成功");
				break;
			case 0x01:
				System.out.println("重启失败");
				break;
			}*/
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer soundControl(String dasId, String slaveId, int action) {
		try {
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_SOUND_CONTROL);//喇叭 打开
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasId,16), 0));
			String rtuIdStr = slaveId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));
			byte[] b = new byte[13];
			String equiStr = slaveId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);//0A0B 设备ID
			ByteUtils.writeByte(action, b, 2);//0表示开
			ByteUtils.writeInt2(0x0000, b, 3);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 5);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);//保留字段
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			RESPackets res = cc.execute(cmd, 16 * 1000l);
			int stat = res.getValue()[2] & 0x0f;
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer lightControl(String dasId, String slaveId, int action) {
		try {
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_LIGHT_CONTROL);//灯效 打开
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasId,16), 0));
			String rtuIdStr = slaveId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));
			byte[] b = new byte[13];
			String equiStr = slaveId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);//0A0B 设备ID
			ByteUtils.writeByte(action, b, 2);//0表示开
			ByteUtils.writeInt2(0x0000, b, 3);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 5);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);//保留字段
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			RESPackets res = cc.execute(cmd, 16 * 1000l);
			int stat = res.getValue()[2] & 0x0f;
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public Integer soundLightControl(String dasId, String slaveId, int action) {
		try {
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_SOUND_LIGHT_CONTROL);//喇叭和灯效 打开
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasId,16), 0));
			String rtuIdStr = slaveId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));
			byte[] b = new byte[13];
			String equiStr = slaveId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);//0A0B 设备ID
			ByteUtils.writeByte(action, b, 2);//0表示开
			ByteUtils.writeInt2(0x0000, b, 3);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 5);//保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);//保留字段
			cmd.setCmdData(b);
			
			String ip = ConfUtils.getControlAddress();
			ParkCloudClient cc = new ParkCloudClient(ip);
			RESPackets res = cc.execute(cmd, 16 * 1000l);
			int stat = res.getValue()[2] & 0x0f;
			return stat;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	

	
	
	

}
