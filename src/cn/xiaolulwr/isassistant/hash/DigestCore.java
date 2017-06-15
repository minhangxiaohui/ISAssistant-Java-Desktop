package cn.xiaolulwr.isassistant.hash;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class DigestCore {
	private static DigestCore instance;
	private String algorithm;
	
	public static DigestCore getInstance(String algorithm) {
		if(instance==null) {
			instance=new DigestCore();
		}
		instance.algorithm=algorithm;
		return instance;
	}
	private DigestCore(){}
	
	public String getHashValue(String message) throws Exception {
		byte[] in=message.getBytes();
		MessageDigest md=MessageDigest.getInstance(algorithm);
		byte[] out=md.digest(in);
		return new HexBinaryAdapter().marshal(out);
	}
	public boolean checkHashValue(String message,String checksum) throws Exception{
		String hashValue=this.getHashValue(message);
		return hashValue.equals(checksum);
	}
	public String getHashValue(File fileToHash) throws Exception {
		FileInputStream in=new FileInputStream(fileToHash);
		MessageDigest md=MessageDigest.getInstance(algorithm);
		DigestInputStream din=new DigestInputStream(in, md);
		byte[] buffer=new byte[1024];
		while((din.read(buffer))!=-1);
		byte[] out=md.digest();
		din.close();
		return new HexBinaryAdapter().marshal(out);
	}

	public boolean checkHashValue(File fileToHash,String checksum) throws Exception{
		String hashValue=this.getHashValue(fileToHash);
		return hashValue.equals(checksum);
	}
}
