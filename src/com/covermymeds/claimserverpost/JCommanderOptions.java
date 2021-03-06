// Copyright 2012 CoverMyMeds
package com.covermymeds.claimserverpost;

import java.io.File;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * &copy 2012 CoverMyMeds <br/>
 * Used in combination with the JCommander library to hold parsed argument 
 * values.
 * <br/>
 * The execution flow can be demonstrated by the following code snippet:
 * <br/>
 * <br/>
 * <code>
 * 		String []args = {"-u","John","-p","root","-a","apikey"}; <br/>
 *		JCommanderObject parsedObject = new JCommanderObject(); <br/>
 *		new JCommander(parsedObject, args); <br/>
 *		parsedObject.getUsername().Equals("John");
 * </code>
 * 	@author Juan Roman
 *
 */
public class JCommanderOptions {

	
	@Parameter(names = { "-u", "--username" }, description = "username with which to authenticate", required = true)
	private String username;

	@Parameter(names = { "-p", "--password" }, description = "password with which to authenticate", required = true)
	private String password;
	
	@Parameter(names = { "-a", "--api-key" }, description = "API key. Assigned by CMM", required = true)
	private String apiKey;

	@Parameter(names = { "-s", "--service" }, description = "URL of the service which to POST")
	private String serviceUrl = "https://claims.covermymeds.com/cmmimport/";
	
	@Parameter(names = { "-f", "--fax"}, description = "Physician fax number")
	private String faxNumber;
	
	@Parameter(names = { "-c", "--claim" }, description = "File where claim is present", converter = FileConverter.class)
	private File claimFile;
	
	@Parameter(names = { "-x", "--suppress-browser" }, description = "suppress opening a browser window")
	private Boolean suppressed = false;
	
	@Parameter(names = { "-v", "--verbose" }, description = "show verbose output")
	private Boolean verbose = false;	

	/**
	 * Default constructor.
	 */
	public JCommanderOptions() {}
	
	/**
	 * Returns the file holding the claim or <code>null</code> if not specified.
	 * @return the file holding the claim or <code>null</code> if not specified
	 */
	public File getClaimFile() {
		return claimFile;
	}

	/**
	 * Returns the api key 
	 * @return the api key
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
	 * Returns the fax number
	 * @return the fax number
	 */
	public String getFaxNumber() {
		return faxNumber;
	}

	/**
	 * Returns <code>true</code> if the verbose option has been set
	 * @return <code>true</code> if the verbose option has been set
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Returns the service url
	 * @return the service url
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}

	/**
	 * Returns <code>true</code> if the suppressed option has been set.
	 * @return <code>true</code> if the suppressed option has been set
	 */
	public boolean isSuppressed() {
		return suppressed;
	}
}
