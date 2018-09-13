package com.chinadovey.parking.client;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Test;

import com.chinadovey.parking.webapps.mina.DPSProtocolCodecFactory;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.ERRPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.mina.protocol.ValuePackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class ParkCtrl {

	@Test
	public void test() throws InterruptedException {
		try {

			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);		//开关通断命令
			String dasIdStr = "00000001";
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String equiId = "000001070007";
			String rtuIdStr = equiId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));	//05060708 RTU ID
			byte[] b = new byte[13];
			String equiStr = equiId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			int operate = 1;
			ByteUtils.writeInt2(equi, b, 0);// 0A0B 设备ID
			ByteUtils.writeByte(operate, b, 2);// 2 表示关 1表示开
			ByteUtils.writeInt2(0x00230012, b, 3);// 修改上报周期：1-65535秒，0表示不修改上报时间
			ByteUtils.writeInt4(0x00000000, b, 5);// 保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);// 保留字段
			cmd.setCmdData(b);
			CloudClient cc = new CloudClient("192.168.1.56:9005");
			RESPackets res = cc.execute(cmd, 1000l * 30);
			int status = res.getValue()[2]&0x0f;
			switch(status){
			case 0x00:
				System.out.println("车位锁打开");
				break;
			case 0x01:
				System.out.println("车位锁关闭");
				break;
			case 0x02:
				System.out.println("正在打开");
				break;
			case 0x03:
				System.out.println("正在关闭");
				break;
			}
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void conf() throws InterruptedException {
		try {

			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);		//开关通断命令
			String dasIdStr = "00000001";
			cmd.setDasId(ByteUtils.writeInt4(Integer.parseInt(dasIdStr,16), 0));
			String equiId = "000001070007";
			String rtuIdStr = equiId.substring(0, 8);
			int rtuId = Integer.parseInt(rtuIdStr, 16);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0));	//05060708 RTU ID
			byte[] b = new byte[13];
			String equiStr = equiId.substring(8, 12);
			int equi = Integer.parseInt(equiStr, 16);
			int operate = 1;
			ByteUtils.writeInt2(equi, b, 0);// 0A0B 设备ID
			ByteUtils.writeByte(operate, b, 2);// 2 表示关 1表示开
			ByteUtils.writeInt2(0x00230012, b, 3);// 修改上报周期：1-65535秒，0表示不修改上报时间
			ByteUtils.writeInt4(0x00000000, b, 5);// 保留字段
			ByteUtils.writeInt4(0x00000000, b, 9);// 保留字段
			cmd.setCmdData(b);
			CloudClient cc = new CloudClient("192.168.1.56:9005");
			RESPackets res = cc.execute(cmd, 1000l * 30);
			int status = res.getValue()[2]&0x0f;
			switch(status){
			case 0x00:
				System.out.println("车位锁打开");
				break;
			case 0x01:
				System.out.println("车位锁关闭");
				break;
			case 0x02:
				System.out.println("正在打开");
				break;
			case 0x03:
				System.out.println("正在关闭");
				break;
			}
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
		}

	}
	
	
	public static void main(String[] args) {
		String equiId = "00000101000F";
		String equiStr = equiId.substring(8, 12);
		int equi = Integer.parseInt(equiStr, 16);
		System.err.println(equi);
	}
	
	

	class CloudClient {

		private Log		logger	= LogFactory.getLog(CloudClient.class);

		private String				cloudAddress;
		private NioSocketConnector	connector;
		private ConnectFuture		future;

		public CloudClient(String cloudAddress) {
			this.cloudAddress = cloudAddress;
		}

		private void init(IoHandlerAdapter ioHandler) {
			String ip = cloudAddress.substring(0, cloudAddress.indexOf(':'));
			int port = Integer.parseInt(cloudAddress.substring(cloudAddress
					.indexOf(':') + 1));

			connector = new NioSocketConnector();
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
			chain.addLast("loggingFilter", new LoggingFilter());
			chain.addLast("codecFilter", new ProtocolCodecFilter(
					new DPSProtocolCodecFactory()));
			connector.setHandler(ioHandler);
			connector.setConnectTimeoutCheckInterval(30);
			future = connector.connect(new InetSocketAddress(ip, port));
			future.awaitUninterruptibly();
		}

		private void dispose() {
			connector.dispose();
		}

		public RESPackets execute(ValuePackets valuePacket, long timeoutMillis)
				throws CMDExecErrorException {
			return execute(valuePacket, (short) 0, timeoutMillis);
		}

		public RESPackets execute(ValuePackets valuePacket, short cmdIndex, long timeoutMillis)
				throws CMDExecErrorException {

			// 1.准备数据
			final PacketsEntity[] _cache = new PacketsEntity[1];
			PacketsEntity packetsEntity = new PacketsEntity();
			packetsEntity.setTag(new byte[] { 0x02, 0x01 });
			packetsEntity.setIndex(new byte[2]);
			ByteUtils.writeInt2(cmdIndex, packetsEntity.getIndex(), 0);
			packetsEntity.setValuePackets(valuePacket);
			packetsEntity.finishFilling();

			// 2.初始化服务器连接
			init(new IoHandlerAdapter() {
				public void messageReceived(IoSession session, Object message)
						throws Exception {
					if (message instanceof PacketsEntity) {
						_cache[0] = (PacketsEntity) message;
						 session.close(true); 
					}
				}
			});

			long t1 = System.currentTimeMillis();

			// 3.发送命令
			IoSession session = future.getSession();
			session.write(packetsEntity);

			// 4.等待服务器响应
			session.getCloseFuture().awaitUninterruptibly(timeoutMillis);
			dispose();

			// 5.响应请求
			long t2 = System.currentTimeMillis();
			if (logger.isDebugEnabled())
				logger.debug("耗时：" + (t2 - t1));

			if (_cache[0] == null) {
				throw new CMDExecErrorException("设备未响应", null);
			}

			if (_cache[0].getValuePackets() instanceof RESPackets) {
				return (RESPackets) _cache[0].getValuePackets();
			}

			if (_cache[0].getValuePackets() instanceof ERRPackets) {
				ERRPackets err = (ERRPackets) _cache[0].getValuePackets();
				throw new CMDExecErrorException("连接失败", err);
			} else {
				throw new CMDExecErrorException("未知错误", null);
			}
		}
	}
}
