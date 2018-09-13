package com.chinadovey.parking.webapps.mina.temp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.mina.proxy.utils.ByteUtilities;

import com.chinadovey.parking.webapps.mina.pojo.Equipment0810;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class SensorDataOpt {

	private Connection conn;

	public void conn() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager
				.getConnection(
						"jdbc:mysql://112.124.9.133:3306/arg_sensor?characterEncoding=UTF-8",
						"power", "power");
		conn.setAutoCommit(false);
	}

	public void insert(Equipment0810 data) throws Exception {
		String sql = "insert into sensor_data values(null,?,?,?,?,?,now(),now())";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, ByteUtilities.makeIntFromByte4(data.getRtuIdbytes()));
		
		byte[] b = Arrays.copyOfRange(data.getRtuIdbytes(), 0, 6);
		System.arraycopy(data.getEquiIdbytes(), 0, b, 4, 2);
		
		pstmt.setLong(2, ByteUtils.makeLongFromByte(b, 6));
		pstmt.setInt(3, data.getType());
		pstmt.setObject(4, data.getValue());
		pstmt.setObject(5, data.getStatus());
		pstmt.executeUpdate();
	}

	public void commit() {
		try {
			if (conn != null)
				conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void rollback() {
		try {
			if (conn != null)
				conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
