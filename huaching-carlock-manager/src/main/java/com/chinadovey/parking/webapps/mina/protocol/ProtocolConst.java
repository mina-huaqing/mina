package com.chinadovey.parking.webapps.mina.protocol;

public interface ProtocolConst {
	
	public static final byte[] CMD_OPEN_OR_CLOSE = new byte[] { 0x00, 0x01 };
	
	public static final byte [] CMD_ALL = new byte[] { 0x00, 0x02 };
	
	public static final byte [] CMD_CONF_DAS_ADD = new byte[] { 0x00, 0x03 };
	
	public static final byte [] CMD_CONF_DAS_DEL = new byte[] { 0x00, 0x04 };
	
	public static final byte [] CMD_CONF_DAS_PORT = new byte[] { 0x00, 0x05 };
	
	public static final byte [] CMD_SOUND_CONTROL = new byte[] { 0x00, 0x06 };
	
	public static final byte [] CMD_LIGHT_CONTROL = new byte[] { 0x00, 0x07 };
	
	public static final byte [] CMD_SOUND_LIGHT_CONTROL = new byte[] { 0x00, 0x08 };
	
	public static final byte [] CMD_CAR_STATE_UPDATE = new byte[] { 0x00, 0x09 };
	
	public static final byte [] CMD_DAS_REBOOT = new byte[] { 0x10, 0x00 };
	
	
	
	
}
