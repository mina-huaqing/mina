package com.chinadovey.parking.webapps.mina;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.codehaus.jackson.map.ser.std.StdArraySerializers.FloatArraySerializer;
import org.springframework.data.mongodb.core.MongoOperations;

import com.chinadovey.parking.webapps.mina.Heart.KeepAliveMessageFactoryImpl;
import com.chinadovey.parking.webapps.mina.Heart.KeepAliveRequestTimeoutHandlerImpl;
import com.chinadovey.parking.webapps.util.ConfUtils;
public class MinaServer{

	private static final Logger logger = Logger.getLogger(MinaServer.class);

	private IoAcceptor ioAcceptor;
	private MongoOperations mongoOps;
	
	
	public void start() {
		// 2. 启动Socket
		try {
			ioAcceptor.getSessionConfig().setReadBufferSize(1024*1024);
			((SocketSessionConfig) ioAcceptor.getSessionConfig())
					.setReceiveBufferSize(1024*1024);
			/*60秒内没收到网关任何消息，则向其发送心跳包，然后能收到网关消息则网关在线。
			 * 超过requestTimeout规定的时间还没收到网关响应心跳的消息，则再发几次，大概三次之后，仍然无响应，则关闭session会话，并记录该网关离线日志。*/
			ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 60);
			if (ConfUtils.getUrlByName("heartEnable").equals("1")) {
				KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl(mongoOps);
				KeepAliveRequestTimeoutHandler heartBeatHandler = new  KeepAliveRequestTimeoutHandlerImpl();
				KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
						IdleStatus.BOTH_IDLE,heartBeatHandler);
				heartBeat.setForwardEvent(true);//设置是否到下一个filter
				heartBeat.setRequestInterval(Integer.parseInt(ConfUtils.getUrlByName("requestInterval")));//设置心跳频率
				heartBeat.setRequestTimeout(Integer.parseInt(ConfUtils.getUrlByName("requestTimeout")));
				ioAcceptor.getFilterChain().addLast("heartbeat", heartBeat);
			}
			// 端口重用
			ioAcceptor.bind();
			logger.info("mina服务启动了！");
		} catch (IOException e) {
			logger.error("mina 服务启动失败！",e);
			destroy(e);
			return;
		}

	}
	public void destroy(Exception e) {
		if (e != null)
			logger.error(e.getMessage(), e);
		destroy();
	}

	public void destroy() {
		if (ioAcceptor != null) {
			ioAcceptor.unbind();
			ioAcceptor.dispose();
		}
	}
	public void setIoAcceptor(IoAcceptor ioAcceptor) {
		this.ioAcceptor = ioAcceptor;
	}
	public void setMongoOps(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}
	
	
	

}

