package pl.edu.agh.adminmanager.jsonObect;

import com.google.gson.Gson;

public class JsonData {
	
	private Gson gson = new Gson();
	
	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
}
