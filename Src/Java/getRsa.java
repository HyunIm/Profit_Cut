package votechain;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class getRsa {
	KeyPairGenerator keyPairGenerator;
	KeyFactory keyFactory;
	Cipher cipher;
	KeyPair keypair;
	
	public getRsa() throws NoSuchAlgorithmException, NoSuchPaddingException {
		super();
		this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		this.keyPairGenerator.initialize(512);
		this.keyFactory = KeyFactory.getInstance("RSA");
		this.cipher = Cipher.getInstance("RSA");
		this.keypair = keyPairGenerator.genKeyPair();
	}
	
	private KeyPair get_keyPair() {
		return keyPairGenerator.genKeyPair();
	}
	
	public Key get_public() {
		return this.keypair.getPublic();
	}
	
	public Key get_private() {
		return this.keypair.getPrivate();
	}
	
	private RSAPublicKeySpec get_publicSpec(Key publickey) throws InvalidKeySpecException {
		return this.keyFactory.getKeySpec(publickey, RSAPublicKeySpec.class);
	}
	
	private RSAPrivateKeySpec get_privateSpec(Key privatekey) throws InvalidKeySpecException {
		return this.keyFactory.getKeySpec(privatekey, RSAPrivateKeySpec.class);
	}
	
	public byte[] encryption(String input, PrivateKey privatekey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		KeyPair keypair = get_keyPair();
//		Key privatekey = get_private();
		this.cipher.init(Cipher.ENCRYPT_MODE, privatekey);
		byte[] arrCipherData = this.cipher.doFinal(input.getBytes());
		String encrypted = new String(arrCipherData);
		
		return arrCipherData;
	}
	
	public String decryption(String encrypted, PublicKey key) {
		try {
			KeyPair keypair = get_keyPair();
//			Key publickey = decode_publickey(key);
			byte[] encrypt = decode_base64(encrypted);
			this.cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] arrCipherData = this.cipher.doFinal(encrypt);
			String decrypted = new String(arrCipherData);
			
			return decrypted;
		} catch (Exception e) {
			return "false";
		}
	}
	
//	public HashMap pkToString(PrivateKey key) throws InvalidKeySpecException {
//		RSAPrivateKeySpec privatespec = get_privateSpec(key);
//		String privateKeyModulus = privatespec.getModulus().toString(16);
//		String privateKeyExponent = privatespec.getPrivateExponent().toString(16);
//		HashMap<String, String> keyset = new HashMap<>();
//		keyset.put("Modulus", privateKeyModulus);
//		keyset.put("Exponent", privateKeyExponent);
//		
//		return keyset;
//	}
//	
//	public PrivateKey stringToPk(HashMap keyset) throws InvalidKeySpecException {
//		BigInteger modulus = new BigInteger((String) keyset.get("Modulus"), 16);
//		BigInteger exponent = new BigInteger((String)keyset.get("Exponent"), 16);
//		RSAPrivateKeySpec pks = new RSAPrivateKeySpec(modulus, exponent);
//		
//		PrivateKey pk = this.keyFactory.generatePrivate(pks);
//		return pk;
//	}
	
	public Key decode_publickey(String key) throws InvalidKeySpecException {
		X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(decode_base64(key)); 
		PublicKey pk = this.keyFactory.generatePublic(x509Spec);
		return pk;
	}
	
	public Key decode_privateKey(String key) throws InvalidKeySpecException {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decode_base64(key));
		PrivateKey pk = this.keyFactory.generatePrivate(spec);
		return pk;
	}
	
	public String encode_base64(byte[] key) {
		Encoder encoder = Base64.getEncoder();
		String base64 = encoder.encodeToString(key);
		return base64;
	}
	
	public byte[] decode_base64(String key) {
		Decoder decoder = Base64.getDecoder();
		byte[] base64 = decoder.decode(key);
		return base64;
	}
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		getRsa rsa = new getRsa();
		String msg = "hello";
		getRsa rsa1 = new getRsa();
		PublicKey k = (PublicKey) rsa1.get_public();
		String pk = rsa.encode_base64(rsa.get_private().getEncoded());
		PrivateKey prk = (PrivateKey) rsa.decode_privateKey(pk);
		byte[] a = rsa.encryption(msg, prk);
		String b = rsa.decryption(rsa.encode_base64(a), k);
	}
	
	
}
