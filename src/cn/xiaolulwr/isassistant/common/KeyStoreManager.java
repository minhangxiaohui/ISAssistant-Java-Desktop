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
/**
 * 密钥库管理类，默认使用JCEKS密钥库
 * @author 路伟饶
 *
 */
public class KeyStoreManager {
	private static KeyStoreManager instance;
	private File keystoreFile;
	private KeyStore keystoreInst;
	private char[] password;
	
	private KeyStoreManager() {}
	/**
	 * 获得密钥库管理类的实例
	 * @return
	 * 返回密钥库管理类的实例
	 * @throws Exception
	 * 密钥库类型不正确
	 */
	public static KeyStoreManager getInstance() throws Exception{
		if(instance==null) {
			instance=new KeyStoreManager();
		}
		instance.keystoreInst=KeyStore.getInstance("JCEKS");
		return instance;
	}
	/**
	 * 从文件加载密钥库，该方法不会立即验证密码的正确性
	 * @param file
	 * 密钥库文件
	 * @param password
	 * 密钥库的密码，不会在此处验证密钥库密码的正确性
	 */
	public void openKeyStoreFromFile(File file,char[] password) {
		this.password=(char[]) password.clone();
		keystoreFile=file;
	}
	/**
	 * 创建一个新的密钥库并保存到文件
	 * @param file
	 * 密钥库文件
	 * @param password
	 * 密钥库的密码，此处不会进行二次验证，必须保证此密码的正确性
	 * @throws Exception
	 * 文件异常
	 */
	public void createKeyStore(File file,char[] password) throws Exception{
		this.password=(char[]) password.clone();
		keystoreFile=file;
		FileOutputStream out=new FileOutputStream(keystoreFile);
		keystoreInst.load(null, password);
		keystoreInst.store(out, password);
		out.close();
	}
	/**
	 * 从密钥库读取一个对称密码的密钥
	 * @param entryName
	 * 入口名称
	 * @return
	 * 构建好的密钥对象
	 * @throws Exception
	 * 入口不存在或者不是对称密码的密钥
	 */
	public SecretKey getSymmetricKey(String entryName) throws Exception{
		FileInputStream in=new FileInputStream(keystoreFile);
		keystoreInst.load(in, password);
		KeyStore.ProtectionParameter protParam=new KeyStore.PasswordProtection(password);
		KeyStore.SecretKeyEntry keyEntry=(SecretKeyEntry)keystoreInst.getEntry(entryName, protParam);
		SecretKey key=keyEntry.getSecretKey();
		in.close();
		return key;
	}
	/**
	 * 从密钥库获得私钥
	 * @param entryName
	 * 入口名称
	 * @return
	 * 构建好的私钥对象
	 * @throws Exception
	 * 入口不存在或者存储的不是私钥
	 */
	private PrivateKey getPrivateKey(String entryName) throws Exception {
		FileInputStream in=new FileInputStream(keystoreFile);
		keystoreInst.load(in, password);
		KeyStore.ProtectionParameter protParam=new KeyStore.PasswordProtection(password);
		KeyStore.PrivateKeyEntry privateKeyEntry=(PrivateKeyEntry)keystoreInst.getEntry(entryName, protParam);
		PrivateKey kpr=privateKeyEntry.getPrivateKey();
		in.close();
		return kpr;
	}
	/**
	 * 从数字证书中获得公钥
	 * @param entryName
	 * 入口名称
	 * @return
	 * 构建好的公钥对象
	 * @throws Exception
	 * 入口不存在或者存储的不是数字证书
	 */
	public PublicKey getPublicKeyFromCertificate(String entryName) throws Exception {
		FileInputStream in=new FileInputStream(keystoreFile);
		keystoreInst.load(in, password);
		X509Certificate cert=(X509Certificate)keystoreInst.getCertificate(entryName);
		PublicKey kpub=cert.getPublicKey();
		in.close();
		return kpub;
	}
	/**
	 * 生成密钥对并返回私钥
	 * @param entryName
	 * 入口名称
	 * @param algorithm
	 * 数字签名算法
	 * @return
	 * 构建好的私钥对象
	 * @throws Exception
	 */
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
			keystoreInst.setKeyEntry(entryName, pair.getPrivate(),password, new X509Certificate[]{cert});
			FileOutputStream out=new FileOutputStream(keystoreFile);
			keystoreInst.store(out, password);
			in.close();
			out.close();
			return pair.getPrivate();
		}
	}
}

