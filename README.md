**Introduction**
- This is a ‘proof of concept’ that is being provided ‘as is’ without any support coverage or warranty. Using this approach to remove the Blacklisted Algorithms is NOT recommended by Liferay.
- It has NOT been tested in an environment with a working SAML setup, so it MAY not work.
- This solution will attempt to override the Blacklisted Algorithms built into the OpenSAML codebase.
- It was build based on the source code for Liferay DXP 2025.Q1.0 LTS and was compiled and run locally with JDK 21.

**Deployment Steps**
1. Blacklist the OSGi Component com.liferay.saml.opensaml.integration.internal.bootstrap.SecurityConfigurationBootstrap using these steps: https://learn.liferay.com/w/dxp/system-administration/installing-and-managing-apps/managing-apps/blacklisting-osgi-components
2. Build and deploy the following custom modules:
- custom-saml-opensaml-integration
- saml-opensaml-integration-fragment
3. Confirm that the OSGi modules deploy without any errors.
- custom-saml-opensaml-integration should show a State of Active from a Gogo Shell lb command 
- saml-opensaml-integration-fragment is a Fragment Module so it should show a State of Resolved from a Gogo Shell lb command 
4. Check the server logs for logging like the following after deploying the modules:
```
...[SecurityConfigurationBootstrap:85] DecryptionConfiguration Blacklisted Before: 1
...[SecurityConfigurationBootstrap:90] http://www.w3.org/2001/04/xmlenc#rsa-1_5
...[SecurityConfigurationBootstrap:95] DecryptionConfiguration Blacklisted After: 0
...[SecurityConfigurationBootstrap:103] EncryptionConfiguration Blacklisted Before: 1
...[SecurityConfigurationBootstrap:108] http://www.w3.org/2001/04/xmlenc#rsa-1_5
...[SecurityConfigurationBootstrap:113] EncryptionConfiguration Blacklisted After: 0
```
