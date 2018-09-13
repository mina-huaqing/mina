package com.chinadovey.parking.webapps.mina;

import java.util.Map;

import com.chinadovey.parking.webapps.mina.exception.TagUndefinedException;

public class RTUProcessFactory {
	private Map<String, RTUProcess> processes;

	public RTUProcess getProcess(String tag) throws TagUndefinedException {
		if (processes.containsKey(tag))
			return processes.get(tag);
		throw new TagUndefinedException("RTU tag："+tag + "未定义");
	}

	public void setProcesses(Map<String, RTUProcess> processes) {
		this.processes = processes;
	}
}