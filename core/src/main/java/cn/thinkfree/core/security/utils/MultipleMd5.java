package cn.thinkfree.core.security.utils;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MultipleMd5 implements PasswordEncoder {

    private  MessageDigest messageDigest;

    public MultipleMd5(){
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String encode(CharSequence charSequence) {
        return md5(md5(charSequence));
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(md5(md5(charSequence)));
    }

    private String md5(CharSequence charSequence){
        char[] charArray = charSequence.toString().toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = messageDigest.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
//        hexValue.toString();
        return hexValue.toString();
    }

}
