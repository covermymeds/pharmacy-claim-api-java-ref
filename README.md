#CoverMyMeds Pharmacy Claim API
##Java Reference Implementation
##Overview
Integrate your pharmacy system with CoverMyMeds to create an "Easy Button" for Prior Authorization. Uses the NCPDP Telecom Claim standard and integration takes only a few days of development time.
*	[Download an Overview Presentation](http://www.covermymeds.com/files/cmm-pharmacy-system-overview.ppt) or [(.pdf)](http://www.covermymeds.com/files/cmm-pharmacy-system-overview.pdf)
*	[Full Technical Documentation](http://www.covermymeds.com/main/pharmacy_claim_api)

Use our [Pharmacy System](http://pharmacysystems.covermymeds.com/) page to see a list of systems that integrate with CoverMyMeds or to contact your system vendor to request integration.
This reference implementation is offered to assist in integrating with the CoverMyMeds Claim API using the Eclipse enviornment with Java 6. The user passes command line style arguments and values, i.e --verbose, and the passed in values are used to send a claim to the API. Its uses both [JCommander 1.26](http://jcommander.org) to parse the passed in arguments and [Apache HttpClient 4.2.1](http://hc.apache.org/httpcomponents-client-ga/index.html) to submit the claim and process its response. Most of the code used to submit the claim, as well as response and error handling, is encapsulated in the ClaimServerPostUtils.java file.

##Getting Started
*	Sign up for a free account to learn how CoverMyMeds works. <https://www.covermymeds.com/signup>
*	Watch a short [overview video.](http://help.covermymeds.com/entries/47511-learn-how-to-use-covermymeds-5-minute-silent-video)
*	[Get in touch](mailto:developers@covermymeds.com) and we'll provide an Account Manager to help you through the process and direct access to our senior developers.
*	API Key: [email us to request a key.](mailto:developers@covermymeds.com) We will get back to you the same day.