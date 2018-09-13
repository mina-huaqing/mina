package com.chinadovey.parking.webapps.mina.temp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.chinadovey.parking.webapps.mina.pojo.Equipment;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0710;

public class DBHelper {
	
	private  Connection getConn() {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://112.124.9.133:3306/pp_parking";
	    String username = "power";
	    String password = "power";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	public void update(List<? extends Equipment> list) throws SQLException{
		List<Equipment0710> equiList = (List<Equipment0710>) list;
		for(Equipment0710 equi : equiList){
			System.out.println(equi.getEquiId()+"==================="+equi.getVoltage());
			update(equi);
		}
	}

	
	
	private  int update(Equipment0710 equi) throws SQLException {
	    Connection conn = getConn();
	    int i = 0;
	    String rtuId = equi.getRtuId();
	    String equiId = equi.getEquiId();
	    String equi_id = rtuId +  equiId;
	    int _equiId = Integer.parseInt(equi_id, 16);
	    System.err.println(_equiId);
	    String sql = "update parking_car_lock set voltage='" + equi.getVoltage() + "' where equi_id='"+_equiId+"'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        System.out.println("resutl: " + i);
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        conn.close();
	    }
	    return i;
	}
}

