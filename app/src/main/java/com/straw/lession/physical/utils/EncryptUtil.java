package com.straw.lession.physical.utils;

import com.straw.lession.physical.codec.binary.Base64;
import com.straw.lession.physical.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class EncryptUtil {

    private final static String CHARSET = "utf-8";

    public static String sha512(String originalText) {
        if (originalText == null)
            return null;
        return DigestUtils.sha512Hex(originalText);
    }

    public static String MD5(String originalText) {
        if (originalText == null)
            return null;
        return DigestUtils.md5Hex(originalText);
    }

    public static String base64Encode(String originalText) throws UnsupportedEncodingException {
        if (originalText == null)
            return null;
        return Base64.encodeBase64String(originalText.getBytes("UTF-8"));
    }

    public static String base64Decode(String text) throws UnsupportedEncodingException {
        if (text == null)
            return null;
        return new String(Base64.decodeBase64(text), "UTF-8");
    }

    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    public static final String SALT_KEY = "creditease_2";

    /**
     * DES算法，加密
     * 
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws UnsupportedEncodingException
     *             异常
     */
    public static String desEncrypt(String data, String key) {
        if (null == key || key.length() < 8) {
            throw new RuntimeException("key length must > 8");
        }
        try {
            return desEncrypt(data.getBytes(CHARSET), key);
        } catch (Exception UnsupportedEncodingException) {
            return null;
        }
    }

    /**
     * DES算法，加密
     * 
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     *             异常
     */
    private static String desEncrypt(byte[] data, String key) {

        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes(CHARSET));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("********".getBytes(CHARSET));
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);
            //return new BASE64Encoder().encode(bytes);    //step1
            byte[] bytesd = new Base64().encode(bytes);
            return new String(bytesd);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * DES算法，解密
     * 
     * @param data
     *            待解密字符串
     * @param key
     *            解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception
     *             异常
     */
    private static byte[] desDecrypt(byte[] data, String key) {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("********".getBytes(CHARSET));
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取编码后的值
     * 
     * @param key
     * @param data
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String desDecrypt(String data, String key) {
        String value = null;
        try {
            //byte[] datas = desDecrypt(new BASE64Decoder().decodeBuffer(data), key);  // //step1

            byte[] datas = desDecrypt(new Base64().decode(data), key);
            value = new String(datas);
        } catch (Exception e) {
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(desEncrypt("dafd{阿@all，服务平台接口做如下修改：宜搜接口修改见V1.1。并已提交至SVN，请相关同学及时更新查阅，谢谢！薛公坚(29840332)  19:22:21公:\"aaa\"}", "creditease_2"));
        System.out.println(desDecrypt("zJIINQnd/8BZhurKETSzNsNook1o4BDlAB526PQwFRuief4CkL127VI2aLJhB++aX0IA6bRZsp/3oLv0NvKNZrcEdEOwe/nsdIaBkTHEc4I2L6DFyL8LS4Y8iB1U5Q162ZRTQQU9hvoE1G2tsrp31Ba/P58ckm+/87i6SoyZ4ydcrbkmWrBBreeD8DQ7DxIBJ2RVypSyI7VjOgB51JmPsCFvNtZH2NMQ44TG0W3hYIwNY5zaXFE1VzX27oMUtaW9", "creditease_2"));

        // String[] ids = new String[] { "827610076220001"};
        // for (String id : ids) {
        // System.out.println(id + " " + EncryptUtil.MD5("207" + id +
        // "falsea1b0xp3443231=ushw84&sf23^#@lfjasldf"));
        // }
        // String code = "userName=18321136147&email=1755601633333333@qq.com";
        // String urlLink="userId=81&source=2";
        // String
        // url="http://localhost/lingtou/index.shtml?sign=dXNlcklkPTgxJnNvdXJjZT0y";
        // try {
        // System.out.println(sha512("test138"));
        // System.out.println(base64Encode(urlLink));
        // System.out.println(base64Decode("dXNlcmlkPTEwMDAwNzY0NjkzMyZjb2RlPTkzMzIwMDAwMTkyMTY4NyZhbW91bnQ9NS4w"));
        // System.out.println(base64Encode(code));
        // System.out
        // .println(base64Decode("YZzbMihAlmocW-zGjh7xCpLIN-KdRau2*Wvlsg3ulqg_"));
        // System.out.println(code.indexOf("userName="));
        // System.out.println(code.indexOf("&email="));
        // System.out.println(code.substring(code.indexOf("userName=")
        // + ("userName=").length(), code.indexOf("&email=")));
        // System.out.println(code.substring(code.indexOf("&email=")
        // + ("&email=").length(), code.length()));
        //
        // System.out.println("------>"+MD5("abcd1234"));
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // try {
        // String
        // code="ZFhObGNtbGtQVEV3TURBd056WTBOamt6TXlaaWFYcHZjbVJsY2s1dlBUa3pNekl3TURBd05EZ3dOek16TXlaaGJXOTFiblE5TVRBd0xqQT0";
        // String checkCode="607c20d7a5121bdbd087c0ba8c351a5a";
        // String MD5Code=MD5(code+"heyuebao");
        // System.out.println(base64Decode(base64Decode(code)));
        // System.out.println(MD5Code);
        // System.out.println(MD5Code.equals(checkCode));
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

}
