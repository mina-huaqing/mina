package com.chinadovey.parking.webapps.mina.client;

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

import com.chinadovey.parking.webapps.mina.DPSProtocolCodecFactory;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.ERRPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.mina.protocol.ValuePackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class ParkCloudClient {
	private Log		logger	= LogFactory.getLog(CloudClient.class);

	private String				cloudAddress;
	private NioSocketConnector	connector;
	private ConnectFuture		future;

	public ParkCloudClient(String cloudAddress) {
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

		// 2.初始化服务器连接（连接mina服务器，mina服务器收到消息之后，再根据协议内容，决定发送到哪个外部程序。）
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

		// 3.发送命令（发送命名到mina服务器）
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
	
	public RESPackets execute(byte[] tag, ValuePackets valuePacket, long timeoutMillis)
			throws CMDExecErrorException {
        short cmdIndex = (short)0;
		// 1.准备数据

		final PacketsEntity[] _cache = new PacketsEntity[1];
		PacketsEntity packetsEntity = new PacketsEntity();
		packetsEntity.setTag(tag);
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
