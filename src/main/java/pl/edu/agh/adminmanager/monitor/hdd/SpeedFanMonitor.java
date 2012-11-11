package pl.edu.agh.adminmanager.monitor.hdd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.agent.Client.PostCallback;
import pl.edu.agh.adminmanager.jsonObect.JsonData;
import pl.edu.agh.adminmanager.jsonObect.ReportData;
import pl.edu.agh.adminmanager.jsonObect.ReportDataList;
import pl.edu.agh.adminmanager.jsonObect.ReportResponce;
import pl.edu.agh.adminmanager.monitor.BaseMonitor;

@Component
public class SpeedFanMonitor extends BaseMonitor{

	private static Logger log = Logger.getLogger(SpeedFanMonitor.class.getName());

	private static String SFLOG = "SFLog";
	
	private static String SFLOG_EXT = ".csv";
	
	private static String path;
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	// don't use if not necessary
	@Override
	public void init() {
		path = getContext().getProperty("speed.fan.monitor.logpath");
		
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
		
		if(path!=null){

			String filePath = path+SFLOG+dateFormat.format(new Date())+SFLOG_EXT;
			
			String firstLine = "";
			String lastLine = "";
			
			try {
				ReportData item = new ReportData();
				item.setTable("speedfan");
				BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));

				System.out.println(firstLine);
				String tmpLine;
				firstLine = reader.readLine();
				while((tmpLine = reader.readLine()) != null)
					lastLine = tmpLine;

				List<String> tags = Arrays.asList(firstLine.split("\t"));
				List<String> values = Arrays.asList(lastLine.split("\t"));
				
				for(int idx = 0; idx < tags.size(); idx++){
					item.getData().put(tags.get(idx), values.get(idx));
				}
				list.getList().add(item);
				
				
			} catch (FileNotFoundException e) {
				enabled=false;
				log.error("NO LOG FILE EXISTS "+e);
				
			} catch (IOException e) {
				enabled=false;
				log.error(e);
			}
			
			
		}
		
		
		return list;
	}

}
