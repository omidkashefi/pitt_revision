package edu.pitt.lrdc.cs.revision.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RevisionConfirmation {
	private String docName;
	private HashMap<Integer, ConfirmationItem> oldRevisionConfirmationIndex;
	private HashMap<Integer, ConfirmationItem> newRevisionConfirmationIndex;

	private List<ConfirmationItem> items;

	public HashMap<String, Integer> getMotivationCounts() {
		HashMap<String, Integer> motivationCnts = new HashMap<String, Integer>();
		for(ConfirmationItem item: items) {
			String motivation = item.getMotivation();
			if(!motivationCnts.containsKey(motivation)) {
				motivationCnts.put(motivation, 1); 
			} else {
				motivationCnts.put(motivation, motivationCnts.get(motivation)+1);
			}
		}
		return motivationCnts;
	}
	
	public double getDiff(RevisionDocument doc) {
		ArrayList<RevisionUnit> units = doc.getRoot().getRevisionUnitAtLevel(0);
		int totals = 0;
		int agrees = 0;
		for (RevisionUnit unit : units) {
			ArrayList<Integer> oldSentenceIndices = unit.getOldSentenceIndex();
			ArrayList<Integer> newSentenceIndices = unit.getNewSentenceIndex();
			int oldIndex = -1;
			int newIndex = -1;
			if (oldSentenceIndices != null && oldSentenceIndices.size() > 0) {
				oldIndex = oldSentenceIndices.get(0);
			}
			if (newSentenceIndices != null && newSentenceIndices.size() > 0) {
				newIndex = newSentenceIndices.get(0);
			}

			ConfirmationItem confirm = null;
			if (oldIndex != -1
					&& oldRevisionConfirmationIndex.containsKey(oldIndex)) {
				confirm = oldRevisionConfirmationIndex.get(oldIndex);
			} else if (newIndex != -1
					&& newRevisionConfirmationIndex.containsKey(newIndex)) {
				confirm = newRevisionConfirmationIndex.get(newIndex);
			} else {

			}

			if (confirm != null) {
				totals++;
				String purpose = confirm.getConfirmedPurpose();
				if (purpose.equals(RevisionPurpose.getPurposeName(unit
						.getRevision_purpose()))) {
					agrees++;
				} else {
					System.out.println("ERROR:"
							+ RevisionPurpose.getPurposeName(unit
									.getRevision_purpose()) + ", CONFIRMED: "
							+ purpose);
				}
			}
		}

		return agrees * 1.0 / totals;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public HashMap<Integer, ConfirmationItem> getOldRevisionConfirmationIndex() {
		return oldRevisionConfirmationIndex;
	}

	public void setOldRevisionConfirmationIndex(
			HashMap<Integer, ConfirmationItem> oldRevisionConfirmationIndex) {
		this.oldRevisionConfirmationIndex = oldRevisionConfirmationIndex;
	}

	public HashMap<Integer, ConfirmationItem> getNewRevisionConfirmationIndex() {
		return newRevisionConfirmationIndex;
	}

	public void setNewRevisionConfirmationIndex(
			HashMap<Integer, ConfirmationItem> newRevisionConfirmationIndex) {
		this.newRevisionConfirmationIndex = newRevisionConfirmationIndex;
	}

	public RevisionConfirmation() {
		oldRevisionConfirmationIndex = new HashMap<Integer, ConfirmationItem>();
		newRevisionConfirmationIndex = new HashMap<Integer, ConfirmationItem>();
		items = new ArrayList<ConfirmationItem>();
	}

	public void addItem(ConfirmationItem item) {
		int oldIndex = item.getOldSentenceIndex();
		int newIndex = item.getNewSentenceIndex();
		items.add(item);
		if (oldIndex != -1)
			oldRevisionConfirmationIndex.put(oldIndex, item);
		if (newIndex != -1)
			newRevisionConfirmationIndex.put(newIndex, item);
	}

	public ConfirmationItem getItemOld(int oldIndex) {
		if (!oldRevisionConfirmationIndex.containsKey(oldIndex)) {
			return null;
		} else {
			return oldRevisionConfirmationIndex.get(oldIndex);
		}
	}

	public ConfirmationItem getItemNew(int newIndex) {
		if (!newRevisionConfirmationIndex.containsKey(newIndex)) {
			return null;
		} else {
			return newRevisionConfirmationIndex.get(newIndex);
		}
	}

	public String toString() {
		String str = this.docName + "\n";
		for (ConfirmationItem item : items) {
			str += item.toString() + "\n";
		}
		return str;
	}
}
