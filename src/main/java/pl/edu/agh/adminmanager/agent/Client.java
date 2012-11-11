package pl.edu.agh.adminmanager.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.edu.agh.adminmanager.jsonObect.JsonData;
import pl.edu.agh.adminmanager.jsonObect.Login;
import pl.edu.agh.adminmanager.jsonObect.LoginResponce;

import com.google.gson.Gson;

@SuppressWarnings("restriction")
@Component("client")
public class Client {

	private static Logger log = Logger.getLogger(Client.class.getName());

	@Autowired
	private ContextController context;

	private HttpClient client;

	private String hostUrl;

	private String userName;

	private String sensorKey;

	private String userKey;

	private boolean authorize;

	private static final String USER_REQ = "user";

	public static final String STORE_REQ = "store";

	private final Gson jsonParser = new Gson();

	@PostConstruct
	public void init() {
		hostUrl = context.getProperty("host");
		userName = context.getProperty("user");
		authorize = Boolean.parseBoolean(context.getProperty("authorize"));

		if (!authorize)
			return;

		Login content = new Login();
		content.setNickname(userName);

		sendPost(USER_REQ, content, new PostCallback() {

			public void handle(String json) {
				LoginResponce responce = jsonParser.fromJson(json,
						LoginResponce.class);

				if (responce.getError() != null) {
					log.error(responce.getError());
					return;
				}

				setSensorKey(responce.getSensorKey());
				setUserKey(responce.getUserKey());
			}
		}, false);

	}

	public String toJson(JsonData src) {
		return getJsonParser().toJson(src);
	}

	public Gson getJsonParser() {
		return jsonParser;
	}

	public void sendPost(final String path, final JsonData content,
			final PostCallback responceCallback, final boolean sign) {
		final String url;

		if (sign)
			url = "http://" + hostUrl + "/" + path + "?key=" + getSensorKey();
		else
			url = "http://" + hostUrl + "/" + path;

		if (context.isDebug())
			log.info("POST REQUEST: " + url + " CONTENT: "
					+ (content != null ? toJson(content) : "NULL"));
		else

			context.runTask(new Runnable() {

				public void run() {
					client = new DefaultHttpClient();
					HttpPost postRequest;
					postRequest = new HttpPost(url);

					StringEntity post;
					try {
						post = new StringEntity(content.toString());
						post.setContentType("application/json");
						postRequest.setEntity(post);

						HttpResponse postResponce = client.execute(postRequest);
						
						if (postResponce.getStatusLine().getStatusCode() < 200 || postResponce.getStatusLine().getStatusCode() > 200) {
							log.error("Failed : HTTP error code : "
									+ postResponce.getStatusLine()
											.getStatusCode()+ " => "+ postResponce);
							
						} else {

							if (responceCallback != null) {
								String json = "";
								String line;
								BufferedReader buffer = new BufferedReader(new InputStreamReader(
										postResponce.getEntity().getContent()));
								while ((line = buffer.readLine()) != null) {
									json+=line;
								}
								responceCallback.handle(json);
							}
						}

					} catch (UnsupportedEncodingException e) {
						log.error(e);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						log.error(e);
						// System.out.println("Connection to "+url+" refused");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.error(e);
					}
					client.getConnectionManager().shutdown();

				}
			});

	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getSensorKey() {
		return sensorKey;
	}

	public void setSensorKey(String sensorKey) {
		this.sensorKey = sensorKey;
	}

	public interface PostCallback {

		public void handle(String json);
	}
}
