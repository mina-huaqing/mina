package com.chinadovey.parking.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.chinadovey.parking.webapps.mina.client.CloudClient;
import com.chinadovey.parking.webapps.mina.exception.CMDExecErrorException;
import com.chinadovey.parking.webapps.mina.protocol.CMDPackets;
import com.chinadovey.parking.webapps.mina.protocol.ProtocolConst;
import com.chinadovey.parking.webapps.mina.protocol.RESPackets;

public class CloudClientTest {

	@Test
	public void test1() {
		test((byte) 1);
	}

	public void test2() {
		int i = 0;
		while (true) {
			test((byte) ((i++ % 2 == 0) ? 0x01 : 0x02));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void test(byte _status) {
		try {

			CMDPackets cmd = new CMDPackets();
			cmd.setCmd(ProtocolConst.CMD_OPEN_OR_CLOSE);
			cmd.setRtuId(new byte[] { 0x64, 0x00, 0x00, 0x67 }); //65,66,67
			cmd.setCmdData(new byte[] {
			/* SLAVE */0x09, 0x07,
			/* STATUS */_status });

			// 2.调用接口
			CloudClient cc = new CloudClient("192.168.1.11:9000");
			RESPackets res = cc.execute(cmd, 1000l * 5);

			System.out.println("正确");
			System.out.println(res.getValue()[2]);

		} catch (CMDExecErrorException e) {
			System.out.println("错误");
			e.printStackTrace();
		}
	}
	
	@Test
	public  String md5(String rawPass) {
        byte[] hash;
        String saltedPass = mergePasswordAndSalt("loginName=18507152881^passWord=123456^timeStamp=1481608008^", "CHINAPOWER", false);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            hash = messageDigest.digest(saltedPass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        System.err.println(hex.toString());
        return hex.toString();
    }

	public  String mergePasswordAndSalt(String password, Object salt, boolean strict) {
		if (password == null) {
			password = "";
		}
		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
			}
		}
		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}
}
