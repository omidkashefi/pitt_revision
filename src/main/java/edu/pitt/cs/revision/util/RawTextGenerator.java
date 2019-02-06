package edu.pitt.cs.revision.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.File;

import edu.pitt.lrdc.cs.revision.io.RevisionDocumentReader;
import edu.pitt.lrdc.cs.revision.model.RevisionDocument;

public class RawTextGenerator {
	public static void main(String[] args) throws Exception {
		String srcFolder1 = "C:\\Not Backed Up\\data\\naaclData\\C1";
		String srcFolder2 = "C:\\Not Backed Up\\data\\naaclData\\C2";
		
		String dstFolder1 = "C:\\Not Backed Up\\data\\raw_txt\\C1";
		String dstFolder2 = "C:\\Not Backed Up\\data\\raw_txt\\C2";
		
		extractRawTxt(srcFolder1, dstFolder1);
		extractRawTxt(srcFolder2, dstFolder2);
	}
	
	public static void extractRawTxt(String srcFolder, String dstFolder) throws Exception {
		String listingFile = dstFolder+"/"+"fileList.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(listingFile));
		ArrayList<RevisionDocument> docs = RevisionDocumentReader.readDocs(srcFolder);
		for(RevisionDocument doc: docs) {
			String rawfileName = doc.getDocumentName();
			File f = new File(rawfileName);
			String trimmedName = f.getName();
			trimmedName = trimmedName.replace(" Christian", "");
			trimmedName = trimmedName.replace(" Fan", "");
			trimmedName = trimmedName.replace(" Fian", "");
			String d1Path = dstFolder + "/draft1/"+trimmedName+".txt";
			String d2Path = dstFolder + "/draft2/"+trimmedName+".txt";
			
			BufferedWriter d1Writer = new BufferedWriter(new FileWriter(d1Path));
			BufferedWriter d2Writer = new BufferedWriter(new FileWriter(d2Path));
			
			String d1Txt = "";
			String d2Txt = "";
			
			for(int i = 1;i<=doc.getOldParagraphNum();i++) {
				String line = "";
				for(int j = doc.getFirstOfOldParagraph(i);j<=doc.getLastOfOldParagraph(i);j++) {
					String sentence = doc.getOldSentence(j);
					line+= sentence;
				}
				line += "\n\n";
				d1Txt += line;
			}
			
			for(int i = 1;i<=doc.getNewParagraphNum();i++) {
				String line = "";
				for(int j = doc.getFirstOfNewParagraph(i);j<=doc.getLastOfNewParagraph(i);j++) {
					String sentence = doc.getNewSentence(j);
					line+= sentence;
				}
				line += "\n\n";
				d2Txt += line;
			}
			d1Writer.write(d1Txt);
			d2Writer.write(d2Txt);
			d1Writer.close();
			d2Writer.close();
			
			writer.write("PLACEHOLDER"+ "/draft1/"+trimmedName+".txt\n");
			writer.write("PLACEHOLDER"+ "/draft2/"+trimmedName+".txt\n");
		}
		writer.close();
	}
}
