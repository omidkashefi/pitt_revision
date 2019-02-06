package edu.pitt.lrdc.cs.revision.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.pitt.lrdc.cs.revision.model.ConfirmationItem;
import edu.pitt.lrdc.cs.revision.model.RevisionConfirmation;
import edu.pitt.lrdc.cs.revision.model.RevisionDocument;

class RatingObj {
	int rating;
	boolean visualization;
	boolean difference;
	boolean taxonomy;
	boolean listing;
	String other;

	public String toString() {
		return rating + ", visualization:" + visualization + ", difference:"
				+ difference + ", taxonomy:" + taxonomy + ", listing: "
				+ listing + ", other:" + other;
	}
}

public class RatingReader {
	public static void main(String[] args) throws Exception {
		String ratingPath = "C:\\Not Backed Up\\data\\eagerstudy\\ratingStudy\\rating";
		String confirm12Path = "C:\\Not Backed Up\\data\\eagerstudy\\ratingStudy\\confirm12";
		String confirm23Path = "C:\\Not Backed Up\\data\\eagerstudy\\ratingStudy\\confirm23";
		String revision12Path = "C:\\Not Backed Up\\data\\eagerstudy\\ratingStudy\\d1d2";
		String revision23Path = "C:\\Not Backed Up\\data\\eagerstudy\\ratingStudy\\d2d3";

		ArrayList<RevisionDocument> docs12 = RevisionDocumentReader
				.readDocs(revision12Path);
		ArrayList<RevisionDocument> docs23 = RevisionDocumentReader
				.readDocs(revision23Path);
		List<RevisionConfirmation> confirmations = readConfirmations(confirm12Path);
		List<RevisionConfirmation> confirmations2 = readConfirmations(confirm23Path);

		HashMap<String, RatingObj> ratings = readSystemRatings(ratingPath);
		for (String key : ratings.keySet()) {
			System.out.println(key);
			System.out.println(ratings.get(key));
		}

		for (RevisionDocument doc : docs12) {
			String name = doc.getDocumentName();
			String trimmedName = new File(name).getName();
			trimmedName = trimmedName.replace(".txt.xlsx", ".log");

			for (RevisionConfirmation confirm : confirmations) {
				if (confirm.getDocName().contains(trimmedName)) {
					System.out.println(doc.getDocumentName());
					System.out.println("DIFF: " + confirm.getDiff(doc));
				}
			}
		}
		
		for (RevisionDocument doc : docs23) {
			String name = doc.getDocumentName();
			String trimmedName = new File(name).getName();
			trimmedName = trimmedName.replace(".txt.xlsx", ".log");

			for (RevisionConfirmation confirm : confirmations2) {
				if (confirm.getDocName().contains(trimmedName)) {
					System.out.println(doc.getDocumentName());
					System.out.println("DIFF: " + confirm.getDiff(doc));
				}
			}
		}
		
		for(RevisionConfirmation confirm : confirmations2) {
			System.out.println(confirm.getDocName());
			HashMap<String, Integer> motivationCnts = confirm.getMotivationCounts();
			for(String key: motivationCnts.keySet()) {
				System.out.println(key+": " + motivationCnts.get(key));
			}
		}

	}

	public static List<RevisionConfirmation> readConfirmations(String path)
			throws IOException {
		List<RevisionConfirmation> confirmations = new ArrayList<RevisionConfirmation>();
		File folder = new File(path);
		File[] subs = folder.listFiles();
		for (File sub : subs) {
			String docName = sub.getName();
			RevisionConfirmation confirmation = readConfirmation(sub
					.getAbsolutePath());
			confirmations.add(confirmation);
		}
		return confirmations;
	}

	public static HashMap<String, RatingObj> readSystemRatings(String path)
			throws IOException {
		HashMap<String, RatingObj> ratings = new HashMap<String, RatingObj>();
		File folder = new File(path);
		File[] subs = folder.listFiles();
		for (File sub : subs) {
			String docName = sub.getName();
			RatingObj rating = readRatingStr(sub.getAbsolutePath());
			ratings.put(docName, rating);
		}
		return ratings;
	}

	public static RevisionConfirmation readConfirmation(String path)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String str = "";
		String line = reader.readLine();
		while (line != null) {
			str += line;
			line = reader.readLine();
		}
		reader.close();

		RevisionConfirmation rc = new RevisionConfirmation();
		rc.setDocName(path);
		JSONArray jsonarray = new JSONArray(str);
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jsonobject = jsonarray.getJSONObject(i);
			ConfirmationItem item = new ConfirmationItem();
			String key = jsonobject.getString("key");
			String confirmedPurpose = jsonobject.getString("type");
			String motivation = jsonobject.getString("motivation");

			/*
			 * String[] indices = key.split("-"); int oldSentenceIndex =
			 * Integer.parseInt(indices[0]); int newSentenceIndex =
			 * Integer.parseInt(indices[1]);
			 */
			int[] indices = splitIndices(key);
			int oldSentenceIndex = indices[0];
			int newSentenceIndex = indices[1];

			item.setMotivation(motivation);
			item.setConfirmedPurpose(confirmedPurpose);
			item.setOldSentenceIndex(oldSentenceIndex);
			item.setNewSentenceIndex(newSentenceIndex);
			rc.addItem(item);
		}
		return rc;
	}

	public static int[] splitIndices(String str) {
		Queue<Character> q = new LinkedList<Character>();
		int[] indices = new int[2];
		int index = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '-') {
				if (q.size() == 0) {
					q.add(c);
				} else {
					StringBuffer sb = new StringBuffer();
					while (q.size() > 0) {
						sb.append(q.poll());
					}
					int val = Integer.parseInt(sb.toString());
					indices[index] = val;
					index++;
				}
			} else {
				q.add(c);
			}
		}

		StringBuffer sb = new StringBuffer();
		while (q.size() > 0) {
			sb.append(q.poll());
		}
		int val = Integer.parseInt(sb.toString());
		indices[index] = val;

		return indices;
	}

	public static RatingObj readRatingStr(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String str = "";
		String line = reader.readLine();
		while (line != null) {
			str += line;
			line = reader.readLine();
		}
		reader.close();

		JSONObject jsonobject = new JSONObject(str);

		RatingObj obj = new RatingObj();
		obj.rating = jsonobject.getInt("rating");
		obj.visualization = jsonobject.getBoolean("visualization");
		obj.difference = jsonobject.getBoolean("difference");
		obj.taxonomy = jsonobject.getBoolean("taxonomy");
		obj.listing = jsonobject.getBoolean("listing");
		obj.other = jsonobject.getString("otherMotivation");

		return obj;
	}
}
