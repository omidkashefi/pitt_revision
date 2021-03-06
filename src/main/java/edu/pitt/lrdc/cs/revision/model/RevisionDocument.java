package edu.pitt.lrdc.cs.revision.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @version 1.0
 * @date 7/20/2014
 * @author zhangfan This defines the data structure recording all the
 *         information of the revision in one document(One draft to another
 *         draft)
 */

public class RevisionDocument {
	/**
	 * This is the name of the revision document loaded
	 */
	private String documentName;

	// The prompt information
	private String promptContent;

	public String getPromptContent() {
		return promptContent;
	}

	public void setPromptContent(String promptContent) {
		this.promptContent = promptContent;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	int oldCnt = 0;
	public int getOldWordCnt() {
		if(oldCnt != 0) return oldCnt;
		for(String str: this.getOldDraftSentences()) {
			oldCnt += str.split(" ").length;
		}
		return oldCnt;
	}
	
	int newCnt = 0;
	public int getNewWordCnt() {
		if(newCnt != 0) return newCnt;
		for(String str: this.getNewDraftSentences()) {
			newCnt += str.split(" ").length;
		}
		return newCnt;
	}
	
	private Hashtable<Integer, Integer> oldPurposeMap;
	private Hashtable<Integer, Integer> newPurposeMap;
	
	public int getPurposeofOld(int oldIndex) {
		if(oldPurposeMap == null) {
			oldPurposeMap = new Hashtable<Integer, Integer>();
			ArrayList<RevisionUnit> revisions = this.root.getRevisionUnitAtLevel(0);
			for(RevisionUnit unit: revisions) {
				ArrayList<Integer> oldSents = unit.getOldSentenceIndex();
				int revPurpose = unit.getRevision_purpose();
				for(Integer oldSent: oldSents) {
					if(oldSent != -1) {
						oldPurposeMap.put(oldSent, revPurpose);
					}
				}
			}
		}
		
	    if(!oldPurposeMap.containsKey(oldIndex)) {
	    	return RevisionPurpose.NOCHANGE;
	    } else {
	    	return oldPurposeMap.get(oldIndex);
	    }
	}
	
	public int getPurposeofNew(int newIndex) {
		if(newPurposeMap == null) {
			newPurposeMap = new Hashtable<Integer, Integer>();
			ArrayList<RevisionUnit> revisions = this.root.getRevisionUnitAtLevel(0);
			for(RevisionUnit unit: revisions) {
				ArrayList<Integer> newSents = unit.getNewSentenceIndex();
				int revPurpose = unit.getRevision_purpose();
				for(Integer newSent: newSents) {
					if(newSent != -1) {
						newPurposeMap.put(newSent, revPurpose);
					}
				}
			}
		}
		
		if(!newPurposeMap.containsKey(newIndex)) {
			return RevisionPurpose.NOCHANGE;
		} else {
			return newPurposeMap.get(newIndex);
		}
	}
	
	/**
	 * it contains all the contents of sentences in the old draft
	 */
	private ArrayList<String> oldDraftSentences = new ArrayList<String>();
	/**
	 * This contains all the contents of sentences in the new draft
	 */
	private ArrayList<String> newDraftSentences = new ArrayList<String>();

	/**
	 * Root of all the revisions in this document
	 */
	private RevisionUnit root;

	/**
	 * Root of all the predicted revisions in this document
	 */
	private RevisionUnit predictedRoot;

	/**
	 * The alignment of sentences
	 */
	private Hashtable<Integer, ArrayList<Integer>> mapNewtoOld = new Hashtable<Integer, ArrayList<Integer>>();
	private Hashtable<Integer, ArrayList<Integer>> mapOldtoNew = new Hashtable<Integer, ArrayList<Integer>>();

	/**
	 * The predicted alignment of sentences
	 */
	private Hashtable<Integer, ArrayList<Integer>> predicted_mapNewtoOld = new Hashtable<Integer, ArrayList<Integer>>();
	private Hashtable<Integer, ArrayList<Integer>> predicted_mapOldtoNew = new Hashtable<Integer, ArrayList<Integer>>();

	/**
	 * The relation of sentence and paragraph
	 */
	private Hashtable<Integer, Integer> oldParagraphMap = new Hashtable<Integer, Integer>();
	private Hashtable<Integer, Integer> newParagraphMap = new Hashtable<Integer, Integer>();

	/**
	 * Add the mapping of sentence and paragraph
	 * 
	 * @param oldIndex
	 * @param oldParaNo
	 */
	public void addOldSentenceParaMap(int oldIndex, int oldParaNo) {
		this.oldParagraphMap.put(oldIndex, oldParaNo);
	}

	/**
	 * Get the paragraph No of the old sentence index
	 * 
	 * @param oldIndex
	 * @return
	 */
	public int getParaNoOfOldSentence(int oldIndex) {
		if (this.oldParagraphMap == null || this.oldParagraphMap.size() == 0
				|| !this.oldParagraphMap.containsKey(oldIndex))
			return 0;
		return this.oldParagraphMap.get(oldIndex);
	}

	/**
	 * Add the mapping of new sentence and paragraph
	 * 
	 * @param newIndex
	 * @param newParaNo
	 */
	public void addNewSentenceParaMap(int newIndex, int newParaNo) {
		this.newParagraphMap.put(newIndex, newParaNo);
	}

	/**
	 * Get the paragraph No of the new sentence index
	 * 
	 * @param newIndex
	 * @return
	 */
	public int getParaNoOfNewSentence(int newIndex) {
		if (this.newParagraphMap == null || this.newParagraphMap.size() == 0
				|| !this.newParagraphMap.containsKey(newIndex))
			return 0;
		return this.newParagraphMap.get(newIndex);
	}

	/**
	 * get the content of the specified sentence in the old draft
	 * 
	 * @param index
	 *            The index of the sentence, starting from 1
	 * @return
	 */
	public String getOldSentence(int index) {
		return oldDraftSentences.get(index - 1);
	}

	/**
	 * get the content of the specified sentence in the new draft
	 * 
	 * @param index
	 *            The index of the sentence in the new drat, starting from 1
	 * @return
	 */
	public String getNewSentence(int index) {
		return newDraftSentences.get(index - 1);
	}

	/**
	 * For the alignment of new draft, shift upwards num alignments
	 * 
	 * @param startIndex
	 * @param num
	 */
	public void shiftAlignmentOfNew(int startIndex, int num) {
		Iterator<Integer> it = mapNewtoOld.keySet().iterator();
		ArrayList<Integer> newIndices = new ArrayList<Integer>();
		while (it.hasNext()) {
			int newIndex = it.next();
			newIndices.add(newIndex);
		}

		for (Integer newIndex : newIndices) {
			ArrayList<Integer> alignments = mapNewtoOld.get(newIndex); // Has
																		// already
																		// changed!!!!
			ArrayList<Integer> newAlignments = new ArrayList<Integer>();
			for (int i = 0; i < alignments.size(); i++) {
				int val = alignments.get(i);
				if (val > startIndex)
					val = val - num; // move up by num
				// alignments.set(i, val);
				newAlignments.add(val);
			}
			changeNewAlignment(newIndex, newAlignments);
		}
	}

	public void shiftAlignmentofOld(int startIndex, int num) {
		Iterator<Integer> it = mapOldtoNew.keySet().iterator();
		ArrayList<Integer> oldIndices = new ArrayList<Integer>();
		while (it.hasNext()) {
			int oldIndex = it.next();
			oldIndices.add(oldIndex);
		}

		for (Integer oldIndex : oldIndices) {
			ArrayList<Integer> alignments = mapOldtoNew.get(oldIndex);
			ArrayList<Integer> newAlignments = new ArrayList<Integer>();
			for (int i = 0; i < alignments.size(); i++) {
				int val = alignments.get(i);
				if (val > startIndex)
					val = val - num;
				// alignments.set(i, val);
				newAlignments.add(val);
			}
			changeOldAlignment(oldIndex, newAlignments);
		}
	}

	/**
	 * Get the concatenate sentences from new
	 * 
	 * @param indices
	 * @return
	 */
	public String getNewSentences(ArrayList<Integer> indices) {
		String sent = "";
		Collections.sort(indices);
		if (indices != null) {
			for (Integer i : indices) {
				if (i != -1)
					sent += getNewSentence(i) + " ";
				// sent += getNewSentence(i) + "\n";
			}
		}
		return sent.trim();
	}

	public String getOldSentences(ArrayList<Integer> indices) {
		String sent = "";
		Collections.sort(indices);
		if (indices != null) {
			for (Integer i : indices) {
				if (i > 0)
					sent += getOldSentence(i) + " ";
				// sent += getOldSentence(i) + "\n";
			}
		}
		return sent.trim();
	}
	
	/**
	 * Copy the document for evaluation or for full-stack prediction
	 * @return
	 * @throws Exception 
	 */
	public RevisionDocument copy() throws Exception {
		RevisionDocument copyDoc = new RevisionDocument();
		ArrayList<String> olds = copyDoc.getOldDraftSentences();
		ArrayList<String> news = copyDoc.getNewDraftSentences();
		for(String oldSent : oldDraftSentences) {
			olds.add(oldSent);
		}
		for(String newSent : newDraftSentences) {
			news.add(newSent);
		}
		copyDoc.setDocumentName(this.documentName);
		int oldDraftNum = olds.size();
		int newDraftNum = news.size();
		for(int i = 1;i<=oldDraftNum;i++) {
			copyDoc.addOldSentenceParaMap(i, this.getParaNoOfOldSentence(i));
		}
		for(int i = 1;i<=newDraftNum;i++) {
			copyDoc.addNewSentenceParaMap(i, this.getParaNoOfNewSentence(i));
		}
		RevisionUnit rootUnit = new RevisionUnit(true);
		rootUnit.setRevision_level(3);
		copyDoc.setRoot(rootUnit);
		return copyDoc;
	}
 
	
	/**
	 * Copy the document for evaluation or for full-stack prediction
	 * @return
	 * @throws Exception 
	 */
	public RevisionDocument copyAlign() throws Exception {
		RevisionDocument copyDoc = new RevisionDocument();
		ArrayList<String> olds = copyDoc.getOldDraftSentences();
		ArrayList<String> news = copyDoc.getNewDraftSentences();
		for(String oldSent : oldDraftSentences) {
			olds.add(oldSent);
		}
		for(String newSent : newDraftSentences) {
			news.add(newSent);
		}
		copyDoc.setDocumentName(this.documentName);
		int oldDraftNum = olds.size();
		int newDraftNum = news.size();
		for(int i = 1;i<=oldDraftNum;i++) {
			copyDoc.addOldSentenceParaMap(i, this.getParaNoOfOldSentence(i));
		}
		for(int i = 1;i<=newDraftNum;i++) {
			copyDoc.addNewSentenceParaMap(i, this.getParaNoOfNewSentence(i));
		}
		RevisionUnit rootUnit = new RevisionUnit(true);
		rootUnit.setRevision_level(3);
		copyDoc.setRoot(rootUnit);
		
		RevisionUnit predictedRootUnit = new RevisionUnit(true);
		predictedRootUnit.setRevision_level(3);
		copyDoc.setPredictedRoot(predictedRootUnit);
		
		Hashtable<Integer, ArrayList<Integer>> copyOldToNew = new Hashtable<Integer, ArrayList<Integer>>();
		Hashtable<Integer, ArrayList<Integer>> copyNewToOld = new Hashtable<Integer, ArrayList<Integer>>();
		
		Iterator<Integer> itOld = this.mapOldtoNew.keySet().iterator();
		while(itOld.hasNext()) {
			int index = itOld.next();
			ArrayList<Integer> copy = new ArrayList<Integer>();
			copyList(copy, this.mapOldtoNew.get(index));
			copyOldToNew.put(index, copy);
		}
		Iterator<Integer> itNew = this.mapNewtoOld.keySet().iterator();
		while(itNew.hasNext()) {
			int index = itNew.next();
			ArrayList<Integer> copy = new ArrayList<Integer>();
			copyList(copy, this.mapNewtoOld.get(index));
			copyNewToOld.put(index, copy);
		}
		copyDoc.mapOldtoNew = copyOldToNew;
		copyDoc.mapNewtoOld = copyNewToOld;
		return copyDoc;
		
	}
	
	public void copyList(ArrayList<Integer> a, ArrayList<Integer> b) {
		for(Integer i : b) {
			a.add(i);
		}
	}
	/**
	 * At a sentence to the old draft
	 * 
	 * @param sentence
	 */
	public void addOldSentence(String sentence) {
		this.oldDraftSentences.add(sentence);
	}

	/**
	 * Add a sentence to the new draft
	 * 
	 * @param sentence
	 */
	public void addNewSentence(String sentence) {
		this.newDraftSentences.add(sentence);
	}

	/**
	 * get the root revision unit
	 * 
	 * @return root
	 */
	public RevisionUnit getRoot() {
		return this.root;
	}

	public void setRoot(RevisionUnit root) {
		this.root = root;
	}

	/**
	 * get the predicted root revision unit
	 * 
	 * @return root
	 */
	public RevisionUnit getPredictedRoot() {
		return this.predictedRoot;
	}

	/**
	 * Set the predicted revision root
	 * 
	 * @param root
	 */
	public void setPredictedRoot(RevisionUnit root) {
		this.predictedRoot = root;
	}

	public ArrayList<String> getNewDraftSentences() {
		return newDraftSentences;
	}

	public ArrayList<String> getOldDraftSentences() {
		return oldDraftSentences;
	}

	/**
	 * For GUI design
	 * 
	 * @return
	 */
	public String[] getNewSentencesArray() {
		String[] array = new String[newDraftSentences.size()];
		return newDraftSentences.toArray(array);
	}

	public String[] getOldSentencesArray() {
		String[] array = new String[oldDraftSentences.size()];
		return oldDraftSentences.toArray(array);
	}

	/**
	 * Add a mapping from new to old
	 * 
	 * @param newSentIndex
	 * @param oldSentIndex
	 */
	public void predictNewMappingIndex(int newSentIndex, int oldSentIndex) {
		if (predicted_mapNewtoOld.containsKey(newSentIndex)) {
			ArrayList<Integer> arr = predicted_mapNewtoOld.get(newSentIndex);
			boolean isFound = false;
			for (Integer index : arr) {
				if (index == oldSentIndex) {
					isFound = true;
					break;
				}
			}
			if (!isFound)
				arr.add(oldSentIndex);
		} else {
			ArrayList<Integer> arr = new ArrayList<Integer>();
			arr.add(oldSentIndex);
			this.predicted_mapNewtoOld.put(newSentIndex, arr);
		}
	}

	/**
	 * Add a mapping from old to new
	 * 
	 * @param oldSentIndex
	 * @param newSentIndex
	 */
	public void predictOldMappingIndex(int oldSentIndex, int newSentIndex) {
		if (predicted_mapOldtoNew.containsKey(oldSentIndex)) {
			ArrayList<Integer> arr = predicted_mapOldtoNew.get(oldSentIndex);
			boolean isFound = false;
			for (Integer index : arr) {
				if (index == newSentIndex) {
					isFound = true;
					break;
				}
			}
			if (!isFound)
				arr.add(newSentIndex);
		} else {
			ArrayList<Integer> arr = new ArrayList<Integer>();
			arr.add(newSentIndex);
			this.predicted_mapOldtoNew.put(oldSentIndex, arr);
		}
	}

	/**
	 * Add a mapping from new to old
	 * 
	 * @param newSentIndex
	 * @param oldSentIndex
	 */
	public void addNewMappingIndex(int newSentIndex, int oldSentIndex) {
		if (newSentIndex != -1) {
			if (mapNewtoOld.containsKey(newSentIndex)) {
				ArrayList<Integer> arr = mapNewtoOld.get(newSentIndex);
				boolean isFound = false;
				for (Integer index : arr) {
					if (index == oldSentIndex) {
						isFound = true;
						break;
					}
				}
				if (!isFound && oldSentIndex != -1)
					arr.add(oldSentIndex);
			} else {
				ArrayList<Integer> arr = new ArrayList<Integer>();
				if (oldSentIndex != -1)
					arr.add(oldSentIndex);
				this.mapNewtoOld.put(newSentIndex, arr);
			}
		}
	}

	/**
	 * Add a mapping from old to new
	 * 
	 * @param oldSentIndex
	 * @param newSentIndex
	 */
	public void addOldMappingIndex(int oldSentIndex, int newSentIndex) {
		if (oldSentIndex != -1) {
			if (mapOldtoNew.containsKey(oldSentIndex)) {
				ArrayList<Integer> arr = mapOldtoNew.get(oldSentIndex);
				boolean isFound = false;
				for (Integer index : arr) {
					if (index == newSentIndex) {
						isFound = true;
						break;
					}
				}
				if (!isFound)
					arr.add(newSentIndex);
			} else {
				ArrayList<Integer> arr = new ArrayList<Integer>();
				if (newSentIndex != -1)
					arr.add(newSentIndex);
				this.mapOldtoNew.put(oldSentIndex, arr);
			}
		}
	}

	/**
	 * Get the mapping of old from new
	 * 
	 * @param newSentIndex
	 * @return
	 */
	public ArrayList<Integer> getOldFromNew(int newSentIndex) {
		if (this.mapNewtoOld.containsKey(newSentIndex))
			return this.mapNewtoOld.get(newSentIndex);
		return new ArrayList<Integer>();
	}

	/**
	 * Get the mapping of new from old
	 * 
	 * @param oldSentIndex
	 * @return
	 */
	public ArrayList<Integer> getNewFromOld(int oldSentIndex) {
		if (this.mapOldtoNew.containsKey(oldSentIndex))
			return this.mapOldtoNew.get(oldSentIndex);
		return new ArrayList<Integer>();
	}

	/**
	 * Clear all the predicts
	 */
	public void clearPredicts() {
		this.predicted_mapNewtoOld.clear();
		this.predicted_mapOldtoNew.clear();
	}

	/**
	 * Get the predicted mapping of old from new
	 * 
	 * @param newSentIndex
	 * @return
	 */
	public ArrayList<Integer> getPredictedOldFromNew(int newSentIndex) {
		if (this.predicted_mapNewtoOld.containsKey(newSentIndex))
			return this.predicted_mapNewtoOld.get(newSentIndex);
		return new ArrayList<Integer>();
	}

	/**
	 * Get the predicted mapping of new from old
	 * 
	 * @param oldSentIndex
	 * @return
	 */
	public ArrayList<Integer> getPredictedNewFromOld(int oldSentIndex) {
		if (this.predicted_mapOldtoNew.containsKey(oldSentIndex))
			return this.predicted_mapOldtoNew.get(oldSentIndex);
		return new ArrayList<Integer>();
	}

	/**
	 * Remove a specific index
	 * 
	 * @param arr
	 * @param index
	 */
	public void removeIndex(ArrayList<Integer> arr, int index) {
		/*
		 * for (int i = 0; i < arr.size(); i++) { if (arr.get(i) == index) {
		 * arr.remove(i); return; } }
		 */
		int i = 0;
		while (i < arr.size()) {
			if (arr.get(i) == index) {
				arr.remove(i);
			} else {
				i++;
			}
		}
	}

	public void addIndex(ArrayList<Integer> arr, int index) {
		arr.add(index);
	}

	public void mergeOldSentences(int oldIndexStart, int num) {
		if (num == 1)
			return;
		String mergedText = "";
		for (int i = oldIndexStart; i < oldIndexStart + num; i++) {
			mergedText += oldDraftSentences.get(i - 1) + " ";
		}
		mergedText = mergedText.trim();
		for (int i = 0; i < num - 1; i++) {
			oldDraftSentences.remove(oldIndexStart - 1);
		}
		oldDraftSentences.set(oldIndexStart - 1, mergedText);
	}

	public void mergeNewSentences(int newIndexStart, int num) {
		if (num == 1)
			return; // do nothing;
		String mergedText = "";
		for (int i = newIndexStart; i < newIndexStart + num; i++) {
			mergedText += newDraftSentences.get(i - 1) + " ";
		}
		mergedText = mergedText.trim();
		for (int i = 0; i < num - 1; i++) {
			newDraftSentences.remove(newIndexStart - 1);
		}
		newDraftSentences.set(newIndexStart - 1, mergedText);
	}

	/**
	 * Change the alignment of the new sentence
	 * 
	 * @param oldIndices
	 * @param newIndices
	 */
	public void changeNewAlignment(int newIndex, ArrayList<Integer> oldIndices) {
		ArrayList<Integer> oldAligns = this.mapNewtoOld.get(newIndex);
		if (oldAligns == null)
			oldAligns = new ArrayList<Integer>();
		for (Integer oldIndex : oldAligns) {
			if (this.mapOldtoNew.containsKey(oldIndex)) {
				removeIndex(this.mapOldtoNew.get(oldIndex), newIndex);
			}
		}

		// Should remove the correlated revisions
		ArrayList<RevisionUnit> toRemove = this.getRoot()
				.getRevisionUnitNewAtLevel(0, newIndex);
		//Added: the aligned revisions should also be removed
		for(Integer oldIndex: oldIndices){
			toRemove.addAll(this.getRoot().getRevisionUnitOldAtLevel(0, oldIndex));
		}
		
		for (RevisionUnit unit : toRemove) {
			unit.setAbandoned();
		}
		if (newIndex != -1) {
			for (Integer oldIndex : oldIndices) {
				if (this.mapOldtoNew.containsKey(oldIndex)) {
					addIndex(this.mapOldtoNew.get(oldIndex), newIndex);
				} else {
					ArrayList<Integer> newArr = new ArrayList<Integer>();
					newArr.add(newIndex);
					this.mapOldtoNew.put(oldIndex, newArr);
				}
			}
		}
		this.mapNewtoOld.put(newIndex, oldIndices);
	}

	/**
	 * Change the alignment of the old sentences
	 * 
	 * @param oldIndex
	 * @param newIndices
	 */
	public void changeOldAlignment(int oldIndex, ArrayList<Integer> newIndices) {
		ArrayList<Integer> newAligns = this.mapOldtoNew.get(oldIndex);
		if (newAligns == null)
			newAligns = new ArrayList<Integer>();
		for (Integer newIndex : newAligns) {
			if (this.mapNewtoOld.containsKey(newIndex)) {
				removeIndex(this.mapNewtoOld.get(newIndex), oldIndex);
			}
		}
		// Should remove the correlated revisions
		ArrayList<RevisionUnit> toRemove = this.getRoot()
				.getRevisionUnitOldAtLevel(0, oldIndex);
		
		/**New added, remove the errors*/
		for(Integer newIndex: newIndices) {
			toRemove.addAll(this.getRoot().getRevisionUnitNewAtLevel(0, newIndex));
		}
		
		
		for (RevisionUnit unit : toRemove) {
			unit.setAbandoned();
		}

		for (Integer newIndex : newIndices) {
			if (oldIndex != -1) {
				if (this.mapNewtoOld.containsKey(newIndex)) {
					addIndex(this.mapNewtoOld.get(newIndex), oldIndex);
				} else {
					ArrayList<Integer> oldArr = new ArrayList<Integer>();
					oldArr.add(oldIndex);
					this.mapNewtoOld.put(newIndex, oldArr);
				}
			}
		}
		this.mapOldtoNew.put(oldIndex, newIndices);
	}

	/**
	 * Get the first sentence of the paragraph
	 * 
	 * @param paraNo
	 * @return
	 */
	public int getFirstOfOldParagraph(int paraNo) {
		Iterator<Integer> it = oldParagraphMap.keySet().iterator();
		int first = Integer.MAX_VALUE;
		while (it.hasNext()) {
			int sentenceIndex = it.next();
			int temp = oldParagraphMap.get(sentenceIndex);
			if (temp == paraNo) {
				if (sentenceIndex < first)
					first = sentenceIndex;
			}
		}
		return first;
	}

	/**
	 * Get the last sentence of the paragraph
	 */
	public int getLastOfOldParagraph(int paraNo) {
		Iterator<Integer> it = oldParagraphMap.keySet().iterator();
		int last = Integer.MIN_VALUE;
		while (it.hasNext()) {
			int sentenceIndex = it.next();
			int temp = oldParagraphMap.get(sentenceIndex);
			if (temp == paraNo) {
				if (sentenceIndex > last)
					last = sentenceIndex;
			}
		}
		return last;
	}

	/**
	 * Get the first sentence of the paragraph New
	 * 
	 * @param paraNo
	 * @return
	 */
	public int getFirstOfNewParagraph(int paraNo) {
		Iterator<Integer> it = newParagraphMap.keySet().iterator();
		int first = Integer.MAX_VALUE;
		while (it.hasNext()) {
			int sentenceIndex = it.next();
			int temp = newParagraphMap.get(sentenceIndex);
			if (temp == paraNo) {
				if (sentenceIndex < first)
					first = sentenceIndex;
			}
		}
		return first;
	}

	/**
	 * Get the last sentence of the paragraph
	 */
	public int getLastOfNewParagraph(int paraNo) {
		Iterator<Integer> it = newParagraphMap.keySet().iterator();
		int last = Integer.MIN_VALUE;
		while (it.hasNext()) {
			int sentenceIndex = it.next();
			int temp = newParagraphMap.get(sentenceIndex);
			if (temp == paraNo) {
				if (sentenceIndex > last)
					last = sentenceIndex;
			}
		}
		return last;
	}

	/**
	 * Get the total num of old paragraphs
	 * 
	 * @return
	 */
	public int getOldParagraphNum() {
		int num = 0;
		Iterator<Integer> it = oldParagraphMap.keySet().iterator();
		while (it.hasNext()) {
			int sentenceIndex = it.next();
			int temp = oldParagraphMap.get(sentenceIndex);
			if (temp > num)
				num = temp;
		}
		return num;
	}

	/**
	 * Get the total num of new paragraphs
	 * 
	 * @return
	 */
	public int getNewParagraphNum() {
		int num = 0;
		Iterator<Integer> it = newParagraphMap.keySet().iterator();
		while (it.hasNext()) {
			int sentenceIndex = it.next();
			int temp = newParagraphMap.get(sentenceIndex);
			if (temp > num)
				num = temp;
		}
		return num;
	}

	/**
	 * Get the predicted alignment pairs Pair looks as Old, New
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<ArrayList<Integer>>> getPredictedAlignedIndices() {
		ArrayList<ArrayList<ArrayList<Integer>>> allVals = new ArrayList<ArrayList<ArrayList<Integer>>>();
		int newSentNum = this.newDraftSentences.size();
		int oldSentNum = this.oldDraftSentences.size();
		HashSet<Integer> usedOld = new HashSet<Integer>(); // mark the ones that
															// has been aligned
		for (int i = 1; i <= newSentNum; i++) {
			ArrayList<Integer> oldAligns = this.getPredictedOldFromNew(i);
			ArrayList<Integer> newAligns = new ArrayList<Integer>();
			if (oldAligns != null && oldAligns.size() > 0) { // new sentence is
																// aligned
				HashSet<Integer> tempNewAligns = new HashSet<Integer>();
				for (Integer oldAlign : oldAligns) {
					usedOld.add(oldAlign);
					ArrayList<Integer> newA = this
							.getPredictedNewFromOld(oldAlign);
					for (Integer temp : newA) { // Get the aligned sentences of
												// the old sentence
						tempNewAligns.add(temp);
					}
				}
				for (Integer temp : tempNewAligns) {
					if (temp != -1) {
						newAligns.add(temp); // collected all the new alignments
					}
				}

			} else {
				newAligns.add(i);
				oldAligns = new ArrayList<Integer>();
			}

			String newSent = this.getNewSentences(newAligns);
			String oldSent = this.getOldSentences(oldAligns);
			if (!newSent.equals(oldSent)) {
				ArrayList<ArrayList<Integer>> alignPair = new ArrayList<ArrayList<Integer>>();
				alignPair.add(oldAligns);
				alignPair.add(newAligns);

				allVals.add(alignPair);
			}
		}
		// For those that has not be covered in the step above, should be
		// deletes
		for (int i = 1; i <= oldSentNum; i++) {
			if (!usedOld.contains(i)) {
				ArrayList<ArrayList<Integer>> alignPair = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> oldAligns = new ArrayList<Integer>();
				oldAligns.add(i);
				alignPair.add(oldAligns);
				alignPair.add(new ArrayList<Integer>());

				allVals.add(alignPair);
			}
		}

		return allVals;
	}

	public ArrayList<ArrayList<ArrayList<Integer>>> getAlignedIndices() {
		ArrayList<ArrayList<ArrayList<Integer>>> allVals = new ArrayList<ArrayList<ArrayList<Integer>>>();
		int newSentNum = this.newDraftSentences.size();
		int oldSentNum = this.oldDraftSentences.size();
		HashSet<Integer> usedOld = new HashSet<Integer>(); // mark the ones that
															// has been aligned
		for (int i = 1; i <= newSentNum; i++) {
			ArrayList<Integer> oldAligns = this.getOldFromNew(i);
			ArrayList<Integer> newAligns = new ArrayList<Integer>();
			if (oldAligns != null && oldAligns.size() > 0) { // new sentence is
																// aligned
				HashSet<Integer> tempNewAligns = new HashSet<Integer>();
				for (Integer oldAlign : oldAligns) {
					usedOld.add(oldAlign);
					ArrayList<Integer> newA = this
							.getNewFromOld(oldAlign);
					for (Integer temp : newA) { // Get the aligned sentences of
												// the old sentence
						tempNewAligns.add(temp);
					}
				}
				for (Integer temp : tempNewAligns) {
					if (temp != -1) {
						newAligns.add(temp); // collected all the new alignments
					}
				}

			} else {
				newAligns.add(i);
				oldAligns = new ArrayList<Integer>();
			}

			String newSent = this.getNewSentences(newAligns);
			String oldSent = this.getOldSentences(oldAligns);
			if (!newSent.equals(oldSent)) {
				ArrayList<ArrayList<Integer>> alignPair = new ArrayList<ArrayList<Integer>>();
				alignPair.add(oldAligns);
				alignPair.add(newAligns);

				allVals.add(alignPair);
			}
		}
		// For those that has not be covered in the step above, should be
		// deletes
		for (int i = 1; i <= oldSentNum; i++) {
			if (!usedOld.contains(i)) {
				ArrayList<ArrayList<Integer>> alignPair = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> oldAligns = new ArrayList<Integer>();
				oldAligns.add(i);
				alignPair.add(oldAligns);
				alignPair.add(new ArrayList<Integer>());

				allVals.add(alignPair);
			}
		}

		return allVals;
	}

	
	/**
	 * Materialize the predicted alignment
	 * 
	 * !!Should change this to clear and add later
	 */
	public void materializeAlignment() {
		this.mapOldtoNew = this.predicted_mapOldtoNew;
		this.mapNewtoOld = this.predicted_mapNewtoOld;
	}

	/**
	 * Materialize revision classification
	 */
	public void materializeRevisionPurpose() {
		this.root = this.predictedRoot;
	}

	public StatisticInfo toStatisticInfo() {
		StatisticInfo info = new StatisticInfo();
		String docName = this.getDocumentName();
		docName = docName.substring(docName.indexOf("Annotation_"));
		// System.out.println(docName);
		docName = docName.substring(0, docName.indexOf(".txt.xlsx"));
		docName = docName.substring(docName.indexOf("_") + 1);
		info.setPseudoname(docName);

		int totalOldLength = this.getOldSentencesArray().length;
		int totalNewLength = this.getNewSentencesArray().length;

		ArrayList<RevisionUnit> rus = this.getRoot().getRevisionUnitAtLevel(0);
		int totalAdds = 0;
		int totalDels = 0;
		HashSet<Integer> modis = new HashSet<Integer>();

		int surfaceEdits = 0;
		int contentEdits = 0;

		for (RevisionUnit ru : rus) {
			if (ru.getRevision_op() == RevisionOp.ADD) {
				totalAdds++;
			} else if (ru.getRevision_op() == RevisionOp.DELETE) {
				totalDels++;
			} else {
				ArrayList<Integer> oldIndices = ru.getOldSentenceIndex();
				for (Integer oldIndex : oldIndices) {
					modis.add(oldIndex);
				}
			}

			if (ru.getRevision_purpose() == RevisionPurpose.CLAIMS_IDEAS) {
				contentEdits++;
			} else {
				surfaceEdits++;
			}
		}

		double addRatio = totalAdds * 1.0 / totalNewLength;
		double deleteRatio = totalDels * 1.0 / totalOldLength;
		double modifyRatio = modis.size() * 1.0 / totalOldLength;

		info.setAdditions(totalAdds);
		info.setAddPercent(addRatio);
		info.setContentEdits(contentEdits);
		info.setDeletePercent(deleteRatio);
		info.setDeletions(totalDels);
		info.setEditedPerecent(modifyRatio);
		info.setModifications(modis.size());
		info.setSurfaceEdits(surfaceEdits);
		info.setContentEdits(contentEdits);
		return info;
	}

	public void removeNumber(ArrayList<Integer> list) {
		int i = 0;
		while (i < list.size()) {
			if (list.get(i) == -1) {
				list.remove(i);
			} else {
				i++;
			}
		}
	}

	public String getIndiceStr(ArrayList<Integer> indices) {
		Collections.sort(indices);
		String indiceStr = "";
		for(Integer index: indices) {
			if(index!=-1) {
				indiceStr += index + "-";
			}
		}
		return indiceStr;
	}
	public void removeRedundant() {
		HashSet<String> existingRevs = new HashSet<String>();
		ArrayList<RevisionUnit> revisions = this.getRoot().getRevisionUnitAtLevel(0);
		for(RevisionUnit unit:revisions) {
			String label = "PURPOSE:"+ unit.getRevision_purpose() + "OLD:"+getIndiceStr(unit.getOldSentenceIndex()) + "NEW:"+getIndiceStr(unit.getNewSentenceIndex());
			if(existingRevs.contains(label)) {
				unit.setAbandoned();
			} else {
				existingRevs.add(label);
			}
		}
	}
	
	// After realignment remove the wrong annotations
	public void check() {
		ArrayList<RevisionUnit> units = this.getRoot()
				.getRevisionUnitAtLevel(0);

		ArrayList<RevisionUnit> removes = new ArrayList<RevisionUnit>();
		for (RevisionUnit unit : units) {
			ArrayList<Integer> oldIndices = unit.getOldSentenceIndex();
			ArrayList<Integer> newIndices = unit.getNewSentenceIndex();
			removeNumber(oldIndices);
			removeNumber(newIndices);

			if (oldIndices == null || oldIndices.size() == 0
					|| (oldIndices.size() == 1 && oldIndices.get(0) == -1)) {
				if (unit.getRevision_op() != RevisionOp.ADD)
					removes.add(unit);
			} else if (newIndices == null || newIndices.size() == 0
					|| (newIndices.size() == 1 && newIndices.get(0) == -1)) {
				if (unit.getRevision_op() != RevisionOp.DELETE)
					removes.add(unit);
			} else {
				if (this.getOldSentences(oldIndices).trim()
						.equals(this.getNewSentences(newIndices).trim())) {
					removes.add(unit);
				}
			}
		}
		for (RevisionUnit rem : removes) {
			// units.remove(rem);
			rem.setAbandoned();
		}
	}

	public String[] regenerateDrafts() {
		String[] drafts = new String[2];
		String draft1 = "        ";
		String draft2 = "        ";
		int paraNum = 1;
		for (int i = 0; i < oldDraftSentences.size(); i++) {
			int index = i + 1;
			int currParaNo = this.getParaNoOfOldSentence(index);
			if (currParaNo > paraNum) {
				draft1 += "\n        ";
				draft1 += oldDraftSentences.get(i) + " ";
				paraNum = currParaNo;
			} else {
				draft1 += oldDraftSentences.get(i) + " ";
			}
		}

		paraNum = 1;
		for (int i = 0; i < newDraftSentences.size(); i++) {
			int index = i + 1;
			int currParaNo = this.getParaNoOfNewSentence(index);
			if (currParaNo > paraNum) {
				draft2 += "\n        ";
				draft2 += newDraftSentences.get(i) + " ";
				paraNum = currParaNo;
			} else {
				draft2 += newDraftSentences.get(i) + " ";
			}
		}
		drafts[0] = draft1;
		drafts[1] = draft2;
		return drafts;
	}
	
	public String getOriginalTxtOld() {
		String oldTxt = "";
		int oldNum = this.getOldParagraphNum();
		for(int i = 1;i<=oldNum;i++) {
			int start = this.getFirstOfOldParagraph(i);
			int end = this.getLastOfOldParagraph(i);
			for(int j = start;j<=end;j++) {
				String sentence = this.getOldSentence(j);
				sentence = sentence.replaceAll("\\s\\.", ".");
				sentence = sentence.replaceAll("\\s,", ",");
				oldTxt += sentence+ " ";
			}
			oldTxt+="\n\n";
		}
		return oldTxt;
	}
	
	public String getOriginalTxtNew() {
		String newTxt = "";
		int newNum = this.getNewParagraphNum();
		for(int i = 1;i<=newNum;i++) {
			int start = this.getFirstOfNewParagraph(i);
			int end = this.getLastOfNewParagraph(i);
			for(int j = start;j<=end;j++) {
				String sentence = this.getNewSentence(j);
				sentence = sentence.replaceAll("\\s\\.", ".");
				sentence = sentence.replaceAll("\\s\\.", ".");
				newTxt += sentence + " ";
			}
			newTxt+="\n\n";
		}
		return newTxt;
	}
}
