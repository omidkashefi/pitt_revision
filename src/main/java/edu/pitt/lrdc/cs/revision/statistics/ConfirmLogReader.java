package edu.pitt.lrdc.cs.revision.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConfirmLogReader {
	public static void main(String[] args) throws IOException {
		List<String> paths = new ArrayList<String>();
		paths.add("C:\\Not Backed Up\\data\\eagerstudy\\ESL\\Experiment\\Confirmation");
		paths.add("C:\\Not Backed Up\\data\\eagerstudy\\Native\\Experiment\\Confirmation");
		HashMap<String, ConfirmLog[]> logMap = readLogs(paths);
	}
	
	public static HashMap<String, ConfirmLog[]> readLogs(List<String> paths) throws IOException {
		HashMap<String, ConfirmLog[]> logMap = new HashMap<String, ConfirmLog[]>();
		for(String path: paths) {
			File folder = new File(path);
			File[] subs = folder.listFiles();
			for(File sub: subs) {
				BufferedReader reader = new BufferedReader(new FileReader(sub));
				String fileName = sub.getName();
				fileName = fileName.replaceAll(".log", "");
				String[] tags = fileName.split("-");
				String userId = tags[0];
				String version = tags[1];
				
				if(!logMap.containsKey(userId)) {
					ConfirmLog[] logs = new ConfirmLog[2];
					logMap.put(userId, logs);
				}

				ConfirmLog log;
				if(version.equals("1")) {
					log = new ConfirmLog(userId, false);
					logMap.get(userId)[0] = log;
				} else {
					log = new ConfirmLog(userId, true);
					logMap.get(userId)[1] = log;
				}
				StringBuilder sb = new StringBuilder();
		        String line = reader.readLine();
		        while (line != null) {
		            sb.append(line);
		            line = reader.readLine();
		        }
		        reader.close();
		        String result = sb.toString();
		        JSONArray jobj = new JSONArray(result);
		        for(int i = 0; i < jobj.length(); i++) {
		        	JSONObject obj = (JSONObject)jobj.get(i);
		        	String key = obj.getString("key");
		        	String type = obj.getString("type");
		        	String motivation = obj.getString("motivation");
		        	
		        	int oldIndex = -1;
		        	int newIndex = -1;
		        	if(key.startsWith("-")) {
		        		newIndex = Integer.parseInt(key.substring(key.lastIndexOf("-")+1));
		        	} else {
		        		oldIndex = Integer.parseInt(key.substring(0, key.indexOf("-")));
		        		newIndex = Integer.parseInt(key.substring(key.indexOf("-")+1));
		        	}
		        	ConfirmLogItem item = new ConfirmLogItem();
		        	item.setConfirmedType(type);
		        	item.setMotivateReason(motivation);
		        	item.setOldSentenceID(oldIndex);
		        	item.setNewSentenceID(newIndex);
		        	log.addLog(item);
		        }
			}
		}
		
		return logMap;
	}
}
