package com.chinadovey.parking.webapps.mina.temp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.chinadovey.parking.webapps.mina.pojo.Equipment0710;

public class CarLockWebDataOpt {

	private Connection conn;

	public void conn() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager
				.getConnection(
						"jdbc:mysql://huaching.mysql.rds.aliyuncs.com:3306/pp_parking?characterEncoding=UTF-8",
						 "pp_parking", "pp52725299");
		conn.setAutoCommit(false);
	}
	
	public void update(Equipment0710 equi) throws SQLException {
		try {
			int equiId = Integer.parseInt(equi.getRtuId()+equi.getEquiId(), 16);
			System.err.println(equiId);
			String sql = "update parking_car_lock set voltage='" + equi.getVoltage() + "' where equi_id='"+equiId+"'";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			 e.printStackTrace();
		     conn.close();
		}
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
