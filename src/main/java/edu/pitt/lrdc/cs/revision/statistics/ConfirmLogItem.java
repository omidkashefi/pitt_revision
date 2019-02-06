package edu.pitt.lrdc.cs.revision.statistics;

public class ConfirmLogItem {
	private int	oldSentenceID;
	private int newSentenceID;
	private String confirmedType;
	private String motivateReason;
	public int getOldSentenceID() {
		return oldSentenceID;
	}
	public void setOldSentenceID(int oldSentenceID) {
		this.oldSentenceID = oldSentenceID;
	}
	public int getNewSentenceID() {
		return newSentenceID;
	}
	public void setNewSentenceID(int newSentenceID) {
		this.newSentenceID = newSentenceID;
	}
	public String getConfirmedType() {
		return confirmedType;
	}
	public void setConfirmedType(String confirmedType) {
		this.confirmedType = confirmedType;
	}
	public String getMotivateReason() {
		return motivateReason;
	}
	public void setMotivateReason(String motivateReason) {
		this.motivateReason = motivateReason;
	}
	
	
}
