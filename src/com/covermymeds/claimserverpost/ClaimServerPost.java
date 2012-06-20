package com.covermymeds.claimserverpost;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URI;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/*
 * Http Post to a claim Server and open point browser to response
 */
public class ClaimServerPost {

	private static Map<Integer,String> errors = createErrors();
	
	private static String url = null;
	private static String username = null;
	private static String password = null;
	private static String claim = null;
	private static String fax = null;

	/*
	 * Sample Post request to claims server.
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {

		// Parse commandLine options
		JCommandLine parsedObject = null;
		boolean proceed = true;
		try {
			parsedObject = buildParser(args);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			proceed = false;
		}

		if (proceed && (proceed = validateClaim(parsedObject))) {
			// Assign values to POST parameters
			url = parsedObject.getService_url();
			username = parsedObject.getUsername();
			password = parsedObject.getPassword();
			fax = parsedObject.getFaxNumber();
			claim = getClaim(parsedObject);
		} else {
			System.err
					.println("Error: must specify a claim file argument or supply claim directly");
		}
		if(proceed) {
			// Create an instance of HttpClient.
			HttpClient client = new DefaultHttpClient();

			// Creat and Encode parameters
			List<BasicNameValuePair> formparams = Arrays.asList(
					new BasicNameValuePair("username", username),
					new BasicNameValuePair("password", password),
					new BasicNameValuePair("ncpdp_claim", claim),
					new BasicNameValuePair("physician_fax", fax));
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

				// If supported and suppress not present, open browser and point to each address
				if (!parsedObject.getSuppress() && Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					if (desktop.isSupported(Desktop.Action.BROWSE)) {
						for (String address : addresses) {
							desktop.browse(new URI(address));
						}
					}
				}
			}
			// Catch any errors(400,500...) and handle them as appropriate
			catch (HttpResponseException e) {
				if(parsedObject.getVerbose()) {
					System.out.println(e.getStatusCode()+errors.get(e.getStatusCode()));
				}
				else {
					System.out.println(e.getStatusCode());
				}
			} finally {
				// Close all connections and free up system resources
				client.getConnectionManager().shutdown();
			}
		}
		
	}

	private static JCommandLine buildParser(String[] args)
			throws ParameterException {
		JCommandLine parsedObject = new JCommandLine();
		JCommander command = new JCommander(parsedObject, args);
		return parsedObject;
	}

	private static boolean validateClaim(JCommandLine parsedObject) {
		File claimFile = parsedObject.getClaimInFile();
		if (claimFile == null) {
			List<String> claimString = parsedObject.getClaim();
			return (claimString.size() > 0);
		} else {
			return claimFile.exists();
		}
	}

	private static String getClaim(JCommandLine parsedObject)
			throws IOException {
		if (parsedObject.getClaim().size() > 0) {
			return parsedObject.getClaim().get(0);
		} else {
			File claimFile = parsedObject.getClaimInFile();
			BufferedReader reader = new BufferedReader(
					new FileReader(claimFile));
			char[] buf = new char[1024];
			int numRead = 0;
			StringBuilder fileData = new StringBuilder();
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
			return fileData.toString();
		}
	}
	private static Map<Integer,String> createErrors() {
		Map<Integer,String> result = new HashMap<Integer,String>();
		result.put(403, ". Valid CoverMyMeds authentication required.");
		result.put(404,". Resource not found or missing parameters.");
		result.put(408,". Server timed out. Try again.");
		result.put(500, ". Internal Service Erro. Try again, or contact admin if error persists.");
		return Collections.unmodifiableMap(result);
	}
}