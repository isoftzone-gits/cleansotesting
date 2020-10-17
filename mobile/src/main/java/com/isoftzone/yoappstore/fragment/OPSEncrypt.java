package com.isoftzone.yoappstore.fragment;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

//Written By : Vijay Kumar Rajbhar
public class OPSEncrypt {
    public static byte[] iv = null;
    public static byte[] ivKEY = null;


    String res = "";
    private SecretKeySpec g_skeySpec = null;

    public OPSEncrypt() {
    }


    public Object decryptObj(String hexString) {
        Object responseObject = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(2, this.g_skeySpec, ivSpec);

            ByteArrayInputStream bis = new ByteArrayInputStream(hexString.getBytes());

            CipherInputStream cipherInputStream = new CipherInputStream(bis, cipher);
            ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);


            SealedObject sealedObject = (SealedObject) inputStream.readObject();

            responseObject = sealedObject.getObject(cipher);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "AESEncryptBase: Exception in Decrypt::" + e);
        }
        System.out.println("from decryption method");
        return responseObject;
    }


    public String asHex(byte[] bytes) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            retString.append(Integer.toHexString('?' + (bytes[i] & 0xFF))
                    .substring(1));
        }
        return retString.toString();
    }

    public SecretKeySpec readAESKey(String keyval) {
        SecretKeySpec skeySpec = null;
        int j = 0;
        int ecount = 0;
        byte[] key = new byte[16];
        try {
            String sekey = keyval;
            byte[] utf8Bytes = sekey.getBytes("UTF8");
            byte[] defaultBytes = sekey.getBytes();
            for (int i = 0; i < 16; ) {
                ecount = 0;
                while (j < utf8Bytes.length) {
                    if (i == j) {
                        ecount = 1;
                        break;
                    }
                }
                if (ecount == 1)
                    key[i] = utf8Bytes[i];
                i++;
                j++;
            }

            iv = key;
            skeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "AESEncryptBase:readAESKey Exception in readKey::" +
                            e);
        }
        return skeySpec;
    }

    public SecretKeySpec readAESEncKey(String keyval) {
        SecretKeySpec skeySpec = null;
        int j = 0;
        int ecount = 0;
        byte[] key = new byte[16];
        try {
            String sekey = keyval;
            byte[] utf8Bytes = sekey.getBytes("UTF8");
            byte[] defaultBytes = sekey.getBytes();
            for (int i = 0; i < 16; ) {
                ecount = 0;
                while (j < utf8Bytes.length) {
                    if (i == j) {
                        ecount = 1;
                        break;
                    }
                }
                if (ecount == 1)
                    key[i] = utf8Bytes[i];
                i++;
                j++;
            }

            ivKEY = key;
            skeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "AESEncryptBase:readAESKey Exception in readKey::" +
                            e);
        }
        return skeySpec;
    }
}
