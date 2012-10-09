package pl.edu.agh.adminmanager.monitor.hdd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.agent.Client.PostCallback;
import pl.edu.agh.adminmanager.monitor.BaseMonitor;

@Component
public class HddSpaceMonitor extends BaseMonitor {

	// don't use if not necessary
	@PostConstruct
	@Override
	public void init() {
		setResponceCallback(new PostCallback() {

			public void handle(JSONObject json) {
				// handle server response here
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
	public Object execute() {

		List<JSONObject> list = new ArrayList<JSONObject>();
		for (File root : Arrays.asList(File.listRoots())) {
			JSONObject item = new JSONObject();
			try {
				item.append("time", System.currentTimeMillis() / 1000L);
				JSONObject data = new JSONObject();
				data.append("drive", root.toString());
				data.append("free_space", root.getFreeSpace());
				data.append("total_space", root.getTotalSpace());
				data.append("usable_space", root.getUsableSpace());
				item.append("data", data);
				list.add(item);
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		JSONObject toSend = new JSONObject();
		try {
			JSONArray jsonList = new JSONArray(list);
			toSend.append("lista", jsonList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toSend;
	}

}
