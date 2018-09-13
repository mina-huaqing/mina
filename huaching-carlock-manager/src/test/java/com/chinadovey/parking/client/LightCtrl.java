package com.chinadovey.parking.client;

import junit.framework.Assert;

import org.junit.Test;

import com.chinadovey.parking.webapps.mina.client.CloudClient;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class LightCtrl {

	@Test
	public void test() throws InterruptedException {

		Assert.assertEquals(t(0x64000065, 0x0907, (byte) 0x01, 5), 0x00);
		Thread.sleep(1000);
		Assert.assertEquals(t(0x64000066, 0x0907, (byte) 0x01, 5), 0x00);
		Thread.sleep(1000);
		Assert.assertEquals(t(0x64000067, 0x0907, (byte) 0x01, 5), 0x00);
		Thread.sleep(1000);
		Assert.assertEquals(t(0x64000065, 0x0907, (byte) 0x02, 5), 0x00);
		Thread.sleep(1000);
		Assert.assertEquals(t(0x64000066, 0x0907, (byte) 0x02, 5), 0x00);
		Thread.sleep(1000);
		Assert.assertEquals(t(0x64000067, 0x0907, (byte) 0x02, 5), 0x00);
	}

	public Object t(int rtuId, int slave, byte status, int timeout) {
		try {
			return cmd(rtuId, slave, status, timeout);
		} catch (CMDExecErrorException e) {
			return e.getMessage();
		}
	}

	public byte cmd(int rtuId, int slave, byte status, int timeout)
			throws CMDExecErrorException {
		try {

			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);

			byte[] b = new byte[4];
			ByteUtils.writeInt4(rtuId, b, 0);
			cmd.setRtuId(b);
			b = new byte[3];
			ByteUtils.writeInt2(slave, b, 0);

			b[2] = status;
			cmd.setCmdData(b);

			CloudClient cc = new CloudClient("192.168.1.11:9000");
			RESPackets res = cc.execute(cmd, 1000l * timeout);

			return res.getValue()[2];
		} catch (CMDExecErrorException e) {
			throw e;
		}

	}
}
