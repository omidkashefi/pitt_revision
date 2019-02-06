package edu.pitt.lrdc.cs.revision.statistics;

import java.util.List;
import java.util.ArrayList;

/**
 * Table structure for output the statistics
 * 
 * @author zhangfan
 *
 */
public class Table {
	private String tableName;
	private List<String> columns;
	private List<List<String>> rows;

	public int getColumnIndex(String colName) {
		int index = -1;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(colName)) {
				index = i;
				return index;
			}
		}
		return index;
	}
	
	public void addControlGroup(Table t) {
		columns.add("Group");
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).add("1");
		}
		List<List<String>> tRows = t.getRows();
		for(List<String> tRow: tRows) {
			tRow.add("2");
			rows.add(tRow);
		}
	}
	
	public Table() {
		columns = new ArrayList<String>();
		rows = new ArrayList<List<String>>();
	}

	/**
	 * Minus, invoke after constructing the table
	 * @param c1
	 * @param c2
	 * @param newColName
	 */
	public void addMinus(String c1, String c2, String newColName) {
		int c1Index = -1;
		int c2Index = -1;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(c1)) {
				c1Index = i;
			} else if(columns.get(i).equals(c2)) {
				c2Index = i;
			}
		}
		columns.add(newColName);
		for(int i = 0; i < rows.size(); i++) {
			List<String> row = rows.get(i);
			int c1Val = Integer.parseInt(row.get(c1Index));
			int c2Val = Integer.parseInt(row.get(c2Index));
			int diff = c2Val - c1Val;
			row.add(Integer.toString(diff));
		}
	}
	
	public void printSums(String sumPropName, List<String> conditions, List<String> condValues) {
		int index = getColumnIndex(sumPropName);
		
		String line = "";
		for(int i = 0; i < conditions.size(); i++) {
			line += conditions.get(i) + ":" + condValues.get(i) + "\t";
		}
		
		double val = 0;
		List<Integer> condIndices = new ArrayList<Integer>();
		for(String condition: conditions) {
			condIndices.add(getColumnIndex(condition));
		}
		for(int i = 0; i < rows.size(); i++) {
			boolean isGroup = true;
			List<String> row = rows.get(i);
			for(int j = 0; j < condValues.size(); j++) {
				int condIndex = condIndices.get(j);
				if(!row.get(condIndex).equals(condValues.get(j))) {
					isGroup = false;
				}
			}
			if(isGroup) {
				val += Integer.parseInt(row.get(index));
			}
		}
		System.out.println(sumPropName + "\n" + line + "\n" + val);
	}
	
	/**
	 * normalize, invoke after constructing the table
	 * @param baseColName
	 * @param normalizeColName
	 */
	public void addNormalize(String baseColName, String divideColName, String newColName) {
		int baseIndex = -1;
		int divideIndex = -1;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(baseColName)) {
				baseIndex = i;
			} else if(columns.get(i).equals(divideColName)) {
				divideIndex = i;
			}
		}
		columns.add(newColName);
		for(int i = 0; i < rows.size(); i++) {
			List<String> row = rows.get(i);
			int baseVal = Integer.parseInt(row.get(baseIndex));
			int divideVal = Integer.parseInt(row.get(divideIndex));
			double ratio = divideVal * 1.0 / baseVal;
			if(baseVal == 0) ratio = 0;
			row.add(Double.toString(ratio));
		}
	}
	
	public Table(String name) {
		this.tableName = name;
		columns = new ArrayList<String>();
		rows = new ArrayList<List<String>>();
	}

	public String getTableName() {
		return tableName;
	}

	public List<List<String>> getRows() {
		return this.rows;
	}

	public List<String> getColumns() {
		return this.columns;
	}

	public void addRow(List<String> row) {
		this.rows.add(row);
	}
	
	public void addColumn(String columnName) {
		this.columns.add(columnName);
	}
}
