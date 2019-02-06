package edu.pitt.lrdc.cs.revision.statistics;

import java.util.ArrayList;
import java.util.List;

public class ConfirmLog {
	private String userId;
	private boolean isPostConfirm = false;
	
	private List<ConfirmLogItem> logs;
	
	public ConfirmLog() {
		logs = new ArrayList<ConfirmLogItem>();
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isPostConfirm() {
		return isPostConfirm;
	}

	public void setPostConfirm(boolean isPostConfirm) {
		this.isPostConfirm = isPostConfirm;
	}

	public List<ConfirmLogItem> getLogs() {
		return logs;
	}

	public void setLogs(List<ConfirmLogItem> logs) {
		this.logs = logs;
	}

	public ConfirmLog(String userId, boolean isPostConfirm) {
		this.userId = userId;
		this.isPostConfirm = isPostConfirm;
		logs = new ArrayList<ConfirmLogItem>();
	}
	
	public void addLog(ConfirmLogItem log) {
		this.logs.add(log);
	}
}
