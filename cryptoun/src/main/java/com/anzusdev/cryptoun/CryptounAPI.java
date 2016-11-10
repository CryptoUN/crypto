package com.anzusdev.cryptoun;

import java.util.HashMap;
import java.util.List;

import com.anzusdev.cryptoun.businesslogic.aes.AES.Type;
import com.anzusdev.cryptoun.businesslogic.ca.UCAMethod;
import com.anzusdev.cryptoun.businesslogic.cfb.CFBMethod;
import com.anzusdev.cryptoun.businesslogic.cfb.CFB.Algorithm;
import com.anzusdev.cryptoun.businesslogic.des.DESMethod;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/
@Api(name = "criptoun", version = "1",
scopes = {Constants.EMAIL_SCOPE },
clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
description = "API for intro to cryptography" )
public class CryptounAPI {
	
	@ApiMethod(name = "encryptDES", path = "encryptDES", httpMethod = HttpMethod.GET)
	public HashMap<String, String> encryptDES(@Named("plaintext") String plainText) {
		DESMethod dm = new DESMethod();
		return dm.encryptDES(plainText);
	}
	
	@ApiMethod(name = "decryptDES", path = "decryptDES", httpMethod = HttpMethod.GET)
	public HashMap<String, String> decryptDES(@Named("ciphertext") String cipherText, @Named("desKeyText") String desKeyText) {
		DESMethod dm = new DESMethod();
		return dm.decryptDES(cipherText, desKeyText);
	}
	
	@ApiMethod(name = "encryptCFBPI", path = "encryptCFBPI", httpMethod = HttpMethod.GET)
	public HashMap<String, String[]> encryptCFBPI(@Named("plaintext") String plainText) {
		CFBMethod cfb = new CFBMethod();
		return cfb.encryptCFB(plainText, Algorithm.PI, null);
	}
	
	@ApiMethod(name = "decryptCFBPI", path = "decryptCFBPI", httpMethod = HttpMethod.GET)
	public HashMap<String, String[]> decryptCFBPI(@Named("ciphertext") String cipherText, @Named("iv") String ivBits, @Named("plainSize") int plainSize) {
		CFBMethod cfb = new CFBMethod();
		return cfb.decryptCFB(cipherText, ivBits, "", plainSize, Algorithm.PI, null, null);
	}
	
	@ApiMethod(name = "encryptCFBDES", path = "encryptCFBDES", httpMethod = HttpMethod.GET)
	public HashMap<String, String[]> encryptCFBDES(@Named("plaintext") String plainText) {
		CFBMethod cfb = new CFBMethod();
		return cfb.encryptCFB(plainText, Algorithm.DES, null);
	}
	
	@ApiMethod(name = "decryptCFBDES", path = "decryptCFBDES", httpMethod = HttpMethod.GET)
	public HashMap<String, String[]> decryptCFBDES(@Named("ciphertext") String cipherText, @Named("iv") String ivBits, @Named("desKeyText") String desKeyText) {
		CFBMethod cfb = new CFBMethod();
		return cfb.decryptCFB(cipherText, ivBits, desKeyText, cipherText.length(), Algorithm.DES, null, null);
	}
	
	@ApiMethod(name = "encryptCFBAES", path = "encryptCFBAES", httpMethod = HttpMethod.GET)
	public HashMap<String, String[]> encryptCFBAES(@Named("plaintext") String plainText, @Named("type") Type type) {
		CFBMethod cfb = new CFBMethod();
		return cfb.encryptCFB(plainText, Algorithm.AES, type);
	}
	
	@ApiMethod(name = "decryptCFBAES", path = "decryptCFBAES", httpMethod = HttpMethod.GET)
	public HashMap<String, String[]> decryptCFBAES(@Named("ciphertext") String cipherText, @Named("iv") String ivBits, @Named("type") Type type, @Named("aesKeyText") String aesKeyText) {
		CFBMethod cfb = new CFBMethod();
		return cfb.decryptCFB(cipherText, ivBits, null, cipherText.length(), Algorithm.AES, type, aesKeyText);
	}
	
	@ApiMethod(name = "evolveUCA", path = "evolveUCA", httpMethod = HttpMethod.GET)
	public List<String> evolveUCA(@Named("rule") int rule, @Named("numBits") int numBits, @Named("iterations") int iterations) {
		UCAMethod uca = new UCAMethod();
		return uca.evolve(rule, numBits, iterations);
	}
	
	@ApiMethod(name = "evolveUCAR", path = "evolveUCAR", httpMethod = HttpMethod.GET)
	public List<String> evolveUCAR(@Named("rule") int rule, @Named("numBits") int numBits, @Named("iterations") int iterations) {
		UCAMethod uca = new UCAMethod();
		return uca.evolveReversible(rule, numBits, iterations);
	}
	
}
