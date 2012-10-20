package pl.edu.agh.adminmanager.agent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("client")
public class Client {

	private static Logger log = Logger.getLogger(Client.class.getName());

	@Autowired
	private ContextController context;

	private HttpClient client = new DefaultHttpClient();

	private String hostUrl;

	private String userName;

	private String sensorKey;

	private static final String USER_REQ = "user";

	public static final String STORE_REQ = "store";

	@PostConstruct
	public void init() {
		hostUrl = context.getProperty("host");
		userName = context.getProperty("user");

		try {
			JSONObject content = new JSONObject();
			content.append("nickname", userName);
			sendPost(USER_REQ, content, new PostCallback() {

				public void handle(JSONObject json) {
					try {
						sensorKey = json.getString("sensor_key");
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}, false);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void sendPost(final String path, final Object content,
			final PostCallback responceCallback, final boolean sign) {
		final String url;

		if (sign)
			url = "http://" + hostUrl + "/" + path + "?key=" + sensorKey;
		else
			url = "http://" + hostUrl + "/" + path;

		if (context.isDebug())
			log.info("POST REQUEST: " + url + " CONTENT: "
					+ (content != null ? content : "NULL"));
		else

			context.runTask(new Runnable() {

				public void run() {
					HttpPost postRequest;
					postRequest = new HttpPost(url);

					StringEntity post;
					try {
						post = new StringEntity(content.toString());
						post.setContentType("application/json");
						postRequest.setEntity(post);

						HttpResponse postResponce = client.execute(postRequest);

						if (postResponce.getStatusLine().getStatusCode() != 201) {
							throw new RuntimeException(
									"Failed : HTTP error code : "
											+ postResponce.getStatusLine()
													.getStatusCode());
						}

						if (responceCallback != null) {
							BufferedInputStream buffer = new BufferedInputStream(
									postResponce.getEntity().getContent());
							responceCallback.handle(new JSONObject(buffer
									.toString()));
						}

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

	}

	public interface PostCallback {

		public void handle(JSONObject json);
	}
}
