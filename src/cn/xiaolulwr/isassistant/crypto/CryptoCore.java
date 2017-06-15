package cn.xiaolulwr.isassistant.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CryptoCore {
	private static CryptoCore instance;
	private static String encryptFileID="xiaolulwr";
	
	private FileInputStream inputStream;
	private FileOutputStream outputStream;
	private CryptoCore(){}

	public static CryptoCore getInstance() {
		if(instance==null) {
			instance=new CryptoCore();
		}
		return instance;
	}
	public void encryptFile(File encryptFile,char[] password,int keyLengthByByte,File outputFile) throws Exception {
		openFile(encryptFile,outputFile);
		outputStream.write(encryptFileID.getBytes());
		SecretKey key=getKeyByPassword(password,keyLengthByByte);
		outputStream.write(keyLengthByByte);
		IvParameterSpec iv=getRandomIv();
		Cipher cipher=Cipher.getInstance("AES/CFB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE,key,iv);
		CipherInputStream ci=new CipherInputStream(inputStream, cipher);
		writeEncryptFile(ci);
	}
	public void decryptFile(File decryptFile,char[] password,File outputFile) throws Exception{
		openFile(decryptFile,outputFile);
		byte[] idValue=new byte[encryptFileID.getBytes().length];
		inputStream.read(idValue);
		String idToCheck=new String(idValue);
		if(!idToCheck.equals(encryptFileID)) {
			throw new Exception("文件格式错误");
		}
		int keyLengthByByte=inputStream.read();
		SecretKey key=getKeyByPassword(password,keyLengthByByte);
		IvParameterSpec iv=getIvByEncryptFile();
		Cipher cipher=Cipher.getInstance("AES/CFB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE,key,iv);
		CipherInputStream ci=new CipherInputStream(inputStream, cipher);
		writeDecryptFile(ci);
	}
	private void openFile(File decryptFile,File outputFile) throws Exception {
		inputStream=new FileInputStream(decryptFile);
		outputStream=new FileOutputStream(outputFile);
	}
	private SecretKey getKeyByPassword(char[] password,int keyLengthByByte) throws Exception {
		KeyGenerator generator=KeyGenerator.getInstance("AES");
		byte[] passwordValue=new String(password).getBytes();
		SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(passwordValue);
		generator.init(keyLengthByByte*8, random);
		return generator.generateKey();
	}
	private IvParameterSpec getRandomIv() throws Exception {
		byte[] ivValue=new byte[16];
		Random random=new Random(System.currentTimeMillis());
		random.nextBytes(ivValue);
		outputStream.write(ivValue);
		return new IvParameterSpec(ivValue);
	}
	private IvParameterSpec getIvByEncryptFile() throws Exception {
		byte[] ivValue=new byte[16];
		inputStream.read(ivValue);
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
	public void clean() throws IOException {
		inputStream.close();
		outputStream.close();
	} 
}

