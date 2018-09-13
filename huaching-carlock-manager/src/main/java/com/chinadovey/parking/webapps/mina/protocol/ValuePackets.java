package com.chinadovey.parking.webapps.mina.protocol;

import com.chinadovey.parking.webapps.mina.exception.ProtocolParseErrorException;

public interface ValuePackets {

	public void parsed(byte[] buf) throws ProtocolParseErrorException;

	public byte[] getBytes();
}
