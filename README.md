**Introduction**
- This is a ‘proof of concept’ that is being provided ‘as is’ without any support coverage or warranty. Using this approach to remove the Blacklisted Algorithms is NOT recommended by Liferay.
- It has NOT been tested in an environment with a working SAML setup, so it MAY not work.
- It is being provided as a temporary workaround until a permanent resolution is in place and MUST not be used in a Production environment.
- This solution will attempt to override the Blacklisted Algorithms built into the OpenSAML codebase.
- It was build based on the source code for Liferay DXP 2025.Q1.0 LTS and was compiled and run locally with JDK 21.

**Deployment Steps**
1. Blacklist the OSGi Component com.liferay.saml.opensaml.integration.internal.bootstrap.SecurityConfigurationBootstrap using these steps: https://learn.liferay.com/w/dxp/system-administration/installing-and-managing-apps/managing-apps/blacklisting-osgi-components
2. Do a Gradle Clean then a Gradle build and deploy the following custom modules:
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

**Change Log**
- v2.0 14th June 2025, Fixed package dependency issue that was causing the following exception during SAML Login before the user was redirected to the SAML IdP:
```
2025-07-10 12:48:30.801 INFO  [http-nio-8098-exec-4][SpSsoSamlPortalFilter:109] Failed to send Authn request: java.lang.ClassCastException: class org.opensaml.xmlsec.impl.BasicEncryptionConfiguration cannot be cast to class org.opensaml.xmlsec.EncryptionConfiguration (org.opensaml.xmlsec.impl.BasicEncryptionConfiguration is in unnamed module of loader org.eclipse.osgi.internal.loader.EquinoxClassLoader @585c3fe; org.opensaml.xmlsec.EncryptionConfiguration is in unnamed module of loader org.eclipse.osgi.internal.loader.EquinoxClassLoader @4961dbf2)
```
- Ensure a gradle clean is triggered before a gradle build, to ensure the modules are built correctly based on the latest build.gradle and bnd.bnd.
