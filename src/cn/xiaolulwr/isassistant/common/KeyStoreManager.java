package cn.xiaolulwr.isassistant.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.sql.Date;

import javax.crypto.SecretKey;

import org.bouncycastle.asn1.x500.X500Name;

public class KeyStoreManager {
	private static KeyStoreManager instance;
	private FileInputStream in;
	private FileOutputStream out;
	private KeyStore keystoreInst;
	private boolean isOpened;
	private char[] password;
	
	private KeyStoreManager() {}
	public static KeyStoreManager getInstance() throws Exception{
		if(instance==null) {
			instance=new KeyStoreManager();
		}
		instance.keystoreInst=KeyStore.getInstance("JCEKS");
		return instance;
	}
	public void openKeyStoreFromFile(File file,char[] password) throws Exception{
		this.password=password.clone();
		in=new FileInputStream(file);
		keystoreInst.load(in, password);
		isOpened=true;
	}
	public void createKeyStore(File file,char[] password) throws Exception{
		this.password=password.clone();
		keystoreInst.load(null);
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
	public PrivateKey generateKeyPair(String entryName,String algorithm) throws Exception {
		long nowTime=System.currentTimeMillis();
		Date nowDate=new Date(nowTime);
		X500Name name=new X500Name("CN = Lu OU = IS O = CAUC L = Dongli S = Tianjin C = CN");
		BigInteger certSerialNo=new BigInteger(Long.toString(nowTime));
		return null;
	}
	public void clean() throws Exception {
		isOpened=false;
		in.close();
		out.close();
	}
	public boolean isOpened() {
		return isOpened;
	}
}

