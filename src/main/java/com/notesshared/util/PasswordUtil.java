package com.notesshared.util;
import java.security.*;import java.util.Base64;import javax.crypto.*;import javax.crypto.spec.PBEKeySpec;
public final class PasswordUtil{
    private static final int ITER=120000,KEY=256,SALT=16;private static final SecureRandom RANDOM=new SecureRandom();private PasswordUtil(){}
    public static String hash(String p){byte[] s=new byte[SALT];RANDOM.nextBytes(s);return ITER+"$"+Base64.getEncoder().encodeToString(s)+"$"+Base64.getEncoder().encodeToString(derive(p.toCharArray(),s,ITER));}
    public static boolean verify(String p,String stored){try{String[] x=stored.split("\\$",-1);if(x.length!=3)return false;byte[] a=Base64.getDecoder().decode(x[2]);byte[] b=derive(p.toCharArray(),Base64.getDecoder().decode(x[1]),Integer.parseInt(x[0]));return MessageDigest.isEqual(a,b);}catch(Exception e){return false;}}
    private static byte[] derive(char[] p,byte[] s,int i){try{return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(p,s,i,KEY)).getEncoded();}catch(GeneralSecurityException e){throw new IllegalStateException(e);}}
}
