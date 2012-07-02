package com.covermymeds.claimserverpost;

//TODO Move private functions to utility class and check 
// for null addresses before trying to open them as well
// as check if suppressed
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
 * 
 * Apache HttpClient 4.2
 * 
 * JCommander 1.26
 * 
 * 
 */
public class ClaimServerPost {

	private static Map<Integer, String> errors = createErrors();

	/*
	 * Sample Post request to claims server.
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {

		JCommanderObject parsedObject = ClaimServerPost.parseCommandLine(args);

		// Validate that a claim is present
		if (parsedObject != null && ClaimServerPost.claimSupplied(parsedObject)) {

			UrlEncodedFormEntity encodedParameters = ClaimServerPost
					.encodeParameters(parsedObject);

			List<String> addresses = ClaimServerPost.sendHttpRequest(
					parsedObject.getService_url(), encodedParameters,
					parsedObject.isVerbose());
			if(!parsedObject.isSuppress()) {
				ClaimServerPost.openBrowser(addresses, parsedObject.isVerbose());
			}
		} else {
			System.err
					.println("Error: must specify a claim file argument or '-' to read from stdin");
		}
	}

	/*
	 * Checks that a claim is present as either a file or directly as a String
	 */
	private static boolean claimSupplied(JCommanderObject parsedObject) {
		File claimFile = parsedObject.getClaimInFile();
		return ((claimFile != null && claimFile.exists()) || parsedObject
				.readFromStdin());
	}

	/*
	 * Returns the claim as a String
	 */
	private static String getClaim(File claimFile, boolean readFromStdin)
			throws IOException {
		if (readFromStdin) {
			System.out.println("Enter claim:");
			Scanner stdin = new Scanner(System.in);
			String input = stdin.nextLine();
			if(input.length() >= 2 && input.charAt(0) != (char)02) {
				input = (char)02 + input;
			}
			if(input.length() >= 2 && input.charAt(input.length()-1) != (char)03) {
				input += (char)03;
			}
			return input;
		} else {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(claimFile));
				char[] buf = new char[1024];
				int numRead = 0;
				StringBuffer fileData = new StringBuffer();
				while ((numRead = reader.read(buf)) != -1) {
					String readData = String.valueOf(buf, 0, numRead);
					fileData.append(readData);
				}
				return fileData.toString();
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		}
	}

	/*
	 * Returns a map conatining errors
	 */
	private static Map<Integer, String> createErrors() {
		Map<Integer, String> result = new HashMap<Integer, String>();
		result.put(400, "Oops, there was a connection problem. Please"
				+ " try one more time, then contact CoverMyMeds at 1-866-452-"
				+ "5017/help@covermymeds.com and they will help you diagnose"
				+ " this issue.");
		result.put(
				403,
				"Oops, login failed for the username or password that"
						+ " was submitted. Please check the username and password in your"
						+ " account settings in your Pharmacy System and at the CMM website to"
						+ " make sure they match. If you still have trouble, please contact"
						+ " CoverMyMeds at 1-866-452-5017/help@covermymeds.com and they will"
						+ " help you fix this issue.");
		result.put(
				404,
				"Oops, there was a problem. Please check the username and"
						+ " password in your account settings in your Pharmacy System and at the"
						+ " CMM website to make sure they match. If you still have trouble, please"
						+ " contact CoverMyMeds at 1-866-452-5017/help@covermymeds.com and they will"
						+ " help you fix this issue.");
		result.put(
				408,
				"Oops, there was a timeout. Please try the request again in one"
						+ " minute. If you still have trouble, please contact CoverMyMeds at 1-866-452"
						+ "-5017/help@covermymeds.com and they will help you fix this issue.");
		result.put(
				500,
				"Oops, there was a problem. Please try the request again in one minute."
						+ " If you still have trouble, please contact CoverMyMeds at 1-866-452-5017"
						+ "/help@covermymeds.com and they will help you diagnose this issue.");
		return Collections.unmodifiableMap(result);
	}

	/*
	 * Prints possible options associated with object
	 */
	private static void printUsage(Object object) {
		JCommander help = new JCommander(object);
		help.setProgramName("ClaimServerPost");
		help.usage();
	}

	private static JCommanderObject parseCommandLine(String[] args) {
		// Parse commandLine options
		JCommanderObject parsedObject = null;
		try {
			parsedObject = new JCommanderObject();
			// Parsed Object by calling JCommander Constructor
			new JCommander(parsedObject, args);
		} catch (ParameterException e) {
			System.err.println("Error: " + e.getMessage());
			printUsage(new JCommanderObject());
			parsedObject = null;
		}
		return parsedObject;
	}

	private static UrlEncodedFormEntity encodeParameters(
			JCommanderObject parsedObject) throws IOException {

		// Creat and Encode parameters
		List<BasicNameValuePair> params = Arrays.asList(
				new BasicNameValuePair("username", parsedObject.getUsername()),
				new BasicNameValuePair("password", parsedObject.getPassword()),
				new BasicNameValuePair("ncpdp_claim", ClaimServerPost
						.getClaim(parsedObject.getClaimInFile(),parsedObject.readFromStdin())), new BasicNameValuePair(
						"physician_fax", parsedObject.getApiKey()));

		return new UrlEncodedFormEntity(params, "UTF-8");
	}

	private static List<String> sendHttpRequest(String serviceUrl,
			UrlEncodedFormEntity encodedParameters, boolean verbose)
			throws ClientProtocolException, IOException {

		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl);
		httpPost.setEntity(encodedParameters);

		List<String> addresses = null;
		try {
			// Execute POST with BasicResponseHandler object
			String response = client.execute(httpPost,
					new BasicResponseHandler());

			addresses = Arrays.asList(response.split("\n"));

		}
		// Catch any errors(400,500...) and handle them as appropriate
		catch (HttpResponseException e) {
			String errorMessage = String.valueOf(e.getStatusCode()) + " "
					+ e.getMessage();
			errorMessage = (verbose ? errorMessage + " "
					+ errors.get(e.getStatusCode()) : errorMessage);
			System.err.println(errorMessage);
		} finally {
			// Close all connections and free up system resources
			client.getConnectionManager().shutdown();
		}
		return addresses;
	}

	private static void openBrowser(List<String> addresses, boolean verbose)
			throws IOException, URISyntaxException {
		// If supported and suppress not present, open browser and
		// point to each address
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				for (String address : addresses) {
					if (verbose) {
						System.out.println("Opening browser to: " + address);
					}
					desktop.browse(new URI(address));
				}
			} else {
				System.err.println("Browse action is not supported.");
			}
		} else {
			System.err.println("Desktop is not supported.");
		}
	}
}