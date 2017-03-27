package ru.air.parser;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kapa on 26.10.16.
 */
public class TestCommon {

    public static void main(String[] args) {

        String cipher = "uqWIzOvclP4Np3eIHFtW89qLk3p58H9z8I+spJ976NY=";
        String key = "fc+qILPqqaCwaTUsZ87SiNn1cVn4k9siWAnxboYqhJI=";
        String iv = "MGU8/JR7TUDwAWek4vaY9g==";

        System.out.println(decrypt(cipher, key, iv));


    }

    public static String decrypt(String decodedText, String key, String iv)  {
        try{

            byte[] decodedKey = Base64.getDecoder().decode(key);
            byte[] decodedIv  = Base64.getDecoder().decode(iv);

            javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(decodedKey, "AES");
            javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(decodedIv);

            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] decrypted = cipher.doFinal(decodedText.getBytes());

            String str = new String(decrypted);

            return str;

        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
