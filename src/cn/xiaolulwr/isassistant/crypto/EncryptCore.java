package cn.xiaolulwr.isassistant.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptCore {
	private static EncryptCore instance;
	private char[] password;
	private int keyLengthByByte;
	private static String encryptFileID="xiaolulwr";
	
	private FileInputStream inputStream;
	private FileOutputStream outputStream;
	private EncryptCore(){}
	
	public static EncryptCore getInstance(char[] password,int keyLengthByByte) {
		if(instance==null) {
			instance=new EncryptCore();
		}
		instance.password=password;
		instance.keyLengthByByte=keyLengthByByte;
		return instance;
	}
	
	public void encryptFile(File encryptfile) throws Exception {
		this.openFileToEncrypt(encryptfile);
		SecretKey key=getKeyByPassword();
		IvParameterSpec iv=getIvByCurrentTime();
		Cipher cipher=Cipher.getInstance("AES/CFB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE,key,iv);
		CipherInputStream ci=new CipherInputStream(inputStream, cipher);
		writeEncryptFile(ci);
	}

	private void openFileToEncrypt(File encryptfile) throws Exception{
		inputStream=new FileInputStream(encryptfile);
		String encryptFileName=encryptfile.getPath()+".xiaolulwrcrypto";
		outputStream=new FileOutputStream(encryptFileName);
		outputStream.write(encryptFileID.getBytes());
	}

	private SecretKey getKeyByPassword() throws Exception {
		MessageDigest messdig=MessageDigest.getInstance("SHA-256");
		String passwordString=new String(password);
		byte[] keyValue=messdig.digest(passwordString.getBytes());
		outputStream.write(keyLengthByByte);
		return new SecretKeySpec(keyValue,0,keyLengthByByte,"AES");
	}
	private IvParameterSpec getIvByCurrentTime() throws Exception {
		byte[] ivValue=new byte[16];
		Random random=new Random(System.currentTimeMillis());
		random.nextBytes(ivValue);
		outputStream.write(ivValue);
		return new IvParameterSpec(ivValue);
	}
	
	private void writeEncryptFile(CipherInputStream cin) throws Exception {
		byte[] buffer=new byte[1024];
		int n=0;
		while((n=cin.read(buffer)) !=-1) {
			outputStream.write(buffer, 0, n);
		}
		cin.close();
	}

	public void clean() throws IOException {
		inputStream.close();
		outputStream.close();
	} 
}

