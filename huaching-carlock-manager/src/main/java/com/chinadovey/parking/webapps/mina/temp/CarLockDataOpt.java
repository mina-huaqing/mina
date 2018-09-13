package com.chinadovey.parking.webapps.mina.temp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.apache.mina.proxy.utils.ByteUtilities;

import com.chinadovey.parking.webapps.mina.pojo.Equipment0710;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0713;
import com.chinadovey.parking.webapps.util.ConfUtils;

public class CarLockDataOpt {
	Logger logger = Logger.getLogger(getClass());
	private Connection conn;

	public void conn() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String url = ConfUtils.getJdbcAddress();
		String username = ConfUtils.getJdbcUsername();
		String pwd = ConfUtils.getJdbcPwd();
		//conn = DriverManager.getConnection("jdbc:mysql://huaching.mysql.rds.aliyuncs.com:3306/pp_parking_data?characterEncoding=UTF-8","pp_parking", "pp52725299");
		conn = DriverManager.getConnection(url,username, pwd);
		conn.setAutoCommit(false);
	}

	public void insert(Equipment0710 data) throws SQLException {
		try {
			String sql = "insert into parking_carlock_data values(null,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, data.getRtuId() + data.getEquiId());
			pstmt.setInt(2, data.getOpenState());
			pstmt.setInt(3, data.getCarStatus());
			pstmt.setFloat(4, data.getVoltage());
			pstmt.setInt(5, data.getEquiState());
			pstmt.setInt(6, data.getCycle());
			pstmt.setInt(7, data.getHx());
			pstmt.setInt(8, data.getLx());
			pstmt.setInt(9, data.getHy());
			pstmt.setInt(10, data.getLy());
			pstmt.setInt(11, data.getHz());
			pstmt.setInt(12, data.getLz());
			pstmt.setInt(13, data.getSource());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void insert(Equipment0713 data) {
		try {
			String sql = "insert into parking_carlock_data values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, data.getRtuId() + data.getEquiId());
			pstmt.setInt(2, data.getOpenState());
			pstmt.setInt(3, data.getCarStatus()==null?0:data.getCarStatus());
			pstmt.setFloat(4, data.getVoltage()==null?0:data.getVoltage());
			pstmt.setInt(5, data.getEquiState()==null?0:data.getEquiState());
			pstmt.setInt(6, data.getCycle()==null?0:data.getCycle());
			pstmt.setInt(7, data.getHx()==null?0:data.getHx());
			pstmt.setInt(8, data.getLx()==null?0:data.getLx());
			pstmt.setInt(9, data.getHy()==null?0:data.getHy());
			pstmt.setInt(10, data.getLy()==null?0:data.getLy());
			pstmt.setInt(11, data.getHz()==null?0:data.getHz());
			pstmt.setInt(12, data.getLz()==null?0:data.getLz());
			//pstmt.setInt(13, data.getSource());
			
			//存储网关上报的时间，lz
			byte[] collectTime = data.getCollectTime();
			int collectTime2 = ByteUtilities.makeIntFromByte4(collectTime);
			Timestamp tt = new Timestamp(Long.parseLong(collectTime2+"000"));
			//pstmt.setDate(13, d);
			pstmt.setTimestamp(13, tt);
			logger.error("上报时间:"+collectTime2+"000");
			logger.error("上报时间:"+tt);
			
			pstmt.setInt(14, 10);//主动上报
			//pstmt.setInt(13, 10);//主动上报
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("保存主动上报到数据库出错：", e);
		}
	}
	
	public void insertlog(Equipment0710 data) throws SQLException {
		try {
			String sql = "insert into parking_carlock_log values(null,?,?,now(),?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, data.getRtuId() + data.getEquiId());
			pstmt.setInt(2, data.getEquiState());
			pstmt.setString(3, "");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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
