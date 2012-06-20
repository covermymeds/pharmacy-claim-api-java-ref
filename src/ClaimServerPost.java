import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URI;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;


/*
 * Http Post to a claim Server and open point browser to response
 */
public class ClaimServerPost {

	private static final String url = "https://claims.covermymeds.com/cmmimport/";
	private static final String username = "jerry@csu.edu";
	private static final String password = "foo";
	private static final String claim = "035079D0B1A4        1071804031        20120123          AM04C2H11443628C1CKYAC301C61AM01C419580518C52CATONA LCBDO<BCM1510 ST RT 176CNAOYWHERECOFLCP42345AM07EM1D21439264E103D753746025310E760000D530DE20110826EX6142328850AM03EZ01DB1679652648DRSMITHPM27075438802JRALPH2K101 MAIN STREET2MCENTRALL CITY2NKY2P42330";
	private static final String api_key = "63de0d385960cef796ae40d775225f32";

	/*
	 * Sample Post request to claims server.
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {

		// Create an instance of HttpClient.
		HttpClient client = new DefaultHttpClient();

		// Creat and Encode parameters
		List<BasicNameValuePair> formparams = Arrays.asList(
				new BasicNameValuePair("username", username),
				new BasicNameValuePair("password", password),
				new BasicNameValuePair("ncpdp_claim", claim),
				new BasicNameValuePair("api_key", api_key));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");

		// Create HttpPost and set its Entity
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		try {
			// Execute POST with BasicResponseHandler object
			String response = client.execute(httpPost,
					new BasicResponseHandler());
			String[] addresses = response.split("\n");
			
			//If supported, open browser and point to each address
			if(Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if(desktop.isSupported(Desktop.Action.BROWSE)) {
					for(String address : addresses) {
					desktop.browse(new URI(address));
					}
				}
			}
		}
		// Catch any errors(400,500...) and handle them as appropriate
		catch (HttpResponseException e) {
			System.out.println("Error code: " + e.getStatusCode());
		} finally {
			// Close all connections and free up system resources
			client.getConnectionManager().shutdown();
		}
	}
}