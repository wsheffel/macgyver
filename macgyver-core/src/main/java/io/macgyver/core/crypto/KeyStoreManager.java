package io.macgyver.core.crypto;

import io.macgyver.core.Bootstrap;
import io.macgyver.core.ConfigurationException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import com.google.common.base.Strings;
import com.google.common.io.Closer;

public class KeyStoreManager {

	char[] defaultPass = new char[] { 0x63, 0x68, 0x61, 0x6e, 0x67, 0x65, 0x69,
			0x74 };

	private char[] keyStorePass = defaultPass;
	private char[] globalKeyPass = defaultPass;
	KeyStore keyStore;

	public static final String KEYSTORE_LOCATION_SYSTEM_PROPERTY = "macgyver.keyStore";

	public FileObject getKeyStoreLocation() throws IOException{

		String location = System.getProperty(KEYSTORE_LOCATION_SYSTEM_PROPERTY);
		if (!Strings.isNullOrEmpty(location)) {
			File f = new File(location);
			FileObject keyStoreLocation = VFS.getManager().resolveFile(
					f.getAbsoluteFile().toURI().toURL().toString());
			return keyStoreLocation;

		} else {

			FileObject fo = Bootstrap.getInstance().getVfsManager()
					.getConfigLocation().resolveFile("keystore.jceks");
			return fo;
		}
	}

	protected char[] getKeyStorePassword() {
		return keyStorePass;
	}

	protected char[] getPasswordForKey(String key) {
		return globalKeyPass;
	}

	public Key getKey(String alias) throws GeneralSecurityException {
		return getKeyStore().getKey(alias, getPasswordForKey(alias));
	}

	public synchronized KeyStore getKeyStore() throws GeneralSecurityException {
		if (keyStore != null) {
			return keyStore;
		}
		KeyStore ks = KeyStore.getInstance("JCEKS");

		Closer c = Closer.create();
		try {
			BufferedInputStream is = new BufferedInputStream(getKeyStoreLocation().getContent().getInputStream());
			c.register(is);
			ks.load(is, getKeyStorePassword());
			keyStore = ks;
		} catch (IOException e) {
			throw new GeneralSecurityException(e);
		} finally {

			try {
				c.close();
			} catch (Exception e) {
				// swallow
			}
		}

		return ks;
	}

	SecretKey createAESSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(128); // limit to 128 to get around export controls
		return kg.generateKey();
	}

	public void createKeyStoreIfNotPresent() {
		Closer closer = Closer.create();
		try {
			FileObject keyStoreLocation = getKeyStoreLocation();
			if (!keyStoreLocation.exists()) {
				
				KeyStore ks = KeyStore.getInstance("JCEKS");
				ks.load(null, getKeyStorePassword());

				String keyAlias = "mac0";
				ks.setKeyEntry(keyAlias, createAESSecretKey(),
						getPasswordForKey(keyAlias), null);

				OutputStream out = keyStoreLocation.getContent().getOutputStream();
				closer.register(out);
			
				ks.store(out, getKeyStorePassword());

			}
		} catch (GeneralSecurityException e) {
			throw new ConfigurationException(e);
		} catch (IOException e) {
			throw new ConfigurationException(e);
		} finally {
			try {
				closer.close();
			} catch (Exception IGNORE) {
			}
		}
	}
}
