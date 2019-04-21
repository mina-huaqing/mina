package com.chinadovey.parking.webapps.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.chinadovey.parking.webapps.mina.Heart.KeepAliveMessageFactoryImpl;
import com.chinadovey.parking.webapps.mina.Heart.KeepAliveRequestTimeoutHandlerImpl;

public class TestServer {
	public static void main(String[] args) throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",
        		new ProtocolCodecFilter((ProtocolCodecFactory) new TextLineCodecFactory(Charset.forName("UTF-8")))); // 指定编码过滤器
        acceptor.setHandler(new IoHandlerAdapter() {

			@Override
			public void messageReceived(IoSession session, Object message) throws Exception {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println("服务端收到："+df.format(new Date())+",,"+message);
				session.write("我是服务端,,,"+df.format(new Date()));
			}

			@Override
			public void sessionCreated(IoSession session) throws Exception {
				System.out.println("sessionCreated:"+session.getId());
			}

			@Override
			public void sessionClosed(IoSession session) throws Exception {
				System.out.println("sessionClosed:"+session.getId());
			}

			@Override
			public void sessionOpened(IoSession session) throws Exception {
				System.out.println("sessionOpened:"+session.getId());
				
			}
			
        	
        }); // 指定业务逻辑处理器
        
        acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 60);//60s，没有读到该session发来消息，认为其空闲。
			KeepAliveRequestTimeoutHandler heartBeatHandler = new  KeepAliveRequestTimeoutHandlerImpl();
			KeepAliveFilter heartBeat = new KeepAliveFilter(new KeepAliveMessageFactory() {
				
				@Override
				public boolean isResponse(IoSession session, Object message) {
					System.out.println("isResponse");
					return false;
				}
				
				@Override
				public boolean isRequest(IoSession session, Object message) {
					System.out.println("isRequest请求心跳包信息:"+message);
		            if (message.equals("10")) {
		            	System.out.println("isRequest请求心跳包信息:true");
		                return true;
		                }
					return false;
				}
				
				@Override
				public Object getResponse(IoSession session, Object request) {
					System.out.println("getResponse响应消息");
					return "11";
				}
				
				@Override
				public Object getRequest(IoSession session) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("getRequest,"+df.format(new Date()));
					return "10"; //这是，向客户端，发送的心跳包
				}
			},
					IdleStatus.BOTH_IDLE,new KeepAliveRequestTimeoutHandler() {
						
						@Override
						public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
							try {
								//没收到数据开始起，30秒钟后，认为该session空闲。 然后开始发送心跳包？
								session.getConfig().setIdleTime(IdleStatus.READER_IDLE, 30);
								System.err.println("空闲时间，计数："+session.getIdleCount(IdleStatus.READER_IDLE));
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								System.out.println("超时处理："+df.format(new Date()));
								if (session.getReaderIdleCount() >= 4) {
									System.out.println("关闭session："+df.format(new Date()));
									session.close(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					});
			heartBeat.setForwardEvent(true);//设置是否到下一个filter
			/*
			 * （1）KeepAvlieMessageFactory:   该实例引用用于判断接受与发送的包是否是心跳包，以及心跳请求包的实现 
				（2）IdleStatus:      该过滤器所关注的空闲状态，默认认为读取空闲。 即当读取通道空闲的时候发送心跳包 
				（3）KeepAliveRequestTimeoutHandler： 心跳包请求后超时无反馈情况下的处理机制  默认为CLOSE  即关闭连接 
			 */
			heartBeat.setRequestTimeout(30); // 没收到任何数据5s后，执行上面的超时处理方法？
			heartBeat.setRequestInterval(30); // 设置心跳频率，每隔10s发送一次心跳包？
			acceptor.getFilterChain().addLast("heartbeat", heartBeat);
        
        acceptor.setDefaultLocalAddress(new InetSocketAddress(9999)); // 设置端口号
        acceptor.bind();// 启动监听}
		System.out.println("服务端大概启动了");
	}
}
