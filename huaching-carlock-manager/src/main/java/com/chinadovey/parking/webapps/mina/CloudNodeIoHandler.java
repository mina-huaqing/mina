package com.chinadovey.parking.webapps.mina;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.chinadovey.parking.webapps.biz.CarLockBiz;
import com.chinadovey.parking.webapps.biz.GatewayLogBiz;
import com.chinadovey.parking.webapps.mina.pojo.DASRealTime;
import com.chinadovey.parking.webapps.mina.protocol.PacketsEntity;
import com.chinadovey.parking.webapps.pojo.GatewayLog;
import com.chinadovey.parking.webapps.util.ByteUtils;

public class CloudNodeIoHandler extends IoHandlerAdapter {

	private DPSProcess dpsProcess;

	private String localAddress;
	private MongoOperations mongoOps;
	private static GatewayLogBiz gatewayLogBiz;
	private static CarLockBiz carLockBiz;
	

	public static GatewayLogBiz getGatewayLogBiz() {
		return gatewayLogBiz;
	}

	public static void setGatewayLogBiz(GatewayLogBiz gatewayLogBiz) {
		CloudNodeIoHandler.gatewayLogBiz = gatewayLogBiz;
	}

	public static CarLockBiz getCarLockBiz() {
		return carLockBiz;
	}

	public static void setCarLockBiz(CarLockBiz carLockBiz) {
		CloudNodeIoHandler.carLockBiz = carLockBiz;
	}

	private final Map<Long, IoSession> _sessions;
	

	public CloudNodeIoHandler() {
		this._sessions = new ConcurrentHashMap<Long, IoSession>();
		this._waiting_k2s = new ConcurrentHashMap<Integer, IoSession>();
		this._waiting_s2k = new ConcurrentHashMap<IoSession, Integer>();
	}

	private final Map<Integer, IoSession> _waiting_k2s;
	private final Map<IoSession, Integer> _waiting_s2k;
	private int _waiting_cursor = 0;
	private Lock lock = new ReentrantLock();
	
	public int cacheSession(IoSession session){ 
		if(_waiting_k2s.size()==Integer.MAX_VALUE){
			//理论上这里不会执行，因为一台服务器根本不能支持 Integer.MAX_VALUE 个会话
			throw new RuntimeException("连接数超出最大范围");
		}
		if(_waiting_s2k.containsKey(session)){
			return _waiting_s2k.get(session);
		}
		
		lock.lock();
		while(_waiting_k2s.containsKey(_waiting_cursor)){
			_waiting_cursor++;
		}
		_waiting_k2s.put(_waiting_cursor, session);
		_waiting_s2k.put(session, _waiting_cursor);
		int k = _waiting_cursor;
		lock.unlock();
		
		return k;
	}
	
	public IoSession getCachedSession(int k){
		if(_waiting_k2s.containsKey(k)){
			IoSession session = _waiting_k2s.remove(k);
			_waiting_s2k.remove(session);
			return session;
		}
		return null;
	}
	
	public IoSession getCachedSession(int k,boolean remove){
		if(remove)
			return getCachedSession(k);
		if(_waiting_k2s.containsKey(k)){
			return _waiting_k2s.get(k);
		}
		return null;
	}

	@Override
	public void sessionCreated(IoSession session) {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		_sessions.put(session.getId(), session);
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Query query1 = Query.query(
				Criteria.where("sessionId").is(session.getId()));
		DASRealTime dasRealTime = mongoOps.findOne(query1, DASRealTime.class);
		if (dasRealTime != null ) {
			String dasId =  ByteUtils.asHex(dasRealTime.getDasIdBytes());
			GatewayLog gatewayLog = new GatewayLog();
			gatewayLog.setDasId(dasId);
			gatewayLog.setStatus(1);
			gatewayLog.setTime(new Date());
			gatewayLogBiz.save(gatewayLog);
		}
		
		_sessions.remove(session.getId());
		
		if(_waiting_s2k.containsKey(session)){
			int c = _waiting_s2k.get(session);
			_waiting_k2s.remove(c);
			_waiting_s2k.remove(session);
		}
		// 清楚mongo中的连接信息缓存
		Query query = Query.query(
				Criteria.where("sessionId").is(session.getId())
				.and("slaveAddress").is(localAddress));
		mongoOps.remove(query, DASRealTime.class);

	}
	

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof PacketsEntity) {
			dpsProcess.messageProcess(session, (PacketsEntity) message);
		}
	}

	public void setDpsProcess(DPSProcess tranHandler) {
		this.dpsProcess = tranHandler;
		this.dpsProcess.setIoHandler(this);
	}

	public IoSession getSession(Long id) {
		return _sessions.get(id);
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public void setMongoOps(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
		// 清楚mongo中的连接信息缓存
		Query query = Query.query(
				Criteria.where("slaveAddress").is(localAddress));
		mongoOps.remove(query, DASRealTime.class);
}
}