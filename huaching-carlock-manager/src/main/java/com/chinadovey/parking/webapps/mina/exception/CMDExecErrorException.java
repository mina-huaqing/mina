package com.chinadovey.parking.webapps.mina.exception;

import com.chinadovey.parking.webapps.mina.protocol.ERRPackets;

public class CMDExecErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 688921888493191559L;

	private ERRPackets errPackets;

	public CMDExecErrorException(String message, ERRPackets errPackets) {
		super(message);
		this.errPackets = errPackets;
	}

	public ERRPackets getErrPackets() {
		return errPackets;
	}
}
