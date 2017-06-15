package cn.xiaolulwr.isassistant.mac;

import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class MacCore {
	private static MacCore instance;
	private String algorithm;
	
	public static MacCore getInstance(String algorithm) {
		if(instance==null) {
			instance=new MacCore();
		}
		instance.algorithm=algorithm;
		return instance;
	}
	public String getMac(String message,char[] password) throws Exception {
		byte[] in=message.getBytes();
		Mac mac=Mac.getInstance(algorithm);
		SecretKey key=getKeyByPassword(password);
		mac.init(key);
		mac.update(in);
		byte[] out=mac.doFinal();
		return new HexBinaryAdapter().marshal(out);
	}
	public boolean checkMac(String message,char[] password,String value) throws Exception {
		String currentValue=getMac(message, password);
		return currentValue.equals(value);
	}
	public String getMac(File fileToMac,char[] password) throws Exception{
		FileInputStream fin=new FileInputStream(fileToMac);
		Mac mac=Mac.getInstance(algorithm);
		SecretKey key=getKeyByPassword(password);
		mac.init(key);
		byte[] buffer=new byte[1024];
		int n;
		while((n=fin.read(buffer))!=-1) {
			mac.update(buffer, 0, n);
		}
		byte[] out=mac.doFinal();
		fin.close();
		return new HexBinaryAdapter().marshal(out);
	}
	public boolean checkMac(File fileToCheck,char[] password,String value) throws Exception {
		String currentValue=getMac(fileToCheck, password);
		return currentValue.equals(value);
	}
	private SecretKey getKeyByPassword(char[] password) throws Exception{
		KeyGenerator generator=KeyGenerator.getInstance(algorithm);
		byte[] passwordValue=new String(password).getBytes();
		SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(passwordValue);
		generator.init(random);
		return generator.generateKey();
	}
}
