package cn.xiaolulwr.isassistant.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecryptCore {
	private static DecryptCore instance;
	private char[] password;
	private static String encryptFileID="xiaolulwr";
	
	private FileInputStream inputStream;
	private FileOutputStream outputStream;
	private DecryptCore(){}
	
	public static DecryptCore getInstance(char[] passwd) {
		if(instance==null) {
			instance=new DecryptCore();
		}
		instance.password=passwd;
		return instance;
	}
	public void decryptFile(File decryptfile,File outputPath) throws Exception{
		openFileToDecrypt(decryptfile,outputPath);
		byte[] idValue=new byte[encryptFileID.getBytes().length];
		inputStream.read(idValue);
		String idToCheck=new String(idValue);
		if(!idToCheck.equals(encryptFileID)) {
			throw new Exception("文件格式错误");
		}
		SecretKey key=getKeyByPassword();
		IvParameterSpec iv=getIvByEncryptFile();
		Cipher cipher=Cipher.getInstance("AES/CFB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE,key,iv);
		CipherInputStream ci=new CipherInputStream(inputStream, cipher);
		writeDecryptFile(ci);
	}
	
	private void openFileToDecrypt(File decryptFile,File outputPath) throws Exception {
		inputStream=new FileInputStream(decryptFile);
		outputStream=new FileOutputStream(outputPath);
	}
	
	private SecretKey getKeyByPassword() throws Exception {
		MessageDigest messdig=MessageDigest.getInstance("SHA-256");
		String passwordString=new String(password);
		byte[] keyValue=messdig.digest(passwordString.getBytes());
		int keyLengthByByte=inputStream.read();
		return new SecretKeySpec(keyValue,0,keyLengthByByte,"AES");
	}
	private IvParameterSpec getIvByEncryptFile() throws Exception {
		byte[] ivValue=new byte[16];
		inputStream.read(ivValue);
		return new IvParameterSpec(ivValue);
	}
	private void writeDecryptFile(CipherInputStream cin) throws Exception {
		byte[] buffer=new byte[1024];
		int n=0;
		try {
			while((n=cin.read(buffer)) !=-1) {
				outputStream.write(buffer, 0, n);
			}
		} 
		catch (Exception e) {
			throw new Exception("密码错误或文件损坏");
		}
		cin.close();
	}
	public void clean() throws Exception {
		inputStream.close();
		outputStream.close();
	} 
}
