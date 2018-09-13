package com.chinadovey.parking.webapps.mina.client;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.pojo.Slave;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class CtrlAllThread extends Thread{
	
	private String dasId; 
	
	private int operate;
	
	private String address;
	
	private List<Slave> slaves;
	
	private CountDownLatch latch;

	public CtrlAllThread(String dasId , int operate , String address , List<Slave> slaves, CountDownLatch latch){
		this.dasId = dasId;
		this.operate = operate;
		this.address = address;
		this.slaves = slaves;
		this.latch=latch;
	}
	
	public void run(){
		try {
			RESPackets res = ctrl(dasId,operate,address);
			if(res!=null){
				List<Slave> list = res.getSlaves();
				for(Slave slave:list){
					synchronized (slaves) {  //获得list锁对象，其他线程将不能获得list锁来来改变list对象。  
			            boolean absent = !slaves.contains(slave);  
			            if (absent)  
			            	slaves.add(slave);  
			        }
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			latch.countDown();
		}
	}
	
	private RESPackets ctrl(String dasId , int operate , String address) throws InterruptedException {
		try {
			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_ALL);		//开关通断命令
			String dasIdStr = dasId;
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String equiId = "0000000000FE";
			String rtuIdStr = equiId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));	//05060708 RTU ID
			byte[] b = new byte[13];
			String equiStr = equiId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			ByteUtils.writeInt2(equi, b, 0);// 0A0B 设备ID
			ByteUtils.writeByte(operate, b, 2);// 2 表示关 1表示开
			ByteUtils.writeInt2(0x00230012, b, 3);// 修改上报周期：1-65535秒，0表示不修改上报时间
			ByteUtils.writeInt4(0x00000000, b, 5);// 保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);// 保留字段
			cmd.setCmdData(b);
			CloudClient cc = new CloudClient(address);
			RESPackets res =  cc.execute(cmd, 1000l * 180);
			return res;
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getDasId() {
		return dasId;
	}

	public void setDasId(String dasId) {
		this.dasId = dasId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Slave> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<Slave> slaves) {
		this.slaves = slaves;
	}

}