package com.chinadovey.parking.webapps.util;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class TestClient {
	public static void main(String[] args) {
		// 创建客户端连接器.
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast( "logger", new LoggingFilter() );
        connector.getFilterChain().addLast( "codec", 
        		new ProtocolCodecFilter( (ProtocolCodecFactory) new TextLineCodecFactory( Charset.forName( "UTF-8" ))));//设置编码过滤器
        //connector.setConnectTimeoutCheckInterval(3);
        connector.setHandler(new IoHandlerAdapter() {

			@Override
			public void messageReceived(IoSession session, Object message) throws Exception {
				System.out.println("客户端收到："+message);
			}
        	
        });//设置事件处理器
        ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 9999));//建立连接
        cf.awaitUninterruptibly();//等待连接创建完成
        cf.getSession().write("hello");//发送消息
        cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开
        connector.dispose();
	}
}
