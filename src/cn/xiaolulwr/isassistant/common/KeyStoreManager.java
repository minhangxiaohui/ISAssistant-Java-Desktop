package cn.xiaolulwr.isassistant.common;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.crypto.SecretKey;

public class KeyStoreManager {
	private static KeyStoreManager instance;
	private FileInputStream in;
	private KeyStore keystoreInst;
	private char[] password;
	
	private KeyStoreManager() {}
	public KeyStoreManager getInstance(File file,char[] passwd) throws Exception{
		
		if(instance==null) {
			instance=new KeyStoreManager();
		}
		instance.keystoreInst=KeyStore.getInstance("JCEKS");
		instance.password=passwd.clone();
		in=new FileInputStream(file);
		keystoreInst.load(in, password);
		return instance;
	}
	
	public SecretKey getSymmetricKey(String entryName) throws Exception{
		
		KeyStore.ProtectionParameter protParam=new KeyStore.PasswordProtection(password);
		KeyStore.SecretKeyEntry keyEntry=(SecretKeyEntry)keystoreInst.getEntry(entryName, protParam);
		return keyEntry.getSecretKey();
	}
	
	public PrivateKey getPrivateKey(String entryName) throws Exception {
		
		KeyStore.ProtectionParameter protParam=new KeyStore.PasswordProtection(password);
		KeyStore.PrivateKeyEntry privateKeyEntry=(PrivateKeyEntry)keystoreInst.getEntry(entryName, protParam);
		return privateKeyEntry.getPrivateKey();
	}
	
	public PublicKey getPublicKeyFromCertificate(String entryName) throws Exception {
		
		X509Certificate cert=(X509Certificate)keystoreInst.getCertificate(entryName);
		return cert.getPublicKey();
	}
	
	public void clean() throws Exception {
		in.close();
	}
}

