package com.chinadovey.parking.webapps.mina;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.data.mongodb.core.MongoOperations;

import com.chinadovey.parking.webapps.mina.protocol.DASAllReturnPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class DasAllReturnProcess {
	
	private static Log logger = LogFactory.getLog(DPSProcess.class);
	
	private String localAddress;
	private MongoOperations mongoOps;
	private CloudNodeIoHandler ioHandler;
	
	public DasAllReturnProcess(String localAddress , MongoOperations mongoOps , CloudNodeIoHandler ioHandler) {
		this.localAddress = localAddress;
		this.mongoOps = mongoOps;
		this.ioHandler = ioHandler;
	}
	
	/**
	 * tag:0x0003H DAS的下行命令反馈包
	 */
	public void p0x0005(IoSession session, PacketsEntity packets) {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x0003H,DAS的下行命令反馈包");
		}

		if(packets.getValuePackets() instanceof DASAllReturnPackets){
			DASAllReturnPackets resPackets = (DASAllReturnPackets) packets.getValuePackets();
			int appId = ByteUtils.makeIntFromByte(resPackets.getAppId());
			IoSession targetSession = ioHandler.getCachedSession(appId,false);
			if (logger.isDebugEnabled()) {//AppId=131655 00000247
				logger.debug("尝试获取命令发送者会话 AppId="+appId+" "+ByteUtils.asHex(resPackets.getAppId()));
			}
			if (targetSession != null) {
				logger.debug("向命令发送者转发命令反馈");
				packets.getTag()[0] = 0x03;
				targetSession.write(packets);
				logger.debug("等待客户端关闭会话...");
//				targetSession.getCloseFuture().awaitUninterruptibly(5000);
//				logger.info("断开命令发送者会话 SESSION=" + targetSession.getId());
			} else {
				logger.debug("命令发送者会话未找到，估计已断开");
			}
		}
		
	}
}
