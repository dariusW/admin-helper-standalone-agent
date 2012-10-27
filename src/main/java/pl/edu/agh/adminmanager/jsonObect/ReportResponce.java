package pl.edu.agh.adminmanager.jsonObect;

public class ReportResponce extends JsonData {
	private String records_stored;
	private String error;
	private String table;

	public String getRecordsStored() {
		return records_stored;
	}

	public void setRecordsStored(String records_stored) {
		this.records_stored = records_stored;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
