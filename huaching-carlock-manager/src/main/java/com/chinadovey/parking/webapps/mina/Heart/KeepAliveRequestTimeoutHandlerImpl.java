package com.chinadovey.parking.webapps.mina.Heart;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

public class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler{

	@Override
	public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
		try {
			session.getConfig().setIdleTime(IdleStatus.READER_IDLE, 30);
			System.err.println(session.getIdleCount(IdleStatus.READER_IDLE));
			if (session.getReaderIdleCount() >= 4) {
				session.close(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
