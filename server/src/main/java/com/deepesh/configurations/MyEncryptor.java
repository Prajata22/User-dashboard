package com.deepesh.configurations;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.jasypt.util.password.BasicPasswordEncryptor;

public class MyEncryptor {
	private String password = "RmMWIz+mRzMWINWZk/jg0ZGY1djFk==";
	private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	
	
//	private BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
	
	public MyEncryptor() {
		encryptor.setPassword(password);
		encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
	}

	public StandardPBEStringEncryptor getEncryptor() {
		return encryptor;
	}

	
}