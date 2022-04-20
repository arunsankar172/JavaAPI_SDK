package com.arunapi;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AES256Cipher implements CipherAPI {

    private static String SECRET_KEY = "secret";
    private static String SALT = "salt";
    private static byte[] INIT_VECTOR = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public String encrypt(String stringToBeEncrypted) {

        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    factory.generateSecret(
                            new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256))
                            .getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(INIT_VECTOR));
            return Base64
                    .getEncoder()
                    .encodeToString(cipher.doFinal(stringToBeEncrypted.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting the string : " + stringToBeEncrypted, e);
        }
    }

    @Override
    public String decrypt(String encryptedString) {
        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(INIT_VECTOR));
            return new String(cipher.doFinal(Base64
                    .getDecoder()
                    .decode(encryptedString.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (Exception e) {

            throw new RuntimeException("Error while decrypting: " + encryptedString, e);
        }
    }
}
