package com.wy.insurance.box.backend.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class MD5Utils {

    private static final String EMPTY_STRING = "";

    public static byte[] getMD5Mac(byte[] bySourceByte) {
        byte[] byDisByte;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(bySourceByte);
            byDisByte = md.digest();
        } catch (NoSuchAlgorithmException n) {
            return (null);
        }
        return (byDisByte);
    }

    public static String bintoascii(byte[] bySourceByte) {
        int len, i;
        byte tb;
        char high, tmp, low;
        String result = EMPTY_STRING;
        len = bySourceByte.length;

        for (i = 0; i < len; i++) {
            tb = bySourceByte[i];
            tmp = (char) ((tb >>> 4) & 0x000f);
            if (tmp >= 10)
                high = (char) ('a' + tmp - 10);
            else
                high = (char) ('0' + tmp);
            result += high;
            tmp = (char) (tb & 0x000f);
            if (tmp >= 10)
                low = (char) ('a' + tmp - 10);
            else
                low = (char) ('0' + tmp);
            result += low;
        }
        return result;
    }

    public static String getMD5ofStr(String inbuf, String encoding) {
        if (StringUtils.isEmpty(inbuf)) {
            return EMPTY_STRING;
        }
        try {
            return bintoascii(getMD5Mac(inbuf.getBytes(encoding)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return EMPTY_STRING;
    }

    public static String createSignUsingMD5(String inbuf, String encoding) {
        return getMD5ofStr(inbuf, encoding).toLowerCase();
    }

    /**
     * MD5加密后转大写
     * @param inbuf
     * @param encoding
     * @return
     */
    public static String createSignUsingMD5WithUpper(String inbuf,String encoding) {
        return getMD5ofStr(inbuf, encoding).toUpperCase();
    }

    public static void main(String args[]) {
        String test = "5063dd6378434c118c4d704d5b815957";
        String online = "3787cfc4b00547c8902ea144d0066808";
        String str = test+
                "{\"cityCode\":\"441900\",\"engineNo\":\"G036138\",\"firstRegisterDate\":1347292800000,\"licenseNo\":\"粤S538C2\",\"operationCode\":271031,\"ownerName\":\"黄圣勇\",\"prodCode\":\"CIC0001\",\"transId\":\"557817f298084fb186f3318b4752f285\",\"vehicleFrameNo\":\"LVGBH51K6CG048299\",\"vehicleModelName\":\"丰田GTM7201RS轿车\"}";

        System.out.println(createSignUsingMD5(str,"UTF-8"));
        String token = "4C75FB71-EE4BFEB0-77001457-07A5EA3D";
        String timeStr = "20160627135321";
        String origin = "BaoXianShi";
        System.out.println("七个萝卜签名：");
        System.out.println(createSignUsingMD5(origin+token+timeStr,"UTF-8"));
    }
}
