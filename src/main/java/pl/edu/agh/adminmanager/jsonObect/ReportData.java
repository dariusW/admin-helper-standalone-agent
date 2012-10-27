package pl.edu.agh.adminmanager.jsonObect;

import java.util.Map;
import java.util.TreeMap;

public class ReportData extends JsonData{
	private String table;
	private long time = System.currentTimeMillis() / 1000L;
	private Map<String, String> data = new TreeMap<String, String>();
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}

}