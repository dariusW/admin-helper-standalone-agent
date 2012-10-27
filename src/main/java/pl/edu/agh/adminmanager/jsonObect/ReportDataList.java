package pl.edu.agh.adminmanager.jsonObect;

import java.util.ArrayList;
import java.util.List;

public class ReportDataList extends JsonData{
	private List<ReportData> list = new ArrayList<ReportData>();

	public List<ReportData> getList() {
		return list;
	}

	public void setList(List<ReportData> list) {
		this.list = list;
	}

}