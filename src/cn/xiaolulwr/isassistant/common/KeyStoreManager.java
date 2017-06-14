package cn.xiaolulwr.isassistant.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Calendar;
import javax.crypto.SecretKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class KeyStoreManager {
	private static KeyStoreManager instance;
	private File keystoreFile;
	private KeyStore keystoreInst;
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
		keystoreFile=file;
	}
	public void createKeyStore(File file,char[] password) throws Exception{
		this.password=password.clone();
		keystoreFile=file;
		FileOutputStream out=new FileOutputStream(keystoreFile);
		keystoreInst.load(null, password);
		keystoreInst.store(out, password);
		out.close();
	}
	public SecretKey getSymmetricKey(String entryName) throws Exception{
		FileInputStream in=new FileInputStream(keystoreFile);
		keystoreInst.load(in, password);
		KeyStore.ProtectionParameter protParam=new KeyStore.PasswordProtection(password);
		KeyStore.SecretKeyEntry keyEntry=(SecretKeyEntry)keystoreInst.getEntry(entryName, protParam);
		SecretKey key=keyEntry.getSecretKey();
		in.close();
		return key;
	}
	private PrivateKey getPrivateKey(String entryName) throws Exception {
		FileInputStream in=new FileInputStream(keystoreFile);
		keystoreInst.load(in, password);
		KeyStore.ProtectionParameter protParam=new KeyStore.PasswordProtection(password);
		KeyStore.PrivateKeyEntry privateKeyEntry=(PrivateKeyEntry)keystoreInst.getEntry(entryName, protParam);
		PrivateKey kpr=privateKeyEntry.getPrivateKey();
		in.close();
		return kpr;
	}
	public PublicKey getPublicKeyFromCertificate(String entryName) throws Exception {
		FileInputStream in=new FileInputStream(keystoreFile);
		keystoreInst.load(in, password);
		X509Certificate cert=(X509Certificate)keystoreInst.getCertificate(entryName);
		PublicKey kpub=cert.getPublicKey();
		in.close();
		return kpub;
	}
	public PrivateKey generateKeyPair(String entryName,String algorithm) throws Exception {
		try {
			return this.getPrivateKey(entryName);
		} 
		catch (Exception e) {
			FileInputStream in=new FileInputStream(keystoreFile);
			try {
				keystoreInst.load(in, password);
			} catch (Exception exc) {
				throw new Exception("密钥库不合法或密码错误");
			}
			KeyPairGenerator generator=KeyPairGenerator.getInstance(algorithm.split("with")[1]);
			KeyPair pair=generator.generateKeyPair();
			long nowTime=System.currentTimeMillis();
			Date nowDate=new Date(nowTime);
			Calendar cal=Calendar.getInstance();
			cal.setTime(nowDate);
			cal.add(Calendar.YEAR, 1);
			Date endDate=cal.getTime();
			X500Name name=new X500Name("CN = Lu OU = IS O = CAUC L = Dongli S = Tianjin C = CN");
			BigInteger certSerialNo=new BigInteger(Long.toString(nowTime));
			ContentSigner signer=new JcaContentSignerBuilder(algorithm).build(pair.getPrivate());
			JcaX509v3CertificateBuilder certbuilder=
					new JcaX509v3CertificateBuilder
					(name, certSerialNo, nowDate, endDate, name, pair.getPublic());
			BasicConstraints basicConstraints = new BasicConstraints(true);
			certbuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints);
			X509Certificate cert=
					new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(certbuilder.build(signer));
			keystoreInst.setKeyEntry(entryName, pair.getPrivate().getEncoded(), new X509Certificate[]{cert});
			FileOutputStream out=new FileOutputStream(keystoreFile);
			keystoreInst.store(out, password);
			in.close();
			out.close();
			return pair.getPrivate();
		}
	}
}

