package utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class EncryptionUtil {

    /***
     * 加密
     * @param content
     * @param
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getEncryptionStr(String content) {
        String key = "0000sl_010122940";
        try {
            key = AESCipher.getkeyLength16(key);
         //   BASE64Encoder encode = new BASE64Encoder();
         //   String base64 = encode.encode(content.getBytes());
            String string = AESCipher.aesEncryptString(content, key);
            string = AESCipher.aesEncryptString(string, "_ease_oa_#$%^&*(");
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 解密
     * @param content
     * @param
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDecryptStr(String content) {
        String key = "0000sl_010122940";
        try {
            key = AESCipher.getkeyLength16(key);
            String jm1 = AESCipher.aesDecryptString(content, "_ease_oa_#$%^&*(");
            String jm2 = AESCipher.aesDecryptString(jm1, key);

      //      byte b[] = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(jm2);
       //     String result = new String(b);
            return jm2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}