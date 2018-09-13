package com.chinadovey.parking.webapps.mina;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.chinadovey.parking.webapps.mina.pojo.DasMacDO;
import com.chinadovey.parking.webapps.mina.protocol.DASReportMacPackets;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.util.ByteUtils;
import com.chinadovey.parking.webapps.util.IpConvertUtil;

public class DpsReportMacProcess {
	
	private static Log logger = LogFactory.getLog(DPSProcess.class);
	
	private MongoOperations mongoOps;
	
	
	public DpsReportMacProcess(MongoOperations mongoOps ) {
		this.mongoOps = mongoOps;
	}
	
	/**
	 * tag:0x0006H DAS上报MAC数据包
	 * @throws SQLException 
	 */
	public void messageHandle(IoSession session, PacketsEntity packets) throws SQLException {
		if (logger.isDebugEnabled()) {
			logger.debug("TAG:0x0006H,DAS上报MAC数据包");
		}
		// 解析DAS数据包
		DASReportMacPackets dasMacPackets = (DASReportMacPackets) packets.getValuePackets();
		try {
			DasMacDO dasMacDO = new DasMacDO();
			dasMacDO.setDasId(ByteUtils.asHex(dasMacPackets.getDasId()));
			dasMacDO.setDasIp(IpConvertUtil.longToIp(Long.parseLong(ByteUtils.asHex(dasMacPackets.getDasIp()),16)));
			dasMacDO.setDasMac(ByteUtils.asHex(dasMacPackets.getDasMac()));
			
			Query query = Query.query(
					Criteria.where("dasId").is(dasMacDO.getDasId()));
			Update update = Update
					.update("dasIp", dasMacDO.getDasIp())
					.set("dasMac", dasMacDO.getDasMac());

			mongoOps.upsert(query, update, DasMacDO.class);
			
			
			

		} catch (Exception e) {
			logger.error(e.getMessage() + "\n DasMacPackets:\n" + dasMacPackets, e);
			e.printStackTrace();
		}
	}
}
