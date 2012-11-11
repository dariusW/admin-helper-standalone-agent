package pl.edu.agh.adminmanager.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.monitor.BaseMonitor;

@SuppressWarnings("restriction")
@Component("contextController")
public class ContextController {
	private static Logger log = Logger.getLogger(ContextController.class
			.getName());

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private TaskScheduler taskScheduler;

	private ApplicationContext context;

	private Properties config;

	private boolean debug;

	private Client client;

	public ApplicationContext getContext() {
		return context;
	}

	void setContext(ApplicationContext context) {
		client = (Client) context.getBean("client");
		this.context = context;
	}

	@PostConstruct
	public void init() {

		config = new Properties();
		try {
			InputStream s = new FileInputStream("config.properties");
			config.load(s);

			s.close();
		} catch (FileNotFoundException e) {
			OutputStream newConfig;
			try {
				newConfig = new FileOutputStream(new File(
						"config.properties"));
				InputStream backupConfig = getClass().getResourceAsStream(
						"/config/config.properties");
				byte[] buf = new byte[1024];
				int len;
				while ((len = backupConfig.read(buf)) > 0){
					newConfig.write(buf, 0, len);
				}
				newConfig.close();
				backupConfig.close();
				InputStream s = new FileInputStream("config.properties");
				config.load(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("FATAL ERROR " + e1);
				System.exit(0);
			}

		} catch (IOException e) {
			// TODO: add kill on properties error
			log.error("FATAL ERROR"+e);
			System.exit(0);
		}
		debug = Boolean.parseBoolean(getProperty("debug"));

	}

	public String getProperty(String name) {
		return config.getProperty(name);
	}

	public void setProperty(String name, String value) {
		config.setProperty(name, value);
	}

	public void runTask(Runnable task) {
		taskExecutor.execute(task);
	}

	public void close() {
		System.exit(0);

	}

	public void registerMonitor(final BaseMonitor monitor) {
		taskScheduler.scheduleAtFixedRate(new Runnable() {

			public void run() {
				if (monitor != null && client != null)
					if (monitor.isEnabled())
						client.sendPost(Client.STORE_REQ, monitor.execute(),
								monitor.getResponceCallback(), true);

			}
		}, monitor.getFixedPeriod());
	}

	public boolean isDebug() {
		return debug;
	}

	Properties getConfigProperties() {
		return config;
	}
}
