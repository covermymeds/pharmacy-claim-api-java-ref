package com.covermymeds.claimserverpost;

import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

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

	/*
	 * Sample Post request to claims server.
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {

		JCommanderObject parsedObject = ClaimServerPostUtils.parseCommandLine(args);

		// Validate that a claim is present
		if (parsedObject != null && ClaimServerPostUtils.claimSupplied(parsedObject.getClaimInFile(), parsedObject.readFromStdin())) {

			UrlEncodedFormEntity encodedParameters = ClaimServerPostUtils
					.encodeParameters(parsedObject);

			List<String> addresses = ClaimServerPostUtils.sendHttpRequest(
					parsedObject.getService_url(), encodedParameters,
					parsedObject.isVerbose());
			
			if(addresses != null && !parsedObject.isSuppress()) {
				ClaimServerPostUtils.openBrowser(addresses, parsedObject.isVerbose());
			}
			
		} else if(parsedObject != null){
			System.err
					.println("Error: must specify a claim file argument or '-' to read from stdin");
		}
	}
}