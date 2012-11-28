// Copyright 2012 CoverMyMeds
package com.covermymeds.claimserverpost;

import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

/**
 * &copy 2012 CoverMyMeds <br/>
 * A Java reference to making a request using the
 * <a href="http://www.covermymeds.com/main/pharmacy_claim_api">
 * CoverMyMeds' Pharmacy API</a>.
 * <br/>
 * <br/>
 * <div>
 * 	<b>Necessary libraries:</b>
 * 	<ul>
 * 		<li>Apache HttpClient 4.2.1</li>
 * 		<li>JCommander 1.26</li>
 * 	</ul>
 * </div>
 * 	@author Juan Roman
 * @see <a href="http://hc.apache.org/httpcomponents-client-ga/index.html">Apache HttpClient</a>,
 * <a href="http://jcommander.org">JCommander</a>
 */
public class ClaimServerPost {

	/**
	 * Uses the  arguments passed in to send a http request to a claim server. 
	 * Unless specified not to, using the "-x" or "--suppress-browser" flag, 
	 * opens a browser to each url returned in the response.
	 * @param args the options passed in to be used for sending the http request
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {

		JCommanderOptions parsedOptions = ClaimServerPostUtils.parseCommandLine(args);

		if (parsedOptions != null) {

			UrlEncodedFormEntity encodedParameters = ClaimServerPostUtils
					.encodeParameters(parsedOptions);

			List<String> addresses = ClaimServerPostUtils.sendHttpRequest(
					parsedOptions.getServiceUrl(), encodedParameters,
					parsedOptions.isVerbose());
			
			if(addresses != null && !parsedOptions.isSuppressed()) {
				ClaimServerPostUtils.openBrowser(addresses, parsedOptions.isVerbose());
			}
			
		}
	}
}