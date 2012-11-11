package pl.edu.agh.adminmanager.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.agh.adminmanager.agent.Client;
import pl.edu.agh.adminmanager.agent.ContextController;
import pl.edu.agh.adminmanager.jsonObect.JsonData;

import com.google.gson.Gson;

@SuppressWarnings("restriction")
public abstract class BaseMonitor {

	@Autowired
	private ContextController context;

	private String propertyBase = "";

	protected long fixedPeriod;

	protected boolean enabled;

	private Client.PostCallback responceCallback = null;

	private final Gson jsonParser = new Gson();

	public abstract JsonData execute();

	public BaseMonitor() {

		List<String> split = Arrays.asList(getClass().getName().split(
				"(?=\\p{Upper})"));
		split.set(0, "");
		for (String s : split)
			propertyBase += (propertyBase.isEmpty() ? s.toLowerCase() : "."
					+ s.toLowerCase()).trim();

	}

	@PostConstruct
	public void init() {
		setEnabled(Boolean.parseBoolean(context.getProperty(propertyBase)));

		setFixedPeriod(Long.parseLong(context.getProperty(propertyBase
				+ ".period")));

		context.registerMonitor(this);
	}

	public ContextController getContext() {
		return context;
	}

	public long getFixedPeriod() {
		return fixedPeriod;
	}

	public void setFixedPeriod(long fixedPeriod) {
		this.fixedPeriod = fixedPeriod;
	}

	public String getPropertyBase() {
		return propertyBase;
	}

	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String toString() {
		return propertyBase;
	}

	public Client.PostCallback getResponceCallback() {
		return responceCallback;
	}

	public void setResponceCallback(Client.PostCallback responceCallback) {
		this.responceCallback = responceCallback;
	}

	public String toJson(Object src) {
		return getJsonParser().toJson(src);
	}

	public <T> T fromJson(String src, Class<T> classOfT) {
		return getJsonParser().fromJson(src, classOfT);
	}

	public Gson getJsonParser() {
		return jsonParser;
	}

	public List<String> execExternalMonitor(String uri) throws IOException, InterruptedException {
		List<String> result = new ArrayList<String>();
		Process p = Runtime.getRuntime().exec(uri);
		//p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			result.add(line);
		}
		p.destroy(); 
		return result;
	}

}
