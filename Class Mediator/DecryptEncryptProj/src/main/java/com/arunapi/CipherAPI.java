package com.arunapi;

public interface CipherAPI {

    String encrypt(String stringToBeEncrypted);

    String decrypt(String encryptedString);
}
