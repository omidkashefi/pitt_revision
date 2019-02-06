package edu.pitt.lrdc.cs.revision.model;

public class ConfirmationItem {
	private int oldSentenceIndex;
	private int newSentenceIndex;
	private String confirmedPurpose;
	private String motivation;
	public int getOldSentenceIndex() {
		return oldSentenceIndex;
	}
	public void setOldSentenceIndex(int oldSentenceIndex) {
		this.oldSentenceIndex = oldSentenceIndex;
	}
	public int getNewSentenceIndex() {
		return newSentenceIndex;
	}
	public void setNewSentenceIndex(int newSentenceIndex) {
		this.newSentenceIndex = newSentenceIndex;
	}
	public String getConfirmedPurpose() {
		return confirmedPurpose;
	}
	public void setConfirmedPurpose(String confirmedPurpose) {
		this.confirmedPurpose = confirmedPurpose;
	}
	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}	
	
	public String toString() {
		return oldSentenceIndex+":"+newSentenceIndex+", confirmed:"+ confirmedPurpose + ", motivation: "+motivation;
	}
}
