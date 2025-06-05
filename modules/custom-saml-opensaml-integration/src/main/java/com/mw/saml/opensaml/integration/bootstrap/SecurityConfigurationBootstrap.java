/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.mw.saml.opensaml.integration.bootstrap;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.saml.opensaml.integration.internal.util.ConfigurationServiceBootstrapUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.opensaml.xmlsec.DecryptionConfiguration;
import org.opensaml.xmlsec.EncryptionConfiguration;
import org.opensaml.xmlsec.SignatureSigningConfiguration;
import org.opensaml.xmlsec.SignatureValidationConfiguration;
import org.opensaml.xmlsec.config.DefaultSecurityConfigurationBootstrap;
import org.opensaml.xmlsec.impl.BasicDecryptionConfiguration;
import org.opensaml.xmlsec.impl.BasicEncryptionConfiguration;
import org.opensaml.xmlsec.impl.BasicSignatureSigningConfiguration;
import org.opensaml.xmlsec.impl.BasicSignatureValidationConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Carlos Sierra Andrés
 */
@Component(service = {})
public class SecurityConfigurationBootstrap {

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		BasicDecryptionConfiguration basicDecryptionConfiguration =
			DefaultSecurityConfigurationBootstrap.
				buildDefaultDecryptionConfiguration();
		BasicEncryptionConfiguration basicEncryptionConfiguration =
			DefaultSecurityConfigurationBootstrap.
				buildDefaultEncryptionConfiguration();
		BasicSignatureSigningConfiguration basicSignatureSigningConfiguration =
			DefaultSecurityConfigurationBootstrap.
				buildDefaultSignatureSigningConfiguration();
		BasicSignatureValidationConfiguration
			basicSignatureValidationConfiguration =
				DefaultSecurityConfigurationBootstrap.
					buildDefaultSignatureValidationConfiguration();

		Object blacklistedAlgorithmsObject = properties.get(
			"blacklisted.algorithms");		
		
		if (blacklistedAlgorithmsObject instanceof String[]) {
			basicDecryptionConfiguration.setBlacklistedAlgorithms(
				_combine(
					basicDecryptionConfiguration.getBlacklistedAlgorithms(),
					(String[])blacklistedAlgorithmsObject));

			basicEncryptionConfiguration.setBlacklistedAlgorithms(
				_combine(
					basicEncryptionConfiguration.getBlacklistedAlgorithms(),
					(String[])blacklistedAlgorithmsObject));

			basicSignatureSigningConfiguration.setBlacklistedAlgorithms(
				_combine(
					basicSignatureSigningConfiguration.
						getBlacklistedAlgorithms(),
					(String[])blacklistedAlgorithmsObject));

			basicSignatureValidationConfiguration.setBlacklistedAlgorithms(
				_combine(
					basicSignatureValidationConfiguration.
						getBlacklistedAlgorithms(),
					(String[])blacklistedAlgorithmsObject));
		}

		// MW Start
		
		Collection<String> emptyCollection = Collections.emptyList();
		
		_log.info("DecryptionConfiguration Blacklisted Before: " + basicDecryptionConfiguration.getBlacklistedAlgorithms().size());

		Iterator<String> decryptionBefore = basicDecryptionConfiguration.getBlacklistedAlgorithms().iterator();
		
		while (decryptionBefore.hasNext()) {
			_log.info(decryptionBefore.next());
		}
			
		basicDecryptionConfiguration.setBlacklistedAlgorithms(emptyCollection);
		
		_log.info("DecryptionConfiguration Blacklisted After: " + basicDecryptionConfiguration.getBlacklistedAlgorithms().size());

		Iterator<String> decryptionAfter = basicDecryptionConfiguration.getBlacklistedAlgorithms().iterator();
		
		while (decryptionAfter.hasNext()) {
			_log.info(decryptionAfter.next());
		}
		
		_log.info("EncryptionConfiguration Blacklisted Before: " + basicEncryptionConfiguration.getBlacklistedAlgorithms().size());

		Iterator<String> encryptionBefore = basicEncryptionConfiguration.getBlacklistedAlgorithms().iterator();
		
		while (encryptionBefore.hasNext()) {
			_log.info(encryptionBefore.next());
		}
			
		basicEncryptionConfiguration.setBlacklistedAlgorithms(emptyCollection);
		
		_log.info("EncryptionConfiguration Blacklisted After: " + basicEncryptionConfiguration.getBlacklistedAlgorithms().size());

		Iterator<String> encryptionAfter = basicEncryptionConfiguration.getBlacklistedAlgorithms().iterator();
		
		while (encryptionAfter.hasNext()) {
			_log.info(encryptionAfter.next());
		}
		// MW End
		
		ConfigurationServiceBootstrapUtil.register(
			DecryptionConfiguration.class, basicDecryptionConfiguration);
		ConfigurationServiceBootstrapUtil.register(
			EncryptionConfiguration.class, basicEncryptionConfiguration);
		ConfigurationServiceBootstrapUtil.register(
			SignatureSigningConfiguration.class,
			basicSignatureSigningConfiguration);
		ConfigurationServiceBootstrapUtil.register(
			SignatureValidationConfiguration.class,
			basicSignatureValidationConfiguration);
	}

	private Collection<String> _combine(
		Collection<String> collection, String... strings) {

		Collection<String> combinedCollection = new HashSet<>(collection);

		Collections.addAll(combinedCollection, strings);

		return combinedCollection;
	}

	private static final Log _log = LogFactoryUtil.getLog(SecurityConfigurationBootstrap.class);	
}