package pl.edu.agh.adminmanager.agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.monitor.BaseMonitor;

@SuppressWarnings("restriction")
@Component("contextController")
public class ContextController {

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
//					getClass().getResourceAsStream(
//					"/config/config.properties");
			config.load(s);

			s.close();
			debug = Boolean.parseBoolean(getProperty("debug"));
		} catch (IOException e) {
			// TODO: add kill on properties error
			e.printStackTrace();
		}

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

	Properties getConfigProperties(){
		return config;
	}
}
