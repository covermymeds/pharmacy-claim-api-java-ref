package com.covermymeds.claimserverpost;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

public class JCommanderObject {

	@Parameter(names = { "-a", "--api_key" }, description = "API key. Assigned by CMM", required = true)
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

	public File getClaimInFile() {
		return claimFile;
	}

	public boolean readFromStdin() {
		return readFromStdin;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Boolean isVerbose() {
		return verbose;
	}

	public String getService_url() {
		return serviceUrl;
	}

	public Boolean isSuppress() {
		return suppress;
	}
}
