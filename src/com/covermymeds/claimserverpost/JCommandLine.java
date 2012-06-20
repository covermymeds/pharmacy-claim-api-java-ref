package com.covermymeds.claimserverpost;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

public class JCommandLine {

	@Parameter(names={"-f","--fax"},description="physician fax number")
	private String faxNumber = "";
	
	@Parameter(names={"-u","--username"},description="username with which to authenticate", required=true)
	private String username;
	
	@Parameter(names={"-p","--password"},description="password with which to authenticate", required=true)
	private String password;
	
	@Parameter(names={"-v","--verbose"},description="show verbose output")
	private Boolean verbose = false;
	
	@Parameter(names={"-s","--service"},description="URL of the service which to POST")
	private String service_url = "https://claims.covermymeds.com/cmmimport/";
	
	@Parameter(names={"-x","--suppress-browser"},description="suppress opening a browser window for each URL")
	private Boolean suppress = false;
	
	@Parameter(names={"-c","--claim"},description="File where claim is present", converter=FileConverter.class)
	private File claimFile;
	
	@Parameter(description = "The claim itself if --claim, -c is not supplied surrounded in \"\"s")
	private List<String> claim = new ArrayList<String>();

	public File getClaimInFile() {
		return claimFile;
	}

	public List<String> getClaim() {
		return claim;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getVerbose() {
		return verbose;
	}

	public String getService_url() {
		return service_url;
	}

	public Boolean getSuppress() {
		return suppress;
	}
}
