package com.codinghero.sql.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBResult implements DBLoadable {
	
	private List<String> columnLabelList = new ArrayList<String>();
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public String toDBLineString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < columnLabelList.size() - 1; i++) {
			sb.append(data.get(columnLabelList.get(i)).toString());
			sb.append(DBLoadable.FIELDS_TERMINATED);
		}
		int lastIndex = columnLabelList.size() - 1;
		sb.append(data.get(columnLabelList.get(lastIndex)).toString());
		sb.append(DBLoadable.LINES_TERMINATED);
		
		return sb.toString();
	}

	public Object putData(String key, Object value) {
		return data.put(key, value);
	}

	public Object getData(String key) {
		return data.get(key);
	}

	public boolean addColumnLabel(String columnLabel) {
		return columnLabelList.add(columnLabel);
	}

	public String getColumnLabel(int columnIndex) {
		return columnLabelList.get(columnIndex);
	}
}
