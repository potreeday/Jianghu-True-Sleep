package game.xh.indie.com.jianghu.util;

public class MyEncrypt {
    //final Base64.Decoder decoder = Base64.getDecoder();
    //final Base64.Encoder encoder = Base64.getEncoder();

    //简单的加密算法
    public static String getEncryptStr(String strOld){
        //将字符串转化为字符数组
        char[] chars= strOld.toCharArray();
        //逐字符进行加密
        for(int i=0;i<chars.length;i++){
            chars[i] = (char)(chars[i] + i/10 +1);
        }
        String strNew=String.valueOf(chars);
        return strNew;
    }

    //解密算法
    public  static String getDecryptStr(String strNew){
        //将字符串转化为字符数组
        char[] chars= strNew.toCharArray();
        //逐字符进行解密
        for(int i=0;i<chars.length;i++){
            chars[i] = (char)(chars[i] - i/10 -1);
        }
        String strOld=String.valueOf(chars);
        return strOld;
    }

}
