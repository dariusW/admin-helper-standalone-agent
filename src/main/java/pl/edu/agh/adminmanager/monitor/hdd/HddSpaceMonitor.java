package pl.edu.agh.adminmanager.monitor.hdd;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.agent.Client.PostCallback;
import pl.edu.agh.adminmanager.jsonObect.ReportData;
import pl.edu.agh.adminmanager.jsonObect.ReportDataList;
import pl.edu.agh.adminmanager.jsonObect.ReportResponce;
import pl.edu.agh.adminmanager.monitor.BaseMonitor;

@Component
public class HddSpaceMonitor extends BaseMonitor {
	

	private static Logger log = Logger.getLogger(ReportResponce.class.getName());

	// don't use if not necessary
	@Override
	public void init() {
		setResponceCallback(new PostCallback() {

			public void handle(String json) {
				ReportResponce responce = fromJson(json, ReportResponce.class);
				log.info(responce);		
			}
		});
		// important to register monitor
		super.init();
	}

	/**
	 * this method is executed on every monitor scan
	 * 
	 * example:
	 * 
	 * POST /store { "time": 1349460704, "table": "temperatury" "data": <dane> }
	 * 
	 * lub
	 * 
	 * POST /store { "list": [ { "time": 1349460704, "data": <dane> }, { "time":
	 * 1349460708, "data": <dane> }, { "time": 1349460711, "data": <dane> } ] }
	 * 
	 * @return JSONArray || JSONObject with data
	 */
	@Override
	public ReportDataList execute() {

		ReportDataList list = new ReportDataList();
		for (File root : Arrays.asList(File.listRoots())) {
			ReportData item = new ReportData();
			item.setTable("hdd_space");
			item.getData().put("drive", root.toString().replace("\\", ""));
			item.getData()
					.put("free_space", Long.toString(root.getFreeSpace()));
			item.getData().put("total_space",
					Long.toString(root.getTotalSpace()));
			item.getData().put("usable_space",
					Long.toString(root.getUsableSpace()));
			list.getList().add(item);

		}
		return list;
	}
}
