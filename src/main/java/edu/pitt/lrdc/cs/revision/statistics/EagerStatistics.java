package edu.pitt.lrdc.cs.revision.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import edu.pitt.lrdc.cs.revision.io.RevisionDocumentReader;
import edu.pitt.lrdc.cs.revision.model.RevisionDocument;
import edu.pitt.lrdc.cs.revision.model.RevisionOp;
import edu.pitt.lrdc.cs.revision.model.RevisionPurpose;
import edu.pitt.lrdc.cs.revision.model.RevisionUnit;

public class EagerStatistics {
	public static void main(String[] args) throws Exception {
		List<String> paths12 = new ArrayList<String>();
		List<String> paths23 = new ArrayList<String>();
		
		/*
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\ESL\\Control\\Rev12");
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\ESL\\Experiment\\Rev12");
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\NATIVE\\Control\\Rev12");
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\NATIVE\\Experiment\\Rev12");

		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\ESL\\Control\\Rev23");
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\ESL\\Experiment\\Rev23");
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\NATIVE\\Control\\Rev23");
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\NATIVE\\Experiment\\Rev23");*/
		
		
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\ESL\\Experiment\\Rev12");
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\NATIVE\\Experiment\\Rev12");
		
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\ESL_annotated\\Control\\Rev12");
		//paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\ESL_annotated\\Experiment\\Rev12");
		paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\NATIVE_annotated\\Control\\Rev12");
		//paths12.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\NATIVE_annotated\\Experiment\\Rev12");
		
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\ESL_annotated\\Control\\Rev23");
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\ESL_annotated\\Experiment\\Rev23");
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\NATIVE_annotated\\Control\\Rev23");
		paths23.add("C:\\Not Backed Up\\data\\eagerstudy\\checkedData\\NATIVE_annotated\\Experiment\\Rev23");
		List<HashMap<String, RevisionDocument>> allDocs = readDocuments(
				paths12, paths23);

		HashMap<String, RevisionDocument> docs12 = allDocs.get(0);
		HashMap<String, RevisionDocument> docs23 = allDocs.get(1);

		List<String> confirmPaths = new ArrayList<String>();
		confirmPaths
				.add("C:\\Not Backed Up\\data\\eagerstudy\\ANNOTATED\\ESL_annotated\\Experiment\\Confirmation");
		confirmPaths
				.add("C:\\Not Backed Up\\data\\eagerstudy\\Native\\Experiment\\Confirmation");
		HashMap<String, ConfirmLog[]> logMap = ConfirmLogReader
				.readLogs(confirmPaths);

		Table table = analyze(logMap, docs12, docs23, true);
		Table table2 = analyze(logMap, docs12, docs23, false);
		
		/*
		TablePrinter.printExcel(table,
				"C:\\Not Backed Up\\temp\\statistics(Exp).xlsx");
		TablePrinter.printExcel(table2,
				"C:\\Not Backed Up\\temp\\statistics(Control).xlsx");*/
		
		table.addControlGroup(table2);
		//TablePrinter.printExcel(table, "C:\\Not Backed Up\\temp\\statistics(All).xlsx");
		List<String> conditions = new ArrayList<String>();
		List<String> condValues = new ArrayList<String>();
		
		conditions.add("Group");
		condValues.add("1");
		conditions.add("Language");
		condValues.add("0");
		
		System.out.println("Experiment,  ESL");
		table.printSums("ContentCount12", conditions, condValues);
		table.printSums("ContentCount23", conditions, condValues);
		table.printSums("SurfaceCount12", conditions, condValues);
		table.printSums("SurfaceCount23", conditions, condValues);
		
		condValues.set(0, "2");
		condValues.set(1, "0");
		System.out.println("Control,  ESL");
		table.printSums("ContentCount12", conditions, condValues);
		table.printSums("ContentCount23", conditions, condValues);
		table.printSums("SurfaceCount12", conditions, condValues);
		table.printSums("SurfaceCount23", conditions, condValues);
		
		condValues.set(0, "1");
		condValues.set(1, "1");
		System.out.println("Experiment,  Native");
		table.printSums("ContentCount12", conditions, condValues);
		table.printSums("ContentCount23", conditions, condValues);
		table.printSums("SurfaceCount12", conditions, condValues);
		table.printSums("SurfaceCount23", conditions, condValues);
	
		
		condValues.set(0, "2");
		condValues.set(1, "1");
		System.out.println("Control,  Native");
		table.printSums("ContentCount12", conditions, condValues);	
		table.printSums("ContentCount23", conditions, condValues);
		table.printSums("SurfaceCount12", conditions, condValues);
		table.printSums("SurfaceCount23", conditions, condValues);
		
		
		conditions.clear();
		condValues.clear();
		System.out.println("================================");
		table.printSums("ContentCount12Add", conditions, condValues);
		table.printSums("ClaimCount12Add", conditions, condValues);
		table.printSums("RebuttalCount12Add", conditions, condValues);
		table.printSums("EvidenceCount12Add", conditions, condValues);
		table.printSums("GeneralCount12Add", conditions, condValues);
		table.printSums("ReasoningCount12Add", conditions, condValues);
		System.out.println("==================================");
		table.printSums("ContentCount12Delete", conditions, condValues);
		table.printSums("ClaimCount12Delete", conditions, condValues);
		table.printSums("RebuttalCount12Delete", conditions, condValues);
		table.printSums("EvidenceCount12Delete", conditions, condValues);
		table.printSums("GeneralCount12Delete", conditions, condValues);
		table.printSums("ReasoningCount12Delete", conditions, condValues);
		System.out.println("==================================");
		table.printSums("ContentCount12Modify", conditions, condValues);
		table.printSums("ClaimCount12Modify", conditions, condValues);
		table.printSums("RebuttalCount12Modify", conditions, condValues);
		table.printSums("EvidenceCount12Modify", conditions, condValues);
		table.printSums("GeneralCount12Modify", conditions, condValues);
		table.printSums("ReasoningCount12Modify", conditions, condValues);
		System.out.println("==================================");
		table.printSums("SurfaceCount12Add", conditions, condValues);
		table.printSums("ClarityCount12Add", conditions, condValues);
		table.printSums("SpellingCount12Add", conditions, condValues);
		table.printSums("OrganizationCount12Add", conditions, condValues);
		table.printSums("PrecisionCount12Add", conditions, condValues);
		System.out.println("==================================");
		table.printSums("SurfaceCount12Delete", conditions, condValues);
		table.printSums("ClarityCount12Delete", conditions, condValues);
		table.printSums("SpellingCount12Delete", conditions, condValues);
		table.printSums("OrganizationCount12Delete", conditions, condValues);
		table.printSums("PrecisionCount12Delete", conditions, condValues);
		System.out.println("==================================");
		table.printSums("SurfaceCount12Modify", conditions, condValues);
		table.printSums("ClarityCount12Modify", conditions, condValues);
		table.printSums("SpellingCount12Modify", conditions, condValues);
		table.printSums("OrganizationCount12Modify", conditions, condValues);
		table.printSums("PrecisionCount12Modify", conditions, condValues);
		
		
		
		System.out.println("================================");
		table.printSums("ContentCount23Add", conditions, condValues);
		table.printSums("ClaimCount23Add", conditions, condValues);
		table.printSums("RebuttalCount23Add", conditions, condValues);
		table.printSums("EvidenceCount23Add", conditions, condValues);
		table.printSums("GeneralCount23Add", conditions, condValues);
		table.printSums("ReasoningCount23Add", conditions, condValues);
		System.out.println("==================================");
		table.printSums("ContentCount23Delete", conditions, condValues);
		table.printSums("ClaimCount23Delete", conditions, condValues);
		table.printSums("RebuttalCount23Delete", conditions, condValues);
		table.printSums("EvidenceCount23Delete", conditions, condValues);
		table.printSums("GeneralCount23Delete", conditions, condValues);
		table.printSums("ReasoningCount23Delete", conditions, condValues);
		System.out.println("==================================");
		table.printSums("ContentCount23Modify", conditions, condValues);
		table.printSums("ClaimCount23Modify", conditions, condValues);
		table.printSums("RebuttalCount23Modify", conditions, condValues);
		table.printSums("EvidenceCount23Modify", conditions, condValues);
		table.printSums("GeneralCount23Modify", conditions, condValues);
		table.printSums("ReasoningCount23Modify", conditions, condValues);
		System.out.println("==================================");
		table.printSums("SurfaceCount23Add", conditions, condValues);
		table.printSums("ClarityCount23Add", conditions, condValues);
		table.printSums("SpellingCount23Add", conditions, condValues);
		table.printSums("OrganizationCount23Add", conditions, condValues);
		table.printSums("PrecisionCount23Add", conditions, condValues);
		System.out.println("==================================");
		table.printSums("SurfaceCount23Delete", conditions, condValues);
		table.printSums("ClarityCount23Delete", conditions, condValues);
		table.printSums("SpellingCount23Delete", conditions, condValues);
		table.printSums("OrganizationCount23Delete", conditions, condValues);
		table.printSums("PrecisionCount23Delete", conditions, condValues);
		System.out.println("==================================");
		table.printSums("SurfaceCount23Modify", conditions, condValues);
		table.printSums("ClarityCount23Modify", conditions, condValues);
		table.printSums("SpellingCount23Modify", conditions, condValues);
		table.printSums("OrganizationCount23Modify", conditions, condValues);
		table.printSums("PrecisionCount23Modify", conditions, condValues);
		
	}

	public static void countBasics(List<String> row, RevisionDocument doc12,
			RevisionDocument doc23) {
		int wordCnt1 = doc12.getOldWordCnt();
		int wordCnt2 = doc12.getNewWordCnt();
		int wordCnt3 = doc23.getNewWordCnt();
		int sentCnt1 = doc12.getOldDraftSentences().size();
		int sentCnt2 = doc12.getNewDraftSentences().size();
		int sentCnt3 = doc23.getNewDraftSentences().size();
		int paraCnt1 = doc12.getOldParagraphNum();
		int paraCnt2 = doc12.getNewParagraphNum();
		int paraCnt3 = doc23.getNewParagraphNum();

		row.add(Integer.toString(wordCnt1));
		row.add(Integer.toString(wordCnt2));
		row.add(Integer.toString(wordCnt3));
		row.add(Integer.toString(sentCnt1));
		row.add(Integer.toString(sentCnt2));
		row.add(Integer.toString(sentCnt3));
		row.add(Integer.toString(paraCnt1));
		row.add(Integer.toString(paraCnt2));
		row.add(Integer.toString(paraCnt3));
	}

	public static void countRevs(List<String> row, RevisionDocument doc12,
			RevisionDocument doc23) {
		countRev(row, doc12);
		countRev(row, doc23);
	}

	public static void countRev(List<String> row, RevisionDocument doc) {
		ArrayList<RevisionUnit> revs = doc.getRoot().getRevisionUnitAtLevel(0);
		int allCnt = 0;
		int contentCnt = 0;
		int surfaceCnt = 0;
		int claimCnt = 0;
		int reasoningCnt = 0;
		int evidenceCnt = 0;
		int rebuttalCnt = 0;
		int generalCnt = 0;
		int precisionCnt = 0;
		int organizationCnt = 0;
		int clarityCnt = 0;
		int spellingCnt = 0;
		
		int allCntModify = 0;
		int contentCntModify = 0;
		int surfaceCntModify = 0;
		int claimCntModify = 0;
		int reasoningCntModify = 0;
		int evidenceCntModify = 0;
		int rebuttalCntModify = 0;
		int generalCntModify = 0;
		int precisionCntModify = 0;
		int organizationCntModify = 0;
		int clarityCntModify = 0;
		int spellingCntModify = 0;
		
		int allCntAdd = 0;
		int contentCntAdd = 0;
		int surfaceCntAdd = 0;
		int claimCntAdd = 0;
		int reasoningCntAdd = 0;
		int evidenceCntAdd = 0;
		int rebuttalCntAdd = 0;
		int generalCntAdd = 0;
		int precisionCntAdd = 0;
		int organizationCntAdd = 0;
		int clarityCntAdd = 0;
		int spellingCntAdd = 0;
		
		int allCntDelete = 0;
		int contentCntDelete = 0;
		int surfaceCntDelete = 0;
		int claimCntDelete = 0;
		int reasoningCntDelete = 0;
		int evidenceCntDelete = 0;
		int rebuttalCntDelete = 0;
		int generalCntDelete = 0;
		int precisionCntDelete = 0;
		int organizationCntDelete = 0;
		int clarityCntDelete = 0;
		int spellingCntDelete = 0;

		for (RevisionUnit rev : revs) {
			allCnt++;
			if (rev.getRevision_purpose() == RevisionPurpose.CLAIMS_IDEAS) {
				claimCnt++;
				contentCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					claimCntAdd++;
					contentCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					claimCntDelete++;
					contentCntDelete++;
					allCntDelete++;
				} else {
					claimCntModify++;
					contentCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.CD_WARRANT_REASONING_BACKING) {
				reasoningCnt++;
				contentCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					reasoningCntAdd++;
					contentCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					reasoningCntDelete++;
					contentCntDelete++;
					allCntDelete++;
				} else {
					reasoningCntModify++;
					contentCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.EVIDENCE) {
				evidenceCnt++;
				contentCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					evidenceCntAdd++;
					contentCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					evidenceCntDelete++;
					contentCntDelete++;
					allCntDelete++;
				} else {
					evidenceCntModify++;
					contentCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.CD_REBUTTAL_RESERVATION) {
				rebuttalCnt++;
				contentCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					rebuttalCntAdd++;
					contentCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					rebuttalCntDelete++;
					contentCntDelete++;
					allCntDelete++;
				} else {
					rebuttalCntModify++;
					contentCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.CD_GENERAL_CONTENT_DEVELOPMENT) {
				generalCnt++;
				contentCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					generalCntAdd++;
					contentCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					generalCntDelete++;
					contentCntDelete++;
					allCntDelete++;
				} else {
					generalCntModify++;
					contentCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.PRECISION) {
				precisionCnt++;
				surfaceCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					precisionCntAdd++;
					contentCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					precisionCntDelete++;
					contentCntDelete++;
					allCntDelete++;
				} else {
					precisionCntModify++;
					contentCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.ORGANIZATION) {
				organizationCnt++;
				surfaceCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					organizationCntAdd++;
					surfaceCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					organizationCntDelete++;
					surfaceCntDelete++;
					allCntDelete++;
				} else {
					organizationCntModify++;
					surfaceCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.WORDUSAGE_CLARITY) {
				clarityCnt++;
				surfaceCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					clarityCntAdd++;
					surfaceCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					clarityCntDelete++;
					surfaceCntDelete++;
					allCntDelete++;
				} else {
					clarityCntModify++;
					surfaceCntModify++;
					allCntModify++;
				}
			} else if (rev.getRevision_purpose() == RevisionPurpose.CONVENTIONS_GRAMMAR_SPELLING) {
				spellingCnt++;
				surfaceCnt++;
				if(rev.getRevision_op() == RevisionOp.ADD) {
					spellingCntAdd++;
					surfaceCntAdd++;
					allCntAdd++;
				} else if(rev.getRevision_op() == RevisionOp.DELETE) {
					spellingCntDelete++;
					surfaceCntDelete++;
					allCntDelete++;
				} else {
					spellingCntModify++;
					surfaceCntModify++;
					allCntModify++;
				}
			}
		}

		row.add(Integer.toString(allCnt));
		row.add(Integer.toString(contentCnt));
		row.add(Integer.toString(surfaceCnt));
		row.add(Integer.toString(claimCnt));
		row.add(Integer.toString(reasoningCnt));
		row.add(Integer.toString(evidenceCnt));
		row.add(Integer.toString(rebuttalCnt));
		row.add(Integer.toString(generalCnt));
		row.add(Integer.toString(precisionCnt));
		row.add(Integer.toString(organizationCnt));
		row.add(Integer.toString(clarityCnt));
		row.add(Integer.toString(spellingCnt));
		
		row.add(Integer.toString(allCntAdd));
		row.add(Integer.toString(contentCntAdd));
		row.add(Integer.toString(surfaceCntAdd));
		row.add(Integer.toString(claimCntAdd));
		row.add(Integer.toString(reasoningCntAdd));
		row.add(Integer.toString(evidenceCntAdd));
		row.add(Integer.toString(rebuttalCntAdd));
		row.add(Integer.toString(generalCntAdd));
		row.add(Integer.toString(precisionCntAdd));
		row.add(Integer.toString(organizationCntAdd));
		row.add(Integer.toString(clarityCntAdd));
		row.add(Integer.toString(spellingCntAdd));
		
		row.add(Integer.toString(allCntDelete));
		row.add(Integer.toString(contentCntDelete));
		row.add(Integer.toString(surfaceCntDelete));
		row.add(Integer.toString(claimCntDelete));
		row.add(Integer.toString(reasoningCntDelete));
		row.add(Integer.toString(evidenceCntDelete));
		row.add(Integer.toString(rebuttalCntDelete));
		row.add(Integer.toString(generalCntDelete));
		row.add(Integer.toString(precisionCntDelete));
		row.add(Integer.toString(organizationCntDelete));
		row.add(Integer.toString(clarityCntDelete));
		row.add(Integer.toString(spellingCntDelete));
		
		row.add(Integer.toString(allCntModify));
		row.add(Integer.toString(contentCntModify));
		row.add(Integer.toString(surfaceCntModify));
		row.add(Integer.toString(claimCntModify));
		row.add(Integer.toString(reasoningCntModify));
		row.add(Integer.toString(evidenceCntModify));
		row.add(Integer.toString(rebuttalCntModify));
		row.add(Integer.toString(generalCntModify));
		row.add(Integer.toString(precisionCntModify));
		row.add(Integer.toString(organizationCntModify));
		row.add(Integer.toString(clarityCntModify));
		row.add(Integer.toString(spellingCntModify));
	}

	public static void countConfirm(List<String> row, ConfirmLog log,
			RevisionDocument doc12) {
		int[] allVals = { RevisionPurpose.CLAIMS_IDEAS,
				RevisionPurpose.CD_WARRANT_REASONING_BACKING,
				RevisionPurpose.CD_REBUTTAL_RESERVATION,
				RevisionPurpose.EVIDENCE,
				RevisionPurpose.CD_GENERAL_CONTENT_DEVELOPMENT,
				RevisionPurpose.PRECISION, RevisionPurpose.ORGANIZATION,
				RevisionPurpose.CONVENTIONS_GRAMMAR_SPELLING,
				RevisionPurpose.WORDUSAGE_CLARITY };
		int len = allVals.length;
		int[][] matrix = new int[len][len];
		HashMap<Integer, Integer> index = new HashMap<Integer, Integer>();
		for (int i = 0; i < len; i++) {
			index.put(allVals[i], i);
		}

		if (log != null) {
			HashMap<Integer, Integer> revOldIndex = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> revNewIndex = new HashMap<Integer, Integer>();
			
			List<ConfirmLogItem> logItems = log.getLogs();
			
			for (ConfirmLogItem item : logItems) {
				int oldIndex = item.getOldSentenceID();
				int newIndex = item.getNewSentenceID();
				
				if(oldIndex != -1) {
					revOldIndex.put(oldIndex, RevisionPurpose.getPurposeIndex(item.getConfirmedType()));
				}
				if(newIndex != -1) {
					revNewIndex.put(newIndex, RevisionPurpose.getPurposeIndex(item.getConfirmedType()));
				}
			}
			
			List<RevisionUnit> revisions = doc12.getRoot().getRevisionUnitAtLevel(0);
			for(RevisionUnit unit: revisions) {
				int purpose = unit.getRevision_purpose();
				ArrayList<Integer> oldIndexes = unit.getOldSentenceIndex();
				ArrayList<Integer> newIndexes = unit.getNewSentenceIndex();
				int selfPurpose = -1;
				for(Integer oldIndex: oldIndexes) {
					if(revOldIndex.containsKey(oldIndex)) {
						selfPurpose = revOldIndex.get(oldIndex);
						break;
					}
				}
				if(selfPurpose == -1) {
					for(Integer newIndex: newIndexes) {
						if(revNewIndex.containsKey(newIndex)) {
							selfPurpose = revNewIndex.get(newIndex);
							break;
						}
					}
				}

				matrix[index.get(selfPurpose)][index.get(purpose)] += 1;
			}
		}
		for (int i = 0; i < allVals.length; i++) {
			for (int j = 0; j < allVals.length; j++) {
				row.add(Integer.toString(matrix[i][j]));
			}
		}
	}

	public static void countConfirmRaw(List<String> row, ConfirmLog log,
			RevisionDocument doc12, RevisionDocument doc23) {
		int contentAgree = 0;
		int contentDisagree = 0;
		int contentDisagreeImplementation = 0;
		int surfaceAgree = 0;
		int surfaceDisagree = 0;
		int surfaceDisagreeImplementation = 0;
		int content2surface = 0;
		int content2surfaceImplementation = 0;
		int surface2content = 0;
		int surface2contentImplementation = 0;
		int deleteDisagrees = 0;
		int allImplementation = 0;

		HashMap<Integer, Integer> revOldIndex = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> revNewIndex = new HashMap<Integer, Integer>();
		
		if (log != null) {
			List<ConfirmLogItem> logItems = log.getLogs();
			for (ConfirmLogItem item : logItems) {
				int oldIndex = item.getOldSentenceID();
				int newIndex = item.getNewSentenceID();
				
				if(oldIndex != -1) {
					revOldIndex.put(oldIndex, RevisionPurpose.getPurposeIndex(item.getConfirmedType()));
				}
				if(newIndex != -1) {
					revNewIndex.put(newIndex, RevisionPurpose.getPurposeIndex(item.getConfirmedType()));
				}
			}
			
			List<RevisionUnit> revisions = doc12.getRoot().getRevisionUnitAtLevel(0);
			for(RevisionUnit unit: revisions) {
				int purpose = unit.getRevision_purpose();
				if(unit.getRevision_op() == RevisionOp.DELETE) deleteDisagrees++;
				else {
					ArrayList<Integer> disagreedIndexes = unit.getNewSentenceIndex();
					int newIndex = disagreedIndexes.get(0);
					if(doc23.getNewFromOld(newIndex)!=null) {
						if(doc23.getPurposeofOld(newIndex)!= RevisionPurpose.NOCHANGE) {
							allImplementation++;
						}
					} else {
						allImplementation++;
					}
				}
				ArrayList<Integer> oldIndexes = unit.getOldSentenceIndex();
				ArrayList<Integer> newIndexes = unit.getNewSentenceIndex();
				int selfPurpose = -1;
				for(Integer oldIndex: oldIndexes) {
					if(revOldIndex.containsKey(oldIndex)) {
						selfPurpose = revOldIndex.get(oldIndex);
						break;
					}
				}
				if(selfPurpose == -1) {
					for(Integer newIndex: newIndexes) {
						if(revNewIndex.containsKey(newIndex)) {
							selfPurpose = revNewIndex.get(newIndex);
							break;
						}
					}
				}
				if(purpose == selfPurpose) {
					if(isContent(purpose)) {
						contentAgree++;
					} else {
						surfaceAgree++;
					}
				} else {
					if(isContent(purpose) && isContent(selfPurpose)) {
						contentDisagree++;
						if(unit.getRevision_op() != RevisionOp.DELETE) {
							ArrayList<Integer> disagreedIndexes = unit.getNewSentenceIndex();
							int newIndex = disagreedIndexes.get(0);
							if(doc23.getNewFromOld(newIndex)!=null) {
								if(doc23.getPurposeofOld(newIndex)!= RevisionPurpose.NOCHANGE) {
									contentDisagreeImplementation++;
								}
							} else {
								contentDisagreeImplementation++;
							}
						}
					} else if(!isContent(purpose) && !isContent(selfPurpose)) {
						surfaceDisagree++;
						if(unit.getRevision_op() != RevisionOp.DELETE) {
							ArrayList<Integer> disagreedIndexes = unit.getNewSentenceIndex();
							int newIndex = disagreedIndexes.get(0);
							if(doc23.getNewFromOld(newIndex)!=null) {
								if(doc23.getPurposeofOld(newIndex)!= RevisionPurpose.NOCHANGE) {
									surfaceDisagreeImplementation++;
								}
							} else {
								surfaceDisagreeImplementation++;
							}
						}
					} else if(isContent(purpose) && !isContent(selfPurpose)) {
						content2surface++;
						if(unit.getRevision_op() != RevisionOp.DELETE) {
							ArrayList<Integer> disagreedIndexes = unit.getNewSentenceIndex();
							int newIndex = disagreedIndexes.get(0);
							if(doc23.getNewFromOld(newIndex)!=null) {
								if(doc23.getPurposeofOld(newIndex)!= RevisionPurpose.NOCHANGE) {
									content2surfaceImplementation++;
								}
							} else {
								content2surfaceImplementation++;
							}
						}
					} else {
						surface2content++;
						if(unit.getRevision_op() != RevisionOp.DELETE) {
							ArrayList<Integer> disagreedIndexes = unit.getNewSentenceIndex();
							int newIndex = disagreedIndexes.get(0);
							if(doc23.getNewFromOld(newIndex)!=null) {
								if(doc23.getPurposeofOld(newIndex)!= RevisionPurpose.NOCHANGE) {
									surface2contentImplementation++;
								}
							} else {
								surface2contentImplementation++;
							}
						}
					}
				}
			}
		}

		row.add(Integer.toString(contentAgree));
		row.add(Integer.toString(contentDisagree));
		row.add(Integer.toString(surfaceAgree));
		row.add(Integer.toString(surfaceDisagree));
		row.add(Integer.toString(content2surface));
		row.add(Integer.toString(surface2content));
		
		row.add(Integer.toString(contentDisagreeImplementation));
		row.add(Integer.toString(surfaceDisagreeImplementation));
		row.add(Integer.toString(content2surfaceImplementation));
		row.add(Integer.toString(surface2contentImplementation));
		row.add(Integer.toString(deleteDisagrees));
		row.add(Integer.toString(allImplementation));
		
	}

		
	public static boolean isContent(int revPurpose) {
		if (revPurpose == RevisionPurpose.CLAIMS_IDEAS
				|| revPurpose == RevisionPurpose.CD_REBUTTAL_RESERVATION
				|| revPurpose == RevisionPurpose.CD_WARRANT_REASONING_BACKING
				|| revPurpose == RevisionPurpose.CD_GENERAL_CONTENT_DEVELOPMENT
				|| revPurpose == RevisionPurpose.EVIDENCE)
			return true;
		return false;
	}

	public static Table analyze(HashMap<String, ConfirmLog[]> logMap,
			HashMap<String, RevisionDocument> docs12,
			HashMap<String, RevisionDocument> docs23, boolean isExperiment) {
		Table table = new Table("EagerStatistics");
		table.addColumn("ID");
		table.addColumn("Language");
		table.addColumn("WordCnt1");
		table.addColumn("WordCnt2");
		table.addColumn("WordCnt3");
		table.addColumn("SentCnt1");
		table.addColumn("SentCnt2");
		table.addColumn("SentCnt3");
		table.addColumn("ParaCnt1");
		table.addColumn("ParaCnt2");
		table.addColumn("ParaCnt3");

		table.addColumn("RevCount12");
		table.addColumn("ContentCount12");
		table.addColumn("SurfaceCount12");

		table.addColumn("ClaimCount12");
		table.addColumn("ReasoningCount12");
		table.addColumn("EvidenceCount12");
		table.addColumn("RebuttalCount12");
		table.addColumn("GeneralCount12");
		table.addColumn("PrecisionCount12");
		table.addColumn("OrganizationCount12");
		table.addColumn("ClarityCount12");
		table.addColumn("SpellingCount12");
		
		table.addColumn("RevCount12Add");
		table.addColumn("ContentCount12Add");
		table.addColumn("SurfaceCount12Add");

		table.addColumn("ClaimCount12Add");
		table.addColumn("ReasoningCount12Add");
		table.addColumn("EvidenceCount12Add");
		table.addColumn("RebuttalCount12Add");
		table.addColumn("GeneralCount12Add");
		table.addColumn("PrecisionCount12Add");
		table.addColumn("OrganizationCount12Add");
		table.addColumn("ClarityCount12Add");
		table.addColumn("SpellingCount12Add");
		
		table.addColumn("RevCount12Delete");
		table.addColumn("ContentCount12Delete");
		table.addColumn("SurfaceCount12Delete");

		table.addColumn("ClaimCount12Delete");
		table.addColumn("ReasoningCount12Delete");
		table.addColumn("EvidenceCount12Delete");
		table.addColumn("RebuttalCount12Delete");
		table.addColumn("GeneralCount12Delete");
		table.addColumn("PrecisionCount12Delete");
		table.addColumn("OrganizationCount12Delete");
		table.addColumn("ClarityCount12Delete");
		table.addColumn("SpellingCount12Delete");

		table.addColumn("RevCount12Modify");
		table.addColumn("ContentCount12Modify");
		table.addColumn("SurfaceCount12Modify");

		table.addColumn("ClaimCount12Modify");
		table.addColumn("ReasoningCount12Modify");
		table.addColumn("EvidenceCount12Modify");
		table.addColumn("RebuttalCount12Modify");
		table.addColumn("GeneralCount12Modify");
		table.addColumn("PrecisionCount12Modify");
		table.addColumn("OrganizationCount12Modify");
		table.addColumn("ClarityCount12Modify");
		table.addColumn("SpellingCount12Modify");
		
		table.addColumn("RevCount23");

		table.addColumn("ContentCount23");
		table.addColumn("SurfaceCount23");
		table.addColumn("ClaimCount23");
		table.addColumn("ReasoningCount23");
		table.addColumn("EvidenceCount23");
		table.addColumn("RebuttalCount23");
		table.addColumn("GeneralCount23");
		table.addColumn("PrecisionCount23");
		table.addColumn("OrganizationCount23");
		table.addColumn("ClarityCount23");
		table.addColumn("SpellingCount23");

		
		table.addColumn("RevCount23Add");
		table.addColumn("ContentCount23Add");
		table.addColumn("SurfaceCount23Add");

		table.addColumn("ClaimCount23Add");
		table.addColumn("ReasoningCount23Add");
		table.addColumn("EvidenceCount23Add");
		table.addColumn("RebuttalCount23Add");
		table.addColumn("GeneralCount23Add");
		table.addColumn("PrecisionCount23Add");
		table.addColumn("OrganizationCount23Add");
		table.addColumn("ClarityCount23Add");
		table.addColumn("SpellingCount23Add");
		
		table.addColumn("RevCount23Delete");
		table.addColumn("ContentCount23Delete");
		table.addColumn("SurfaceCount23Delete");

		table.addColumn("ClaimCount23Delete");
		table.addColumn("ReasoningCount23Delete");
		table.addColumn("EvidenceCount23Delete");
		table.addColumn("RebuttalCount23Delete");
		table.addColumn("GeneralCount23Delete");
		table.addColumn("PrecisionCount23Delete");
		table.addColumn("OrganizationCount23Delete");
		table.addColumn("ClarityCount23Delete");
		table.addColumn("SpellingCount23Delete");

		table.addColumn("RevCount23Modify");
		table.addColumn("ContentCount23Modify");
		table.addColumn("SurfaceCount23Modify");

		table.addColumn("ClaimCount23Modify");
		table.addColumn("ReasoningCount23Modify");
		table.addColumn("EvidenceCount23Modify");
		table.addColumn("RebuttalCount23Modify");
		table.addColumn("GeneralCount23Modify");
		table.addColumn("PrecisionCount23Modify");
		table.addColumn("OrganizationCount23Modify");
		table.addColumn("ClarityCount23Modify");
		table.addColumn("SpellingCount23Modify");
		
		int[] allVals = { RevisionPurpose.CLAIMS_IDEAS,
				RevisionPurpose.CD_WARRANT_REASONING_BACKING,
				RevisionPurpose.CD_REBUTTAL_RESERVATION,
				RevisionPurpose.EVIDENCE,
				RevisionPurpose.CD_GENERAL_CONTENT_DEVELOPMENT,
				RevisionPurpose.PRECISION, RevisionPurpose.ORGANIZATION,
				RevisionPurpose.CONVENTIONS_GRAMMAR_SPELLING,
				RevisionPurpose.WORDUSAGE_CLARITY };
		for (int i = 0; i < allVals.length; i++) {
			for (int j = 0; j < allVals.length; j++) {
				String rev1 = RevisionPurpose.getPurposeName(allVals[i]);
				String rev2 = RevisionPurpose.getPurposeName(allVals[j]);
				String tag = "SELF_" + rev1 + "TO_" + rev2;
				table.addColumn(tag);
			}
		}

		table.addColumn("Within Content Agree");
		table.addColumn("Within Content Disagree");
		table.addColumn("Within Surface Agree");
		table.addColumn("Within Surface Disagree");
		table.addColumn("Content to Surface");
		table.addColumn("Surface to Content");
		
		table.addColumn("Within Content Disagree Implementation");
		table.addColumn("Within Surface Disagree Implementation");
		table.addColumn("Content to Surface Implementation");
		table.addColumn("Surface to Content Implementation");
		table.addColumn("Delete Disagree Counts");
		table.addColumn("All Implementation");

		Iterator<String> it = docs12.keySet().iterator();
		while (it.hasNext()) {
			String userId = it.next();
			RevisionDocument doc12 = docs12.get(userId);
			RevisionDocument doc23 = docs23.get(userId);

			ConfirmLog oldLog = null;
			ConfirmLog newLog = null;
			ConfirmLog[] logs = logMap.get(userId);
			if (logs != null) {
				oldLog = logs[0];
				newLog = logs[1];
			}

			if (isExperiment && logs != null) {
				List<String> row = new ArrayList<String>();
				row.add(userId);
				if(userId.startsWith("native")) {
					row.add("1");
				} else {
					row.add("0");
				}
				countBasics(row, doc12, doc23);
				countRevs(row, doc12, doc23);
				countConfirm(row, oldLog, doc12);
				countConfirmRaw(row, oldLog, doc12, doc23);
				table.addRow(row);
			} else if (!isExperiment && logs == null) {
				List<String> row = new ArrayList<String>();
				row.add(userId);
				if(userId.startsWith("native")) {
					row.add("1");
				} else {
					row.add("0");
				}
				countBasics(row, doc12, doc23);
				countRevs(row, doc12, doc23);
				countConfirm(row, oldLog, doc12);
				countConfirmRaw(row, oldLog, doc12, doc23);
				table.addRow(row);
			}
		}

		addRatios(table);
		return table;
	}

	public static void addRatios(Table table)  {
		table.addNormalize("SentCnt1", "RevCount12", "RevCount12Normalized");
		table.addNormalize("SentCnt2", "RevCount23", "RevCount23Normalized");
		table.addNormalize("SentCnt1", "ContentCount12", "Content12Normalized");
		table.addNormalize("SentCnt1", "SurfaceCount12", "Surface12Normalized");
		table.addNormalize("SentCnt2", "ContentCount23", "Content23Normalized");
		table.addNormalize("SentCnt2", "SurfaceCount23", "Surface23Normalized");
		
		table.addNormalize("RevCount12", "ContentCount12", "Content12Ratio");
		table.addNormalize("RevCount12", "SurfaceCount12", "Surface12Ratio");
		table.addNormalize("RevCount23", "ContentCount23", "Content23Ratio");
		table.addNormalize("RevCount23", "SurfaceCount23", "Surface23Ratio");
		
		table.addNormalize("ContentCount12", "Within Content Agree", "WithinContentAgreeRatio");
		table.addNormalize("ContentCount12", "Within Content Disagree", "WithinContentDisagreeRatio");
		table.addNormalize("SurfaceCount12", "Within Surface Agree", "WithinSurfaceAgreeRatio");
		table.addNormalize("SurfaceCount12", "Within Surface Disagree", "WithinSurfaceDisagreeRatio");
		table.addNormalize("ContentCount12", "Content to Surface", "ContentToSurfaceRatio");
		table.addNormalize("SurfaceCount12", "Surface to Content", "SurfaceToContentRatio");
	}
	
	public static List<HashMap<String, RevisionDocument>> readDocuments(
			List<String> paths12, List<String> paths23) throws Exception {
		HashMap<String, RevisionDocument> docs12 = new HashMap<String, RevisionDocument>();
		HashMap<String, RevisionDocument> docs23 = new HashMap<String, RevisionDocument>();

		List<RevisionDocument> docs12List = new ArrayList<RevisionDocument>();
		List<RevisionDocument> docs23List = new ArrayList<RevisionDocument>();
		for (String path12 : paths12) {
			docs12List.addAll(RevisionDocumentReader.readDocs(path12));
		}
		for (String path23 : paths23) {
			docs23List.addAll(RevisionDocumentReader.readDocs(path23));
		}

		for (RevisionDocument doc : docs12List) {
			String docName = doc.getDocumentName();
			String[] splits = docName.split("_");
			String idName = "";
			for (String split : splits) {
				if (split.startsWith("esl") || split.startsWith("native")) {
					idName = split;
					break;
				}
			}
			idName = idName.replace(".txt", "");
			idName = idName.replace(".xlsx", "");
			docs12.put(idName, doc);
		}

		for (RevisionDocument doc : docs23List) {
			String docName = doc.getDocumentName();
			String[] splits = docName.split("_");
			String idName = "";
			for (String split : splits) {
				if (split.startsWith("esl") || split.startsWith("native")) {
					idName = split;
					break;
				}
			}
			idName = idName.replace(".txt", "");
			idName = idName.replace(".xlsx", "");
			docs23.put(idName, doc);
		}

		List<HashMap<String, RevisionDocument>> lists = new ArrayList<HashMap<String, RevisionDocument>>();
		lists.add(docs12);
		lists.add(docs23);
		return lists;
	}
}
