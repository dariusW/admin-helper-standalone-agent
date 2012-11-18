package pl.edu.agh.adminmanager.monitor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.agent.Client.PostCallback;
import pl.edu.agh.adminmanager.jsonObect.JsonData;
import pl.edu.agh.adminmanager.jsonObect.ReportData;
import pl.edu.agh.adminmanager.jsonObect.ReportDataList;
import pl.edu.agh.adminmanager.jsonObect.ReportResponce;

@Component
public class HddTempMonitor extends BaseMonitor {

	private static Logger log = Logger.getLogger(HddTempMonitor.class.getName());

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

	@Override
	public JsonData execute() {
		ReportDataList list = new ReportDataList();
		if (!System.getProperty("os.name").startsWith("Windows"))
			return list;
		try {
			ReportData item = new ReportData(getContext());
			item.setTable("hdd_temp");
			List<String> result;
			if(Integer.parseInt(System.getProperty("sun.arch.data.model"))==64){
				result = execExternalMonitor("agents/DiskSmart64.exe");
			} else {
				result = execExternalMonitor("agents/DiskSmart.exe");
			}
				
			for (String line : result) {
				List<String> p = Arrays.asList(line.split("="));
				if (p.size() >= 2) {
					item.getData().put(p.get(0), p.get(1));
				}
			}
			list.getList().add(item);

		} catch (IOException e) {
			log.error(e);
		} catch (InterruptedException e) {
			log.error(e);
		}
		return list;
	}

}
