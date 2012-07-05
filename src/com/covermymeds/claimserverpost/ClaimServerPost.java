package com.covermymeds.claimserverpost;

import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

/**
 * &copy 2012 CoverMyMeds <br/>
 * A Java reference to making a request using the
 * <a href="http://www.covermymeds.com/main/pharmacy_claim_api#what-data-to-send">
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
 *
 */
public class ClaimServerPost {

	/**
	 * Using the command line arguments passed in, creates a JCommanderObject and
	 * uses its parameters to send a http request to a claim server. Unless specified
	 * not to, using the "-x or --suppress-browser" flag, opens a browser to each
	 * url returned in the response.
	 * @param args the command line options passed in.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {

		JCommanderOptions parsedOptions = ClaimServerPostUtils.parseCommandLine(args);

		// Check that either a correct claim file has been given or the user has enabled the option
		// to enter it themselves
		if (parsedOptions != null && (ClaimServerPostUtils.claimFileExists(parsedOptions.getClaimFile())
				|| parsedOptions.readFromStdin())) {

			UrlEncodedFormEntity encodedParameters = ClaimServerPostUtils
					.encodeParameters(parsedOptions);

			List<String> addresses = ClaimServerPostUtils.sendHttpRequest(
					parsedOptions.getServiceUrl(), encodedParameters,
					parsedOptions.isVerbose());
			
			if(addresses != null && !parsedOptions.isSuppress()) {
				ClaimServerPostUtils.openBrowser(addresses, parsedOptions.isVerbose());
			}
			
		} else if(parsedOptions != null){
			System.err.println("Error: must specify a claim file argument or '-' to read from stdin");
		}
	}
	
	private ClaimServerPost() {
		//disable external instantiation
	}
}