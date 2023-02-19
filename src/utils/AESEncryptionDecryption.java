/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mo22
 */
public class AESEncryptionDecryption {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";
            
    private MessageDigest sha;
    private Cipher cipher;
    
    private String secret;
    
    public AESEncryptionDecryption(String secret){
        this.secret = secret;
        this.cipherChoosing(this.ALGORITHM);
    }
    
    private void cipherChoosing(String algorithm){
        try {
            this.cipher = Cipher.getInstance(algorithm);
        } catch (Exception ex) {
            System.out.println("Error choosing cipher: " + ex.getMessage());
        }
    }
    
    private void prepareSecretKey(String myKey){
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String encrypt(String strToEncrypt){       
        try {
            prepareSecretKey(secret);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception ex) {
            System.out.println("Error while encrypting: " + ex.getMessage());
        }
        
        return "";
    }
    
    
    public String decrypt(String strToDecrypt){
        try {
            prepareSecretKey(secret);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception ex) {
            System.out.println("Error while decrypting:  " + ex.getMessage());
        }
        return "";
    }
}
