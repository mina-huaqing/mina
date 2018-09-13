package com.chinadovey.parking.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.chinadovey.parking.webapps.mina.DPSProtocolCodecFactory;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.DASPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.mina.protocol.RTUPackets;
import com.chinadovey.parking.webapps.mina.protocol.ValuePackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class ParkSimulator {

	private static final Log	logger		= LogFactory.getLog(ParkSimulator.class);

	private String				host		= "121.40.52.180:9005";
	private int					dasId		= 0x01020304;
	private int					rtuId		= 0x05060708;
	private int					equId		= 0x00000A0B;

	private CloudClient			cc;

	private byte[]				value		= new byte[18];
	/**
	 * 车位锁开启状态<br>
	 * 00H 车位锁打开 挡臂处于平躺状态<br>
	 * 01H 车位锁关闭 挡臂处于竖起状态 <br>
	 * 02H 正在打开 挡臂正在倒下 <br>
	 * 03H 正在关闭 挡臂正在竖起 <br>
	 * 0EH 未知状态
	 */
	private Integer				openState	= 0x0E;
	/**
	 * 车位锁车辆状态<br>
	 * 00H 车位上有车<br>
	 * 10H 车位上无车<br>
	 * 20H 预留<br>
	 * 30H 预留<br>
	 * E0H 未知状态
	 */
	private Integer				carState	= 0xE0;

	public ParkSimulator(String sim) {
		this();
		String args[] = sim.split("[?=&]");
		if (args.length < 6)
			return;
		host = args[0];
		dasId = Integer.parseInt(args[2].substring(2), 16);
		rtuId = Integer.parseInt(args[4].substring(2), 16);
		equId = Integer.parseInt(args[6].substring(2), 16);
	}

	public ParkSimulator() {

		ByteUtils.writeInt2(equId, value, 0);
		ByteUtils.writeByte(0x00000000, value, 2);
		ByteUtils.writeInt4(0x00000001, value, 3);
		ByteUtils.writeByte(0x00000000, value, 7);
		ByteUtils.writeInt2(0x000000E0, value, 8);
		ByteUtils.writeLong8(0x00000000, value, 10);
	}

	public void connectToPower() {
		cc = new CloudClient(host);
		cc.init(new IoHandlerAdapter() {
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				logger.info(message);
				if (message instanceof PacketsEntity) {
					ValuePackets valuePackets = ((PacketsEntity) message).getValuePackets();
					CMDPackets cmd = (CMDPackets) valuePackets;
					listen(session, cmd);
				}
			}
		});

		active();
	}

	private void changeOpenState(int openState) {
		this.openState = openState;
		ByteUtils.writeByte(carState | openState, value, 2);
	}

	private void changeCarState(int carState) {
		this.carState = carState;
		ByteUtils.writeByte(carState | openState, value, 2);
	}

	private void doCallback(IoSession session, CMDPackets cmd) {
		RESPackets res = new RESPackets();
		res.setTag(cmd.getCmd());
		res.setAppId(cmd.getAppId());
		res.setDasId(cmd.getDasId());
		res.setRtuId(cmd.getRtuId());
		res.setValue(value);

		PacketsEntity packetsEntity = new PacketsEntity();
		packetsEntity.setTag(new byte[] { 0x00, 0x03 });
		packetsEntity.setIndex(new byte[2]);
		ByteUtils.writeInt2(0, packetsEntity.getIndex(), 0);
		packetsEntity.setValuePackets(res);
		packetsEntity.finishFilling();

		try {
			if (session != null)
				cc.execute(session, packetsEntity);
			else
				cc.execute(packetsEntity);
		} catch (CMDExecErrorException e) {
			e.printStackTrace();
		}
	}

	public void open(IoSession session, CMDPackets cmd) {
		if (openState == 0x01 || openState == 0x03) {
			changeOpenState(0x02);
		}
		doCallback(session, cmd);
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
				logger.info("车位锁正在开启..." + i + "/5...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		onOpened(session, cmd);
	}

	public void close(final IoSession session, final CMDPackets cmd) {
//		onCarLease(session, cmd);
		if (openState == 0x00 || openState == 0x02) {
			changeOpenState(0x03);
		}
		doCallback(session, cmd);
		
		new Thread(){
			public void run(){
				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(1000);
						logger.info("车位锁正在关闭..." + i + "/5...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				 onClosed(session, cmd);
			}
		}.start();
	}

	public void onOpened(IoSession session, CMDPackets cmd) {
		changeOpenState(0x00);
		doCallback(session, cmd);
		logger.info("车位锁已开启...");
//		onCarEnter(session, cmd);
	}

	public void onClosed(IoSession session, CMDPackets cmd) {
		changeOpenState(0x01);
		doCallback(session, cmd);
		logger.info("车位锁已关闭...");
	}

	public void onCarEnter(IoSession session, CMDPackets cmd) {
		changeCarState(0x00);
		doCallback(null, cmd);
		logger.info("车辆进入...");
	}

	public void onCarLease(IoSession session, CMDPackets cmd) {
		changeCarState(0x10);
		doCallback(null, cmd);
		logger.info("车辆离开...");
	}

	// 开关车位锁
	public void listen(IoSession session, CMDPackets cmd) {
		logger.info(cmd);
		if (ByteUtils.makeIntFromByte(cmd.getCmd()) == 0x0001) {
			byte c = cmd.getCmdData()[2];
			switch (c) {
			case 1:
				open(session, cmd);
				break;
			case 2:
				close(session, cmd);
				break;
			default:
				break;
			}
		}
	}

	public void active() {
		new Thread() {
			public void run() {
				for (short i = 0; i < 1; i++) {
					try {

						final RTUPackets rtu = new RTUPackets();
						rtu.setTag(ByteUtils.writeInt2(0x0710, 0));
						rtu.setRtuId(ByteUtils.writeInt4(rtuId, 0));
						rtu.setValue(value);
						rtu.setLength(ByteUtils.writeInt2(value.length, 0));

						final DASPackets das = new DASPackets();
						das.setDasTag(ByteUtils.writeInt2(0x0001, 0));
						das.setDasId(ByteUtils.writeInt4(dasId, 0));
						das.setRtus(new ArrayList<RTUPackets>());
						das.getRtus().add(rtu);

						PacketsEntity packetsEntity = new PacketsEntity();
						packetsEntity.setTag(new byte[] { 0x00, 0x01 });
						packetsEntity.setIndex(new byte[2]);
						ByteUtils.writeInt2(i, packetsEntity.getIndex(), 0);
						packetsEntity.setValuePackets(das);
						packetsEntity.finishFilling();

						cc.execute(packetsEntity);
					} catch (CMDExecErrorException e) {

					} finally {
						try {
							Thread.sleep(1000 * 60);
						} catch (InterruptedException e) {}
					}
				}
			}
		}.start();
	}

	static class CloudClient {

		private String				cloudAddress;
		private NioSocketConnector	connector;
		private ConnectFuture		future;

		public CloudClient(String cloudAddress) {
			this.cloudAddress = cloudAddress;
		}

		public void init(IoHandlerAdapter ioHandler) {
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

		public void dispose() {
			connector.dispose();
		}

		public void execute(PacketsEntity packetsEntity)
				throws CMDExecErrorException {
			IoSession session = future.getSession();
			session.write(packetsEntity);
		}

		public void execute(IoSession session, PacketsEntity packetsEntity)
				throws CMDExecErrorException {
			session.write(packetsEntity);
		}
	}

	public static void main(String[] args) {
		new ParkSimulator("121.40.52.180:9005?dasId=0x01020304&rtuId=0x05060708&equId=0x00000A0B").connectToPower();
	}
}
