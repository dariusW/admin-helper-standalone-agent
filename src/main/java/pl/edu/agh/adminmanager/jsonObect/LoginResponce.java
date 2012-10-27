package pl.edu.agh.adminmanager.jsonObect;

public class LoginResponce extends JsonData{
	private String nickname;
	private String error;
	private String user_key;
	private String sensor_key;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getUserKey() {
		return user_key;
	}

	public void setUserKey(String user_key) {
		this.user_key = user_key;
	}

	public String getSensorKey() {
		return sensor_key;
	}

	public void setSensorKey(String sensor_key) {
		this.sensor_key = sensor_key;
	}
}
