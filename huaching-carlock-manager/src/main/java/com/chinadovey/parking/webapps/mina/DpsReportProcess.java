package com.chinadovey.parking.webapps.mina;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.mina.exception.RTUProcessException;
import com.chinadovey.parking.webapps.mina.exception.TagUndefinedException;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.pojo.EquimentRealTime;
import com.chinadovey.parking.webapps.mina.pojo.Equipment;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0710;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0713;
import com.chinadovey.parking.webapps.mina.pojo.RTURealTime;
import com.chinadovey.parking.webapps.mina.protocol.DASPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.mina.protocol.RTUPackets;
import com.chinadovey.parking.webapps.mina.temp.CarLockDataOpt;
import com.chinadovey.parking.webapps.pojo.Carlock;
import com.chinadovey.parking.webapps.util.ByteUtils;
import com.chinadovey.parking.webapps.util.ConfUtils;

public class DpsReportProcess {
	
	private static Log logger = LogFactory.getLog(DPSProcess.class);
	
	private String localAddress;
	private MongoOperations mongoOps;
	private CloudNodeIoHandler ioHandler;
	private RTUProcessFactory rtuProcessFactory;
	private CarLockBiz carLockBiz;
	
	public DpsReportProcess(String localAddress , MongoOperations mongoOps , CloudNodeIoHandler ioHandler , RTUProcessFactory rtuProcessFactory,CarLockBiz carLockBiz) {
		this.localAddress = localAddress;
		this.mongoOps = mongoOps;
		this.ioHandler = ioHandler;
		this.rtuProcessFactory = rtuProcessFactory;
		this.carLockBiz = carLockBiz;
	}
	
	/**
	 * tag:0x0001H DAS上报数据包
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void p0x0001(IoSession session, PacketsEntity packets) throws SQLException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x0001H,DAS上报数据包");
		}
		DASPackets dasPackets = (DASPackets) packets.getValuePackets();
		try {
			// 刷新 mongoDB 中 DAS socket 的长连接信息
			if (logger.isDebugEnabled()) {
				logger.debug(
					"刷新 mongoDB 中 DAS socket 的长连接信息：" + localAddress + "@" + session.getId());
			}
			DASRealTime dasRelaTime = new DASRealTime();
			dasRelaTime.setId(ByteUtils.asHex(dasPackets.getDasId()));
			dasRelaTime.setDasIdBytes(dasPackets.getDasId());
			dasRelaTime.setSessionId(session.getId());
			dasRelaTime.setSlaveAddress(localAddress);
			
			Query query = Query.query(
					Criteria.where("id").is(dasRelaTime.getId()));
			Update update = Update
					.update("slaveAddress", dasRelaTime.getSlaveAddress())
					.set("sessionId", dasRelaTime.getSessionId())
					.set("dasIdBytes", dasRelaTime.getDasIdBytes());

			mongoOps.upsert(query, update, DASRealTime.class);
			
			List<Object> objectsToSave = new ArrayList<Object>();
			for (RTUPackets rtuParkets : dasPackets.getRtus()) { //根据协议，网关上报的每个包中可以包含多个设备（地锁）的包
				
				// RTU 数据处理 （地锁设备包的tag字节目前有0710、0713两种，rtuProcess也有两个子类和其对应。2018年6月28日）
				RTUProcess cnp = rtuProcessFactory.getProcess(ByteUtilities.asHex(rtuParkets.getTag()));
				
				// 处理接收到的数据
				List<? extends Equipment> list = null;
				try {
					//logger.error("rtuParkets:"+rtuParkets.toString());
					list = cnp.process(rtuParkets);
				} catch (RTUProcessException e) {
					e.printStackTrace();
				}
				
				if (list != null && list.size() > 0){
					objectsToSave.addAll((Collection<?>) list);
				}
			}

			CarLockDataOpt parkOpt = null;// 插入车位锁数据
			try {
				for (Object obj : objectsToSave) {
					/**
					 * 定时上报
					 */
					if (obj instanceof Equipment0710) {
						if (parkOpt == null) {
							parkOpt = new CarLockDataOpt();
							parkOpt.conn();
						}
						Equipment0710 equi = (Equipment0710) obj;
						if (ConfUtils.getUrlByName("ispush").equals("1")) {
							new PushInfoThread(equi,carLockBiz).start();//调用http接口发送通知
						}
						parkOpt.insert((Equipment0710) obj);//保存普通上报
						
					}else if(obj instanceof Equipment0713){
						/**
						 * 下发控制命令之后，主动上报
						 */
						if (parkOpt == null) {
							parkOpt = new CarLockDataOpt();
							parkOpt.conn();
						}
						Equipment0713 equipment0713 = (Equipment0713) obj;
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String slaveId = equipment0713.getRtuId()+equipment0713.getEquiId();
						logger.error(df.format(new Date())+"锁编号："+slaveId+"======================主动上报");
						//1、保存
						parkOpt.insert(equipment0713);
						//2、推送
						if (ConfUtils.getUrlByName("ispush").equals("1")) {
							PushActiveInfo push = new PushActiveInfo(equipment0713, carLockBiz);
							push.push();
						}
					}
				}
				if (parkOpt != null)
					parkOpt.commit();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				if (parkOpt != null)
					parkOpt.rollback();
			}
			
			
			
			
			
		} catch (TagUndefinedException e) {
			logger.error(e.getMessage() + "\n DasPackets:\n" + dasPackets, e);
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println(0x16);
	}

}
