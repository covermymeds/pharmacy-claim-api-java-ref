package com.covermymeds.claimserverpost;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * Used in combination with the JCommander class to hold parsed command line 
 * argument values.
 * <br/>
 * The usual execution flow can be demonstrated by the code snippet below:
 * <br/>
 * <br/>
 * <pre>
 * <code>
 * 		String []args = {"-u","username","-p","password","-a","apikey","-"};
 *		JCommanderObject parsedObject = new JCommanderObject();
 *		new JCommander(parsedObject, args);
 * </code>
 * </pre>
 * <div>
 * 	@author Juan Roman
 * </div>
 * &copy 2012 CoverMyMeds
 *
 */
public class JCommanderObject {

	@Parameter(names = { "-a", "--api-key" }, description = "API key. Assigned by CMM", required = true)
	private String apiKey;

	@Parameter(names = { "-u", "--username" }, description = "username with which to authenticate", required = true)
	private String username;

	@Parameter(names = { "-p", "--password" }, description = "password with which to authenticate", required = true)
	private String password;

	@Parameter(names = { "-v", "--verbose" }, description = "show verbose output")
	private Boolean verbose = false;

	@Parameter(names = { "-s", "--service" }, description = "URL of the service which to POST")
	private String serviceUrl = "https://claims.covermymeds.com/cmmimport/";

	@Parameter(names = { "-x", "--suppress-browser" }, description = "suppress opening a browser window for each URL")
	private Boolean suppress = false;

	@Parameter(names = { "-c", "--claim" }, description = "File where claim is present", converter = FileConverter.class)
	private File claimFile;

	@Parameter(names = { "-" }, description = "Used when claim will be provided through stdin")
	private Boolean readFromStdin = false;

	/**
	 * Returns the file holding the claim or <code>null</code> if not specified.
	 * @return the file holding the claim or <code>null</code> if not specified
	 */
	public File getClaimInFile() {
		return claimFile;
	}

	/**
	 * Returns <code>true</code> if a claim will be entered using standard input.
	 * @return <code>true</code> if a claim will be entered using standard input
	 */
	public boolean readFromStdin() {
		return readFromStdin;
	}

	/**
	 * Returns the apiKey passed into 
	 * @return
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Returns the username
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the password
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns <code>true</code> if the verbose option has been set
	 * @return <code>true</code> if the verbose option has been set
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Returns the url
	 * @return the url 
	 */
	public String getService_url() {
		return serviceUrl;
	}

	/**
	 * Returns <code>true</code> if the suppressed option has been set.
	 * @return <code>true</code> if the suppressed option has been set
	 */
	public boolean isSuppress() {
		return suppress;
	}
}
