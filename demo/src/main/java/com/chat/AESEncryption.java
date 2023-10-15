package com.chat;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;



public class AESEncryption {

    private static final String ALGORITHM = "AES";
    private static final String KEY_FILE_PATH = "aes_key.txt";  // Path to the key file
    private SecretKey secretKey;

    public AESEncryption() {
        try {
            Path keyFilePath = Paths.get(KEY_FILE_PATH);

            if (Files.exists(keyFilePath)) {
                // Key file exists, read the key from the file
                byte[] keyBytes = Files.readAllBytes(keyFilePath);
                this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            } else {
                // Key file does not exist, generate a new key and save it
                generateAndSaveKey();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateAndSaveKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128);
            this.secretKey = keyGenerator.generateKey();

            // Save the key to the file
            Files.write(Paths.get(KEY_FILE_PATH), secretKey.getEncoded());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String plaintext) throws Exception {
        SecretKey key = getKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
    }

    public String decrypt(byte[] ciphertext) throws Exception {
        SecretKey key = getKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public SecretKey getKey() throws Exception {
        if (secretKey == null) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128); // You can change the key size (128, 192, or 256 bits)
            secretKey = keyGenerator.generateKey();
        }
        return secretKey;
    }
    public SecretKey shareKey() throws Exception {
        SecretKey secretKey = getKey();
        return secretKey;
    }
}
