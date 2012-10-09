package pl.edu.agh.adminmanager.monitor;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.agh.adminmanager.agent.Client;
import pl.edu.agh.adminmanager.agent.ContextController;

public abstract class BaseMonitor {
	
	@Autowired 
	private ContextController context;
		
	private String propertyBase ="";
	
	private long fixedPeriod;
	
	private boolean enabled;
	
	private Client.PostCallback responceCallback = null;
	
	public abstract Object execute();
	
	public BaseMonitor(){
		
		List<String> split = Arrays.asList(getClass().getName().split("(?=\\p{Upper})"));
		split.set(0, "");
		for(String s: split)
			propertyBase += (propertyBase.isEmpty()?s.toLowerCase():"."+s.toLowerCase()).trim();
				
	}
	
	@PostConstruct
	public void init(){
		setEnabled(Boolean.parseBoolean(context.getProperty(propertyBase)));
		
		setFixedPeriod(Long.parseLong(context.getProperty(propertyBase+".period")));

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

	public String getPropertyBase(){
		return propertyBase;
	}

	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String toString(){
		return propertyBase;
	}

	public Client.PostCallback getResponceCallback() {
		return responceCallback;
	}

	public void setResponceCallback(Client.PostCallback responceCallback) {
		this.responceCallback = responceCallback;
	}
}
