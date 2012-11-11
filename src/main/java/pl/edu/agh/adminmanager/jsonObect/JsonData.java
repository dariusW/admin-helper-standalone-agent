package pl.edu.agh.adminmanager.jsonObect;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonData {
	
	private static Gson gson = (new GsonBuilder()).excludeFieldsWithModifiers(Modifier.STATIC).create();
	
	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
}
