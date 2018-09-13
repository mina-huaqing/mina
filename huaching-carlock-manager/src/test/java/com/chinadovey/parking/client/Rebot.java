package com.chinadovey.parking.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.chinadovey.parking.webapps.mina.DPSProtocolCodecFactory;
import com.chinadovey.parking.webapps.mina.client.CloudClient;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.DASPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.mina.protocol.RTUPackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class Rebot {

	public static void main(String[] args) {
		for (int i = 0; i < 0x10; i++) {
			new Thread(new EquiSimuUnit("121.40.52.180:9000",
					Integer.toHexString(0xfe0000 + i))).start();
		}
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 0x10; i++) {
			new Thread(new CtrlSimuUnit()).start();
		}
	}
}

class CtrlSimuUnit implements Runnable {

	public void run() {
		for (int i = 0; i < 10; i++) {

			long t1 = System.currentTimeMillis();
			String rtuId = Integer.toHexString(0xfe0000 + new Random(System
					.currentTimeMillis()).nextInt(0x1));
			test("121.40.52.180:9000",
					rtuId,
					(byte) (new Random(System.currentTimeMillis()).nextInt(1) + 1));

			long t2 = System.currentTimeMillis();
			System.out.println("耗时："+(t2-t1));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void test(String cloudAddress, String rtuId, byte _status) {
		try {

			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);
			cmd.setRtuId(ByteUtils.writeInt4(rtuId, 0)); // 65,66,67
			cmd.setCmdData(new byte[] {
			/* SLAVE */0x00, 0x00,
			/* STATUS */_status });

			// 2.调用接口
			CloudClient cc = new CloudClient(cloudAddress);
			RESPackets res = cc.execute(cmd, 1000l * 50);

			System.out.println("正确");
			System.out.println(res.getValue()[2]);

		} catch (CMDExecErrorException e) {
			System.out.println("错误");
			e.printStackTrace();
		}
	}
}

class EquiSimuUnit implements Runnable {

	private String cloudAddress;
	private NioSocketConnector connector;
	private ConnectFuture future;
	private String[] rtuIds;

	public EquiSimuUnit(String cloudAddress, String... rtuIds) {
		this.cloudAddress = cloudAddress;
		this.rtuIds = rtuIds;
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

	@Override
	public void run() {
		// 2.初始化服务器连接
		init(new IoHandlerAdapter() {
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				System.out.println("rec:"+message);
				if (message instanceof PacketsEntity) {
					PacketsEntity packets = (PacketsEntity) message;
					if (packets.getValuePackets() instanceof CMDPackets) {
						CMDPackets cmdPackets = (CMDPackets) packets
								.getValuePackets();

						// 写入成功状态
						cmdPackets.getCmdData()[2] = 0x00;

						// 命令回应包
						RESPackets resPackets = new RESPackets();
						resPackets.setTag(cmdPackets.getCmd());
						resPackets.setAppId(cmdPackets.getAppId());
						resPackets.setDasId(cmdPackets.getDasId());
						resPackets.setRtuId(cmdPackets.getRtuId());
						resPackets.setValue(cmdPackets.getCmdData());
						resPackets.getValue()[2] = 0x00;

						packets.getTag()[0]=0x00;
						packets.getTag()[1]=0x03;
						packets.setValuePackets(resPackets);
						packets.finishFilling();

						session.write(packets);
					}
				}
			}
		});
		IoSession session = future.getSession();

		// 发送上报包

		DASPackets dasPackets = new DASPackets();
		dasPackets.setDasTag(ByteUtils.writeInt2("0001", 0));
		dasPackets.setDasId(ByteUtils.writeInt4("33000001", 0));
		dasPackets.setRtus(new ArrayList<RTUPackets>());

		RTUPackets rtuPackets;
		for (String rtuId : rtuIds) {
			rtuPackets = new RTUPackets();
			rtuPackets.setTag(ByteUtils.writeInt2("0611", 0));
			rtuPackets.setRtuId(ByteUtils.writeInt4(rtuId, 0));
			rtuPackets.setLength(ByteUtils.writeInt2("000a", 0));
			rtuPackets.setValue(new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00,
					0x00, 0x01, 0x00, 0x00, 0x00 });
			dasPackets.getRtus().add(rtuPackets);
		}

		PacketsEntity packets = new PacketsEntity();
		packets.setTag(ByteUtils.writeInt2("0001", 0));
		packets.setIndex(ByteUtils.writeInt2("0000", 0));

		packets.setValuePackets(dasPackets);
		packets.finishFilling();

		session.write(packets);

		session.getCloseFuture().awaitUninterruptibly();
	}
}
