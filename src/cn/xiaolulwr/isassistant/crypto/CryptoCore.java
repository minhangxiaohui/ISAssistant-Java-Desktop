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
/**
 * 文件加解密类
 * @author 路伟饶
 *
 */
public class CryptoCore {
	private static CryptoCore instance;
	private static String encryptFileID="xiaolulwr";
	
	private FileInputStream inputStream;
	private FileOutputStream outputStream;
	private CryptoCore(){}
	/**
	 * 获得文件加解密类的实例
	 * @return
	 * 返回文件加解密类实例
	 */
	public static CryptoCore getInstance() {
		if(instance==null) {
			instance=new CryptoCore();
		}
		return instance;
	}
	/**
	 * 加密文件的方法
	 * @param encryptFile
	 * 待加密的文件
	 * @param password
	 * 用于加密文件的密码
	 * @param keyLengthByByte
	 * 密钥长度，单位是字节
	 * @param outputFile
	 * 输出加密后的文件
	 * @throws Exception
	 */
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
	/**
	 * 解密文件的方法
	 * @param decryptFile
	 * 待解密的文件
	 * @param password
	 * 用于解密文件的密码
	 * @param outputFile
	 * 输出解密后的文件
	 * @throws Exception
	 */
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
	/**
	 * 打开文件以进行操作，私有方法
	 * @param decryptFile
	 * 用于输入的文件
	 * @param outputFile
	 * 用于输出的文件
	 * @throws Exception
	 */
	private void openFile(File inputFile,File outputFile) throws Exception {
		inputStream=new FileInputStream(inputFile);
		outputStream=new FileOutputStream(outputFile);
	}
	/**
	 * 通过密码获得密钥，私有方法
	 * @param password
	 * 用于获取密钥的密码
	 * @param keyLengthByByte
	 * 密钥长度，单位是字节
	 * @return
	 * 构建好的密钥对象
	 * @throws Exception
	 */
	private SecretKey getKeyByPassword(char[] password,int keyLengthByByte) throws Exception {
		KeyGenerator generator=KeyGenerator.getInstance("AES");
		byte[] passwordValue=new String(password).getBytes();
		SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(passwordValue);
		generator.init(keyLengthByByte*8, random);
		return generator.generateKey();
	}
	/**
	 * 获得随机的初始化向量，私有方法
	 * @return
	 * 初始化向量对象
	 * @throws Exception
	 */
	private IvParameterSpec getRandomIv() throws Exception {
		byte[] ivValue=new byte[16];
		Random random=new Random(System.currentTimeMillis());
		random.nextBytes(ivValue);
		outputStream.write(ivValue);
		return new IvParameterSpec(ivValue);
	}
	/**
	 * 从加密的文件中获得初始化向量，私有方法
	 * @return
	 * 初始化向量对象
	 * @throws Exception
	 */
	private IvParameterSpec getIvByEncryptFile() throws Exception {
		byte[] ivValue=new byte[16];
		inputStream.read(ivValue);
		return new IvParameterSpec(ivValue);
	}
	/**
	 * 写入加密后的文件，私有方法
	 * @param cin
	 * 带Cipher对象的文件输入流
	 * @throws Exception
	 */
	private void writeEncryptFile(CipherInputStream cin) throws Exception {
		byte[] buffer=new byte[1024];
		int n=0;
		while((n=cin.read(buffer)) !=-1) {
			outputStream.write(buffer, 0, n);
		}
		cin.close();
	}
	/**
	 * 写入解密后的文件
	 * @param cin
	 * 带Cipher对象的文件输入流
	 * @throws Exception
	 * 密码错误或文件损坏
	 */
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
	/**
	 * 清理对象，关闭输入输出流
	 * @throws IOException
	 */
	public void clean() throws IOException {
		inputStream.close();
		outputStream.close();
	} 
}

