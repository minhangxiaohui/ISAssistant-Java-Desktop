package cn.xiaolulwr.isassistant.sign;

import java.io.File;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import cn.xiaolulwr.isassistant.common.KeyStoreManager;
/**
 * 数字签名类
 * @author 路伟饶
 *
 */
public class DigitalSignCore {
	private static DigitalSignCore instance;
	private KeyStoreManager manager;
	private String algorithm;
	private FileInputStream in;
	/**
	 * 获取数字签名类的实例
	 * @param algorithm
	 * 数字签名
	 * @param manager
	 * 密钥库管理对象
	 * @return
	 * 数字签名类的实例
	 */
	public static DigitalSignCore getInstance(String algorithm,KeyStoreManager manager) {
		if(instance==null) {
			instance=new DigitalSignCore();
		}
		instance.algorithm=algorithm;
		instance.manager=manager;
		return instance;
	}
	private DigitalSignCore(){}
	/**
	 * 对文件进行签名
	 * @param signFile
	 * 待签名的文件
	 * @return
	 * 数字签名值
	 * @throws Exception
	 */
	public String signFile(File signFile) throws Exception{
		openFile(signFile);
		PrivateKey kpr=manager.generateKeyPair(signFile.getName()+algorithm, algorithm);
		Signature signature=Signature.getInstance(algorithm);
		signature.initSign(kpr);
		byte[] buffer=new byte[1024];
		int n;
		while((n=in.read(buffer))!=-1) {
			signature.update(buffer,0,n);
		}
		byte[] signValue=signature.sign();
		return new HexBinaryAdapter().marshal(signValue);
	}
	/**
	 * 验证文件的数字签名
	 * @param verifyFile
	 * 待验证的文件
	 * @param signValue
	 * 文件的数字签名值
	 * @return
	 * 是否验证通过
	 * @throws Exception
	 */
	public boolean verifyFile(File verifyFile,String signValue) throws Exception{
		openFile(verifyFile);
		PublicKey kpub=manager.getPublicKeyFromCertificate(verifyFile.getName()+algorithm);
		Signature signature=Signature.getInstance(algorithm);
		signature.initVerify(kpub);
		byte[] buffer=new byte[1024];
		int n;
		while((n=in.read(buffer))!=-1) {
			signature.update(buffer,0,n);
		}
		try {
			return signature.verify(new HexBinaryAdapter().unmarshal(signValue));
		} catch (Exception e) {
			throw new Exception("签名验证失败");
		}
	}
	/**
	 * 打开文件，私有方法
	 * @param file
	 * 文件对象
	 * @throws Exception
	 */
	private void openFile(File file) throws Exception {
		in=new FileInputStream(file);
	}
}
