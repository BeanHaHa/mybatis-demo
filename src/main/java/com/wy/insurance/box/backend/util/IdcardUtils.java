package com.wy.insurance.box.backend.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 身份证工具类
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public enum IdcardUtils {
    INSTANCE;
    /** 中国公民身份证号码最小长度。 */
    public static final int CHINA_ID_MIN_LENGTH = 15;
 
    /** 中国公民身份证号码最大长度。 */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /** 性别男 */
    public static final int GENDER_MALE = 1;
    /* 性别女 */
    public static final int GENDER_FEMALE = 2;

    /** 省、直辖市代码表 */
    public static final String cityCode[] = {
            "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
            "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
            "81", "82", "91"
    };
 
    /** 每位加权因子 */
    public static final int power[] = {
            7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };
 
    /** 第18位校检码 */
    public static final String verifyCode[] = {
            "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
    };
    /** 最低年限 */
    public static final int MIN = 1930;
    public static Map<String, String> cityCodes = new HashMap<String, String>();
    /** 台湾身份首字母对应数字 */
    public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();
    /** 香港身份首字母对应数字 */
    public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
    static {
        cityCodes.put("11", "北京");
        cityCodes.put("12", "天津");
        cityCodes.put("13", "河北");
        cityCodes.put("14", "山西");
        cityCodes.put("15", "内蒙古");
        cityCodes.put("21", "辽宁");
        cityCodes.put("22", "吉林");
        cityCodes.put("23", "黑龙江");
        cityCodes.put("31", "上海");
        cityCodes.put("32", "江苏");
        cityCodes.put("33", "浙江");
        cityCodes.put("34", "安徽");
        cityCodes.put("35", "福建");
        cityCodes.put("36", "江西");
        cityCodes.put("37", "山东");
        cityCodes.put("41", "河南");
        cityCodes.put("42", "湖北");
        cityCodes.put("43", "湖南");
        cityCodes.put("44", "广东");
        cityCodes.put("45", "广西");
        cityCodes.put("46", "海南");
        cityCodes.put("50", "重庆");
        cityCodes.put("51", "四川");
        cityCodes.put("52", "贵州");
        cityCodes.put("53", "云南");
        cityCodes.put("54", "西藏");
        cityCodes.put("61", "陕西");
        cityCodes.put("62", "甘肃");
        cityCodes.put("63", "青海");
        cityCodes.put("64", "宁夏");
        cityCodes.put("65", "新疆");
        cityCodes.put("71", "台湾");
        cityCodes.put("81", "香港");
        cityCodes.put("82", "澳门");
        cityCodes.put("91", "国外");
        twFirstCode.put("A", 10);
        twFirstCode.put("B", 11);
        twFirstCode.put("C", 12);
        twFirstCode.put("D", 13);
        twFirstCode.put("E", 14);
        twFirstCode.put("F", 15);
        twFirstCode.put("G", 16);
        twFirstCode.put("H", 17);
        twFirstCode.put("J", 18);
        twFirstCode.put("K", 19);
        twFirstCode.put("L", 20);
        twFirstCode.put("M", 21);
        twFirstCode.put("N", 22);
        twFirstCode.put("P", 23);
        twFirstCode.put("Q", 24);
        twFirstCode.put("R", 25);
        twFirstCode.put("S", 26);
        twFirstCode.put("T", 27);
        twFirstCode.put("U", 28);
        twFirstCode.put("V", 29);
        twFirstCode.put("X", 30);
        twFirstCode.put("Y", 31);
        twFirstCode.put("W", 32);
        twFirstCode.put("Z", 33);
        twFirstCode.put("I", 34);
        twFirstCode.put("O", 35);
        hkFirstCode.put("A", 1);
        hkFirstCode.put("B", 2);
        hkFirstCode.put("C", 3);
        hkFirstCode.put("R", 18);
        hkFirstCode.put("U", 21);
        hkFirstCode.put("Z", 26);
        hkFirstCode.put("X", 24);
        hkFirstCode.put("W", 23);
        hkFirstCode.put("O", 15);
        hkFirstCode.put("N", 14);
    }
 
    /**
     * 将15位身份证号码转换为18位
     * 
     * @param idCard
     *            15位身份编码
     * @return 18位身份编码
     */
    public String conver15CardTo18(String idCard) {
        String idCard18 = "";
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isNum(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null)
                cal.setTime(birthDate);
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            if (cArr != null) {
                int[] iCard = converCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                // 获取校验位
                String sVal = getCheckCode18(iSum17);
                if (sVal.length() > 0) {
                    idCard18 += sVal;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return idCard18;
    }
 
    /**
     * 验证身份证是否合法
     */
    public boolean validateCard(String idCard) {
        String card = idCard.trim();
        if (validateIdCard18(card)) {
            return true;
        }
        if (validateIdCard15(card)) {
            return true;
        }
        String[] cardval = validateIdCard10(card);
        if (cardval != null) {
            if (cardval[2].equals("true")) {
                return true;
            }
        }
        return false;
    }
 
    /**
     * 验证18位身份编码是否合法
     * 
     * @param idCard 身份编码
     * @return 是否合法
     */
    public boolean validateIdCard18(String idCard) {
        boolean bTrue = false;
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            // 前17位
            String code17 = idCard.substring(0, 17);
            // 第18位
            String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
            if (isNum(code17)) {
                char[] cArr = code17.toCharArray();
                if (cArr != null) {
                    int[] iCard = converCharToInt(cArr);
                    int iSum17 = getPowerSum(iCard);
                    // 获取校验位
                    String val = getCheckCode18(iSum17);
                    if (val.length() > 0) {
                        if (val.equalsIgnoreCase(code18)) {
                            bTrue = true;
                        }
                    }
                }
            }
        }
        return bTrue;
    }
 
    /**
     * 验证15位身份编码是否合法
     * 
     * @param idCard
     *            身份编码
     * @return 是否合法
     */
    public boolean validateIdCard15(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return false;
        }
        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (cityCodes.get(proCode) == null) {
                return false;
            }
            String birthCode = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null)
                cal.setTime(birthDate);
            if (!valiDate(cal.get(Calendar.YEAR), Integer.valueOf(birthCode.substring(2, 4)),
                    Integer.valueOf(birthCode.substring(4, 6)))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
 
    /**
     * 验证10位身份编码是否合法
     * 
     * @param idCard 身份编码
     * @return 身份证信息数组
     *         <p>
     *         [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
     *         若不是身份证件号码则返回null
     *         </p>
     */
    public String[] validateIdCard10(String idCard) {
        String[] info = new String[3];
        String card = idCard.replaceAll("[\\(|\\)]", "");
        if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
            return null;
        }
        if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            info[0] = "台湾";
            System.out.println("11111");
            String char2 = idCard.substring(1, 2);
            if (char2.equals("1")) {
                info[1] = "M";
                System.out.println("MMMMMMM");
            } else if (char2.equals("2")) {
                info[1] = "F";
                System.out.println("FFFFFFF");
            } else {
                info[1] = "N";
                info[2] = "false";
                System.out.println("NNNN");
                return info;
            }
            info[2] = validateTWCard(idCard) ? "true" : "false";
        } else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            info[0] = "澳门";
            info[1] = "N";
            // TODO
        } else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = validateHKCard(idCard) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }
 
    /**
     * 验证台湾身份证号码
     * 
     * @param idCard
     *            身份证号码
     * @return 验证码是否符合
     */
    public boolean validateTWCard(String idCard) {
        String start = idCard.substring(0, 1);
        String mid = idCard.substring(1, 9);
        String end = idCard.substring(9, 10);
        Integer iStart = twFirstCode.get(start);
        Integer sum = iStart / 10 + (iStart % 10) * 9;
        char[] chars = mid.toCharArray();
        Integer iflag = 8;
        for (char c : chars) {
            sum = sum + Integer.valueOf(c + "") * iflag;
            iflag--;
        }
        return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true : false;
    }
 
    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     * </p>
     * 
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    public boolean validateHKCard(String idCard) {
        String card = idCard.replaceAll("[\\(|\\)]", "");
        Integer sum = 0;
        if (card.length() == 9) {
            sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9
                    + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        Integer iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.valueOf(c + "") * iflag;
            iflag--;
        }
        if (end.toUpperCase().equals("A")) {
            sum = sum + 10;
        } else {
            sum = sum + Integer.valueOf(end);
        }
        return (sum % 11 == 0) ? true : false;
    }
 
    /**
     * 将字符数组转换成数字数组
     * 
     * @param ca
     *            字符数组
     * @return 数字数组
     */
    public int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return iArr;
    }
 
    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     * 
     * @param iArr
     * @return 身份证编码。
     */
    public int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (power.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < power.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * power[j];
                    }
                }
            }
        }
        return iSum;
    }
 
    /**
     * 将power和值与11取模获得余数进行校验码判断
     * 
     * @param iSum
     * @return 校验位
     */
    public String getCheckCode18(int iSum) {
        String sCode = "";
        switch (iSum % 11) {
        case 10:
            sCode = "2";
            break;
        case 9:
            sCode = "3";
            break;
        case 8:
            sCode = "4";
            break;
        case 7:
            sCode = "5";
            break;
        case 6:
            sCode = "6";
            break;
        case 5:
            sCode = "7";
            break;
        case 4:
            sCode = "8";
            break;
        case 3:
            sCode = "9";
            break;
        case 2:
            sCode = "x";
            break;
        case 1:
            sCode = "0";
            break;
        case 0:
            sCode = "1";
            break;
        }
        return sCode;
    }
 
    /**
     * 根据身份编号获取年龄
     * 
     * @param idCard
     *            身份编号
     * @return 年龄
     */
    public int getAgeByIdCard(String idCard) {
        int iAge = 0;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String year = idCard.substring(6, 10);
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }
 
    /**
     * 根据身份编号获取生日
     * 
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public String getBirthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份证获取生日
     * @param idCard
     * @return 生日(yyyy-MM-dd)
     */
    public String getBirthWithSpeLineByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
    }
 
    /**
     * 根据身份编号获取生日年
     * 
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public Short getYearByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(6, 10));
    }
 
    /**
     * 根据身份编号获取生日月
     * 
     * @param idCard
     *            身份编号
     * @return 生日(MM)
     */
    public Short getMonthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(10, 12));
    }
 
    /**
     * 根据身份编号获取生日天
     * 
     * @param idCard
     *            身份编号
     * @return 生日(dd)
     */
    public Short getDateByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(12, 14));
    }
 
    /**
     * 根据身份编号获取性别
     * 
     * @param idCard 身份编号
     * @return 性别(1-男，2-女)
     */
    public Integer getGenderByIdCard(String idCard) {
        Integer sGender = null;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = GENDER_MALE;
        } else {
            sGender = GENDER_FEMALE;
        }
        return sGender;
    }
 
    /**
     * 根据身份编号获取户籍省份
     * 
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public String getProvinceByIdCard(String idCard) {
        int len = idCard.length();
        String sProvince = null;
        String sProvinNum = "";
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            sProvinNum = idCard.substring(0, 2);
        }
        sProvince = cityCodes.get(sProvinNum);
        return sProvince;
    }
 
    /**
     * 数字验证
     * 
     * @param val
     * @return 提取的数字。
     */
    public boolean isNum(String val) {
        return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
    }
 
    /**
     * 验证小于当前日期 是否有效
     * 
     * @param iYear
     *            待验证日期(年)
     * @param iMonth
     *            待验证日期(月 1-12)
     * @param iDate
     *            待验证日期(日)
     * @return 是否有效
     */
    public boolean valiDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < MIN || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }
        switch (iMonth) {
        case 4:
        case 6:
        case 9:
        case 11:
            datePerMonth = 30;
            break;
        case 2:
            boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
                    && (iYear > MIN && iYear < year);
            datePerMonth = dm ? 29 : 28;
            break;
        default:
            datePerMonth = 31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }

    public int getAgeByBirthday(String birthday) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(birthday);
        } catch (ParseException e) {
        }

        Calendar cal = Calendar.getInstance();
        if (cal.before(date)) {
            return -1;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(date);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    public Long getLongAgeByBirthday(String birthday) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(birthday);
        } catch (ParseException e) {
        }
        return date.getTime();
    }

    /**
     * 随机生成身份证号码
     *
     * @return
     */
    public String getRandomIdNo() {
        String idNo = "330106199111060016";
        // 随机生成省、自治区、直辖市代码 1-2
        String provinces[] = { "11", "12", "13", "14", "15", "21", "22", "23",
                "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
                "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
                "63", "64", "65", "71", "81", "82" };
        String areas = returnAreaJson();
//        String province = randomOne(provinces);
//        // 随机生成地级市、盟、自治州代码 3-4
//        String city = randomCityCode(18);
//        // 随机生成县、县级市、区代码 5-6
//        String county = randomCityCode(28);
        JSONObject areaJson = JSON.parseObject(areas);
        JSONArray areaList = areaJson.getJSONArray("areas");
        JSONObject areaObj = (JSONObject) areaList.get((int)(Math.random() * (areaList.size()-1)));
        String area = areaObj.getString("code");
        // 过滤最后两位为"00"的地区码
        while(isAreaLagal(area)) {
            areaObj = (JSONObject) areaList.get((int)(Math.random() * (areaList.size()-1)));
            area = areaObj.getString("code");
        }
        // 随机生成出生年月 7-14
        String birth = randomBirth(20, 50);
        // 随机生成顺序号 15-17(随机性别)
        String no = new Random().nextInt(899) + 100+"";
        String notIdNo = area + birth + no;
        // 随机生成校验码 18

        // 拼接身份证号码
        idNo =  notIdNo + generateCheck(notIdNo);

        return idNo;
    }

    /**
     * 判断地区码最后两位是否00
     * @param area
     * @return
     */
    public Boolean isAreaLagal(String area) {
        String lastTwoStr = area.substring(area.length()-2, area.length());
        return "00".equals(lastTwoStr);
    }
    public String returnAreaJson() {
        StringBuffer area = new StringBuffer();
        area.append("{\"areas\":[{\"code\":\"110000\",\"name\":\"北京市\"},{\"code\":\"110100\",\"name\":\"市辖区\"},{\"code\":\"110101\",\"name\":\"东城区\"},{\"code\":\"110102\",\"name\":\"西城区\"},{\"code\":\"110103\",\"name\":\"崇文区\"},{\"code\":\"110104\",\"name\":\"宣武区\"},{\"code\":\"110105\",\"name\":\"朝阳区\"},{\"code\":\"110106\",\"name\":\"丰台区\"},{\"code\":\"110107\",\"name\":\"石景山区\"},{\"code\":\"110108\",\"name\":\"海淀区\"},{\"code\":\"110109\",\"name\":\"门头沟区\"},{\"code\":\"110111\",\"name\":\"房山区\"},{\"code\":\"110112\",\"name\":\"通州区\"},{\"code\":\"110113\",\"name\":\"顺义区\"},{\"code\":\"110200\",\"name\":\"县\"},{\"code\":\"110221\",\"name\":\"昌平县\"},{\"code\":\"110224\",\"name\":\"大兴县\"},{\"code\":\"110226\",\"name\":\"平谷县\"},{\"code\":\"110227\",\"name\":\"怀柔县\"},{\"code\":\"110228\",\"name\":\"密云县\"},{\"code\":\"110229\",\"name\":\"延庆县\"},{\"code\":\"120000\",\"name\":\"天津市\"},{\"code\":\"120100\",\"name\":\"市辖区\"},{\"code\":\"120101\",\"name\":\"和平区\"},{\"code\":\"120102\",\"name\":\"河东区\"},{\"code\":\"120103\",\"name\":\"河西区\"},{\"code\":\"120104\",\"name\":\"南开区\"},{\"code\":\"120105\",\"name\":\"河北区\"},{\"code\":\"120106\",\"name\":\"红桥区\"},{\"code\":\"120107\",\"name\":\"塘沽区\"},{\"code\":\"120108\",\"name\":\"汉沽区\"},{\"code\":\"120109\",\"name\":\"大港区\"},{\"code\":\"120110\",\"name\":\"东丽区\"},{\"code\":\"120111\",\"name\":\"西青区\"},{\"code\":\"120112\",\"name\":\"津南区\"},{\"code\":\"120113\",\"name\":\"北辰区\"},{\"code\":\"120200\",\"name\":\"县\"},{\"code\":\"120221\",\"name\":\"宁河县\"},{\"code\":\"120222\",\"name\":\"武清县\"},{\"code\":\"120223\",\"name\":\"静海县\"},{\"code\":\"120224\",\"name\":\"宝坻县\"},{\"code\":\"120225\",\"name\":\"蓟县\"},{\"code\":\"130000\",\"name\":\"河北省\"},{\"code\":\"130100\",\"name\":\"石家庄市\"},{\"code\":\"130101\",\"name\":\"市辖区\"},{\"code\":\"130102\",\"name\":\"长安区\"},{\"code\":\"130103\",\"name\":\"桥东区\"},{\"code\":\"130104\",\"name\":\"桥西区\"},{\"code\":\"130105\",\"name\":\"新华区\"},{\"code\":\"130106\",\"name\":\"郊区\"},{\"code\":\"130107\",\"name\":\"井陉矿区\"},{\"code\":\"130121\",\"name\":\"井陉县\"},{\"code\":\"130123\",\"name\":\"正定县\"},{\"code\":\"130124\",\"name\":\"栾城县\"},{\"code\":\"130125\",\"name\":\"行唐县\"},{\"code\":\"130126\",\"name\":\"灵寿县\"},{\"code\":\"130127\",\"name\":\"高邑县\"},{\"code\":\"130128\",\"name\":\"深泽县\"},{\"code\":\"130129\",\"name\":\"赞皇县\"},{\"code\":\"130130\",\"name\":\"无极县\"},{\"code\":\"130131\",\"name\":\"平山县\"},{\"code\":\"130132\",\"name\":\"元氏县\"},{\"code\":\"130133\",\"name\":\"赵县\"},{\"code\":\"130181\",\"name\":\"辛集市\"},{\"code\":\"130182\",\"name\":\"藁城市\"},{\"code\":\"130183\",\"name\":\"晋州市\"},{\"code\":\"130184\",\"name\":\"新乐市\"},{\"code\":\"130185\",\"name\":\"鹿泉市\"},{\"code\":\"130200\",\"name\":\"唐山市\"},{\"code\":\"130201\",\"name\":\"市辖区\"},{\"code\":\"130202\",\"name\":\"路南区\"},{\"code\":\"130203\",\"name\":\"路北区\"},{\"code\":\"130204\",\"name\":\"古冶区\"},{\"code\":\"130205\",\"name\":\"开平区\"},{\"code\":\"130206\",\"name\":\"新区\"},{\"code\":\"130221\",\"name\":\"丰润县\"},{\"code\":\"130223\",\"name\":\"滦县\"},{\"code\":\"130224\",\"name\":\"滦南县\"},{\"code\":\"130225\",\"name\":\"乐亭县\"},{\"code\":\"130227\",\"name\":\"迁西县\"},{\"code\":\"130229\",\"name\":\"玉田县\"},{\"code\":\"130230\",\"name\":\"唐海县\"},{\"code\":\"130281\",\"name\":\"遵化市\"},{\"code\":\"130282\",\"name\":\"丰南市\"},{\"code\":\"130283\",\"name\":\"迁安市\"},{\"code\":\"130300\",\"name\":\"秦皇岛市\"},{\"code\":\"130301\",\"name\":\"市辖区\"},{\"code\":\"130302\",\"name\":\"海港区\"},{\"code\":\"130303\",\"name\":\"山海关区\"},{\"code\":\"130304\",\"name\":\"北戴河区\"},{\"code\":\"130321\",\"name\":\"青龙满族自治县\"},{\"code\":\"130322\",\"name\":\"昌黎县\"},{\"code\":\"130323\",\"name\":\"抚宁县\"},{\"code\":\"130324\",\"name\":\"卢龙县\"},{\"code\":\"130400\",\"name\":\"邯郸市\"},{\"code\":\"130401\",\"name\":\"市辖区\"},{\"code\":\"130402\",\"name\":\"邯山区\"},{\"code\":\"130403\",\"name\":\"丛台区\"},{\"code\":\"130404\",\"name\":\"复兴区\"},{\"code\":\"130406\",\"name\":\"峰峰矿区\"},{\"code\":\"130421\",\"name\":\"邯郸县\"},{\"code\":\"130423\",\"name\":\"临漳县\"},{\"code\":\"130424\",\"name\":\"成安县\"},{\"code\":\"130425\",\"name\":\"大名县\"},{\"code\":\"130426\",\"name\":\"涉县\"},{\"code\":\"130427\",\"name\":\"磁县\"},{\"code\":\"130428\",\"name\":\"肥乡县\"},{\"code\":\"130429\",\"name\":\"永年县\"},{\"code\":\"130430\",\"name\":\"邱县\"},{\"code\":\"130431\",\"name\":\"鸡泽县\"},{\"code\":\"130432\",\"name\":\"广平县\"},{\"code\":\"130433\",\"name\":\"馆陶县\"},{\"code\":\"130434\",\"name\":\"魏县\"},{\"code\":\"130435\",\"name\":\"曲周县\"},{\"code\":\"130481\",\"name\":\"武安市\"},{\"code\":\"130500\",\"name\":\"邢台市\"},{\"code\":\"130501\",\"name\":\"市辖区\"},{\"code\":\"130502\",\"name\":\"桥东区\"},{\"code\":\"130503\",\"name\":\"桥西区\"},{\"code\":\"130521\",\"name\":\"邢台县\"},{\"code\":\"130522\",\"name\":\"临城县\"},{\"code\":\"130523\",\"name\":\"内丘县\"},{\"code\":\"130524\",\"name\":\"柏乡县\"},{\"code\":\"130525\",\"name\":\"隆尧县\"},{\"code\":\"130526\",\"name\":\"任县\"},{\"code\":\"130527\",\"name\":\"南和县\"},{\"code\":\"130528\",\"name\":\"宁晋县\"},{\"code\":\"130529\",\"name\":\"巨鹿县\"},{\"code\":\"130530\",\"name\":\"新河县\"},{\"code\":\"130531\",\"name\":\"广宗县\"},{\"code\":\"130532\",\"name\":\"平乡县\"},{\"code\":\"130533\",\"name\":\"威县\"},{\"code\":\"130534\",\"name\":\"清河县\"},{\"code\":\"130535\",\"name\":\"临西县\"},{\"code\":\"130581\",\"name\":\"南宫市\"},{\"code\":\"130582\",\"name\":\"沙河市\"},{\"code\":\"130600\",\"name\":\"保定市\"},{\"code\":\"130601\",\"name\":\"市辖区\"},{\"code\":\"130602\",\"name\":\"新市区\"},{\"code\":\"130603\",\"name\":\"北市区\"},{\"code\":\"130604\",\"name\":\"南市区\"},{\"code\":\"130621\",\"name\":\"满城县\"},{\"code\":\"130622\",\"name\":\"清苑县\"},{\"code\":\"130623\",\"name\":\"涞水县\"},{\"code\":\"130624\",\"name\":\"阜平县\"},{\"code\":\"130625\",\"name\":\"徐水县\"},{\"code\":\"130626\",\"name\":\"定兴县\"},{\"code\":\"130627\",\"name\":\"唐县\"},{\"code\":\"130628\",\"name\":\"高阳县\"},{\"code\":\"130629\",\"name\":\"容城县\"},{\"code\":\"130630\",\"name\":\"涞源县\"},{\"code\":\"130631\",\"name\":\"望都县\"},{\"code\":\"130632\",\"name\":\"安新县\"},{\"code\":\"130633\",\"name\":\"易县\"},{\"code\":\"130634\",\"name\":\"曲阳县\"},{\"code\":\"130635\",\"name\":\"蠡县\"},{\"code\":\"130636\",\"name\":\"顺平县\"},{\"code\":\"130637\",\"name\":\"博野县\"},{\"code\":\"130638\",\"name\":\"雄县\"},{\"code\":\"130681\",\"name\":\"涿州市\"},{\"code\":\"130682\",\"name\":\"定州市\"},{\"code\":\"130683\",\"name\":\"安国市\"},{\"code\":\"130684\",\"name\":\"高碑店市\"},{\"code\":\"130700\",\"name\":\"张家口市\"},{\"code\":\"130701\",\"name\":\"市辖区\"},{\"code\":\"130702\",\"name\":\"桥东区\"},{\"code\":\"130703\",\"name\":\"桥西区\"},{\"code\":\"130705\",\"name\":\"宣化区\"},{\"code\":\"130706\",\"name\":\"下花园区\"},{\"code\":\"130721\",\"name\":\"宣化县\"},{\"code\":\"130722\",\"name\":\"张北县\"},{\"code\":\"130723\",\"name\":\"康保县\"},{\"code\":\"130724\",\"name\":\"沽源县\"},{\"code\":\"130725\",\"name\":\"尚义县\"},{\"code\":\"130726\",\"name\":\"蔚县\"},{\"code\":\"130727\",\"name\":\"阳原县\"},{\"code\":\"130728\",\"name\":\"怀安县\"},{\"code\":\"130729\",\"name\":\"万全县\"},{\"code\":\"130730\",\"name\":\"怀来县\"},{\"code\":\"130731\",\"name\":\"涿鹿县\"},{\"code\":\"130732\",\"name\":\"赤城县\"},{\"code\":\"130733\",\"name\":\"崇礼县\"},{\"code\":\"130800\",\"name\":\"承德市\"},{\"code\":\"130801\",\"name\":\"市辖区\"},{\"code\":\"130802\",\"name\":\"双桥区\"},{\"code\":\"130803\",\"name\":\"双滦区\"},{\"code\":\"130804\",\"name\":\"鹰手营子矿区\"},{\"code\":\"130821\",\"name\":\"承德县\"},{\"code\":\"130822\",\"name\":\"兴隆县\"},{\"code\":\"130823\",\"name\":\"平泉县\"},{\"code\":\"130824\",\"name\":\"滦平县\"},{\"code\":\"130825\",\"name\":\"隆化县\"},{\"code\":\"130826\",\"name\":\"丰宁满族自治县\"},{\"code\":\"130827\",\"name\":\"宽城满族自治县\"},{\"code\":\"130828\",\"name\":\"围场满族蒙古族自治县\"},{\"code\":\"130900\",\"name\":\"沧州市\"},{\"code\":\"130901\",\"name\":\"市辖区\"},{\"code\":\"130902\",\"name\":\"新华区\"},{\"code\":\"130903\",\"name\":\"运河区\"},{\"code\":\"130921\",\"name\":\"沧县\"},{\"code\":\"130922\",\"name\":\"青县\"},{\"code\":\"130923\",\"name\":\"东光县\"},{\"code\":\"130924\",\"name\":\"海兴县\"},{\"code\":\"130925\",\"name\":\"盐山县\"},{\"code\":\"130926\",\"name\":\"肃宁县\"},{\"code\":\"130927\",\"name\":\"南皮县\"},{\"code\":\"130928\",\"name\":\"吴桥县\"},{\"code\":\"130929\",\"name\":\"献县\"},{\"code\":\"130930\",\"name\":\"孟村回族自治县\"},{\"code\":\"130981\",\"name\":\"泊头市\"},{\"code\":\"130982\",\"name\":\"任丘市\"},{\"code\":\"130983\",\"name\":\"黄骅市\"},{\"code\":\"130984\",\"name\":\"河间市\"},{\"code\":\"131000\",\"name\":\"廊坊市\"},{\"code\":\"131001\",\"name\":\"市辖区\"},{\"code\":\"131002\",\"name\":\"安次区\"},{\"code\":\"131022\",\"name\":\"固安县\"},{\"code\":\"131023\",\"name\":\"永清县\"},{\"code\":\"131024\",\"name\":\"香河县\"},{\"code\":\"131025\",\"name\":\"大城县\"},{\"code\":\"131026\",\"name\":\"文安县\"},{\"code\":\"131028\",\"name\":\"大厂回族自治县\"},{\"code\":\"131081\",\"name\":\"霸州市\"},{\"code\":\"131082\",\"name\":\"三河市\"},{\"code\":\"131100\",\"name\":\"衡水市\"},{\"code\":\"131101\",\"name\":\"市辖区\"},{\"code\":\"131102\",\"name\":\"桃城区\"},{\"code\":\"131121\",\"name\":\"枣强县\"},{\"code\":\"131122\",\"name\":\"武邑县\"},{\"code\":\"131123\",\"name\":\"武强县\"},{\"code\":\"131124\",\"name\":\"饶阳县\"},{\"code\":\"131125\",\"name\":\"安平县\"},{\"code\":\"131126\",\"name\":\"故城县\"},{\"code\":\"131127\",\"name\":\"景县\"},{\"code\":\"131128\",\"name\":\"阜城县\"},{\"code\":\"131181\",\"name\":\"冀州市\"},{\"code\":\"131182\",\"name\":\"深州市\"},{\"code\":\"140000\",\"name\":\"山西省\"},{\"code\":\"140100\",\"name\":\"太原市\"},{\"code\":\"140101\",\"name\":\"市辖区\"},{\"code\":\"140105\",\"name\":\"小店区\"},{\"code\":\"140106\",\"name\":\"迎泽区\"},{\"code\":\"140107\",\"name\":\"杏花岭区\"},");
        area.append("{\"code\":\"140108\",\"name\":\"尖草坪区\"},{\"code\":\"140109\",\"name\":\"万柏林区\"},{\"code\":\"140110\",\"name\":\"晋源区\"},{\"code\":\"140121\",\"name\":\"清徐县\"},{\"code\":\"140122\",\"name\":\"阳曲县\"},{\"code\":\"140123\",\"name\":\"娄烦县\"},{\"code\":\"140181\",\"name\":\"古交市\"},{\"code\":\"140200\",\"name\":\"大同市\"},{\"code\":\"140201\",\"name\":\"市辖区\"},{\"code\":\"140202\",\"name\":\"城区\"},{\"code\":\"140203\",\"name\":\"矿区\"},{\"code\":\"140211\",\"name\":\"南郊区\"},{\"code\":\"140212\",\"name\":\"新荣区\"},{\"code\":\"140221\",\"name\":\"阳高县\"},{\"code\":\"140222\",\"name\":\"天镇县\"},{\"code\":\"140223\",\"name\":\"广灵县\"},{\"code\":\"140224\",\"name\":\"灵丘县\"},{\"code\":\"140225\",\"name\":\"浑源县\"},{\"code\":\"140226\",\"name\":\"左云县\"},{\"code\":\"140227\",\"name\":\"大同县\"},{\"code\":\"140300\",\"name\":\"阳泉市\"},{\"code\":\"140301\",\"name\":\"市辖区\"},{\"code\":\"140302\",\"name\":\"城区\"},{\"code\":\"140303\",\"name\":\"矿区\"},{\"code\":\"140311\",\"name\":\"郊区\"},{\"code\":\"140321\",\"name\":\"平定县\"},{\"code\":\"140322\",\"name\":\"盂县\"},{\"code\":\"140400\",\"name\":\"长治市\"},{\"code\":\"140401\",\"name\":\"市辖区\"},{\"code\":\"140402\",\"name\":\"城区\"},{\"code\":\"140411\",\"name\":\"郊区\"},{\"code\":\"140421\",\"name\":\"长治县\"},{\"code\":\"140423\",\"name\":\"襄垣县\"},{\"code\":\"140424\",\"name\":\"屯留县\"},{\"code\":\"140425\",\"name\":\"平顺县\"},{\"code\":\"140426\",\"name\":\"黎城县\"},{\"code\":\"140427\",\"name\":\"壶关县\"},{\"code\":\"140428\",\"name\":\"长子县\"},{\"code\":\"140429\",\"name\":\"武乡县\"},{\"code\":\"140430\",\"name\":\"沁县\"},{\"code\":\"140431\",\"name\":\"沁源县\"},{\"code\":\"140481\",\"name\":\"潞城市\"},{\"code\":\"140500\",\"name\":\"晋城市\"},{\"code\":\"140501\",\"name\":\"市辖区\"},{\"code\":\"140502\",\"name\":\"城区\"},{\"code\":\"140521\",\"name\":\"沁水县\"},{\"code\":\"140522\",\"name\":\"阳城县\"},{\"code\":\"140524\",\"name\":\"陵川县\"},{\"code\":\"140525\",\"name\":\"泽州县\"},{\"code\":\"140581\",\"name\":\"高平市\"},{\"code\":\"140600\",\"name\":\"朔州市\"},{\"code\":\"140601\",\"name\":\"市辖区\"},{\"code\":\"140602\",\"name\":\"朔城区\"},{\"code\":\"140603\",\"name\":\"平鲁区\"},{\"code\":\"140621\",\"name\":\"山阴县\"},{\"code\":\"140622\",\"name\":\"应县\"},{\"code\":\"140623\",\"name\":\"右玉县\"},{\"code\":\"140624\",\"name\":\"怀仁县\"},{\"code\":\"142200\",\"name\":\"忻州地区\"},{\"code\":\"142201\",\"name\":\"忻州市\"},{\"code\":\"142202\",\"name\":\"原平市\"},{\"code\":\"142222\",\"name\":\"定襄县\"},{\"code\":\"142223\",\"name\":\"五台县\"},{\"code\":\"142225\",\"name\":\"代县\"},{\"code\":\"142226\",\"name\":\"繁峙县\"},{\"code\":\"142227\",\"name\":\"宁武县\"},{\"code\":\"142228\",\"name\":\"静乐县\"},{\"code\":\"142229\",\"name\":\"神池县\"},{\"code\":\"142230\",\"name\":\"五寨县\"},{\"code\":\"142231\",\"name\":\"岢岚县\"},{\"code\":\"142232\",\"name\":\"河曲县\"},{\"code\":\"142233\",\"name\":\"保德县\"},{\"code\":\"142234\",\"name\":\"偏关县\"},{\"code\":\"142300\",\"name\":\"吕梁地区\"},{\"code\":\"142301\",\"name\":\"孝义市\"},{\"code\":\"142302\",\"name\":\"离石市\"},{\"code\":\"142303\",\"name\":\"汾阳市\"},{\"code\":\"142322\",\"name\":\"文水县\"},{\"code\":\"142323\",\"name\":\"交城县\"},{\"code\":\"142325\",\"name\":\"兴县\"},{\"code\":\"142326\",\"name\":\"临县\"},{\"code\":\"142327\",\"name\":\"柳林县\"},{\"code\":\"142328\",\"name\":\"石楼县\"},{\"code\":\"142329\",\"name\":\"岚县\"},{\"code\":\"142330\",\"name\":\"方山县\"},{\"code\":\"142332\",\"name\":\"中阳县\"},{\"code\":\"142333\",\"name\":\"交口县\"},{\"code\":\"142400\",\"name\":\"晋中地区\"},{\"code\":\"142401\",\"name\":\"榆次市\"},{\"code\":\"142402\",\"name\":\"介休市\"},{\"code\":\"142421\",\"name\":\"榆社县\"},{\"code\":\"142422\",\"name\":\"左权县\"},{\"code\":\"142423\",\"name\":\"和顺县\"},{\"code\":\"142424\",\"name\":\"昔阳县\"},{\"code\":\"142427\",\"name\":\"寿阳县\"},{\"code\":\"142429\",\"name\":\"太谷县\"},{\"code\":\"142430\",\"name\":\"祁县\"},{\"code\":\"142431\",\"name\":\"平遥县\"},{\"code\":\"142433\",\"name\":\"灵石县\"},{\"code\":\"142600\",\"name\":\"临汾地区\"},{\"code\":\"142601\",\"name\":\"临汾市\"},{\"code\":\"142602\",\"name\":\"侯马市\"},{\"code\":\"142603\",\"name\":\"霍州市\"},{\"code\":\"142621\",\"name\":\"曲沃县\"},{\"code\":\"142622\",\"name\":\"翼城县\"},{\"code\":\"142623\",\"name\":\"襄汾县\"},{\"code\":\"142625\",\"name\":\"洪洞县\"},{\"code\":\"142627\",\"name\":\"古县\"},{\"code\":\"142628\",\"name\":\"安泽县\"},{\"code\":\"142629\",\"name\":\"浮山县\"},{\"code\":\"142630\",\"name\":\"吉县\"},{\"code\":\"142631\",\"name\":\"乡宁县\"},{\"code\":\"142632\",\"name\":\"蒲县\"},{\"code\":\"142633\",\"name\":\"大宁县\"},{\"code\":\"142634\",\"name\":\"永和县\"},{\"code\":\"142635\",\"name\":\"隰县\"},{\"code\":\"142636\",\"name\":\"汾西县\"},{\"code\":\"142700\",\"name\":\"运城地区\"},{\"code\":\"142701\",\"name\":\"运城市\"},{\"code\":\"142702\",\"name\":\"永济市\"},{\"code\":\"142703\",\"name\":\"河津市\"},{\"code\":\"142723\",\"name\":\"芮城县\"},{\"code\":\"142724\",\"name\":\"临猗县\"},{\"code\":\"142725\",\"name\":\"万荣县\"},{\"code\":\"142726\",\"name\":\"新绛县\"},{\"code\":\"142727\",\"name\":\"稷山县\"},{\"code\":\"142729\",\"name\":\"闻喜县\"},{\"code\":\"142730\",\"name\":\"夏县\"},{\"code\":\"142731\",\"name\":\"绛县\"},{\"code\":\"142732\",\"name\":\"平陆县\"},{\"code\":\"142733\",\"name\":\"垣曲县\"},{\"code\":\"150000\",\"name\":\"内蒙古自治区\"},{\"code\":\"150100\",\"name\":\"呼和浩特市\"},{\"code\":\"150101\",\"name\":\"市辖区\"},{\"code\":\"150102\",\"name\":\"新城区\"},{\"code\":\"150103\",\"name\":\"回民区\"},{\"code\":\"150104\",\"name\":\"玉泉区\"},{\"code\":\"150105\",\"name\":\"郊区\"},{\"code\":\"150121\",\"name\":\"土默特左旗\"},{\"code\":\"150122\",\"name\":\"托克托县\"},{\"code\":\"150123\",\"name\":\"和林格尔县\"},{\"code\":\"150124\",\"name\":\"清水河县\"},{\"code\":\"150125\",\"name\":\"武川县\"},{\"code\":\"150200\",\"name\":\"包头市\"},{\"code\":\"150201\",\"name\":\"市辖区\"},{\"code\":\"150202\",\"name\":\"东河区\"},{\"code\":\"150203\",\"name\":\"昆都伦区\"},{\"code\":\"150204\",\"name\":\"青山区\"},{\"code\":\"150205\",\"name\":\"石拐矿区\"},{\"code\":\"150206\",\"name\":\"白云矿区\"},{\"code\":\"150207\",\"name\":\"郊区\"},{\"code\":\"150221\",\"name\":\"土默特右旗\"},{\"code\":\"150222\",\"name\":\"固阳县\"},{\"code\":\"150223\",\"name\":\"达尔罕茂明安联合旗\"},{\"code\":\"150300\",\"name\":\"乌海市\"},{\"code\":\"150301\",\"name\":\"市辖区\"},{\"code\":\"150302\",\"name\":\"海勃湾区\"},{\"code\":\"150303\",\"name\":\"海南区\"},{\"code\":\"150304\",\"name\":\"乌达区\"},{\"code\":\"150400\",\"name\":\"赤峰市\"},{\"code\":\"150401\",\"name\":\"市辖区\"},{\"code\":\"150402\",\"name\":\"红山区\"},{\"code\":\"150403\",\"name\":\"元宝山区\"},{\"code\":\"150404\",\"name\":\"松山区\"},{\"code\":\"150421\",\"name\":\"阿鲁科尔沁旗\"},{\"code\":\"150422\",\"name\":\"巴林左旗\"},{\"code\":\"150423\",\"name\":\"巴林右旗\"},{\"code\":\"150424\",\"name\":\"林西县\"},{\"code\":\"150425\",\"name\":\"克什克腾旗\"},{\"code\":\"150426\",\"name\":\"翁牛特旗\"},{\"code\":\"150428\",\"name\":\"喀喇沁旗\"},{\"code\":\"150429\",\"name\":\"宁城县\"},{\"code\":\"150430\",\"name\":\"敖汉旗\"},{\"code\":\"152100\",\"name\":\"呼伦贝尔盟\"},{\"code\":\"152101\",\"name\":\"海拉尔市\"},{\"code\":\"152102\",\"name\":\"满洲里市\"},{\"code\":\"152103\",\"name\":\"扎兰屯市\"},{\"code\":\"152104\",\"name\":\"牙克石市\"},{\"code\":\"152105\",\"name\":\"根河市\"},{\"code\":\"152106\",\"name\":\"额尔古纳市\"},{\"code\":\"152122\",\"name\":\"阿荣旗\"},{\"code\":\"152123\",\"name\":\"莫力达瓦达斡尔族自治旗\"},{\"code\":\"152127\",\"name\":\"鄂伦春自治旗\"},{\"code\":\"152128\",\"name\":\"鄂温克族自治旗\"},{\"code\":\"152129\",\"name\":\"新巴尔虎右旗\"},{\"code\":\"152130\",\"name\":\"新巴尔虎左旗\"},{\"code\":\"152131\",\"name\":\"陈巴尔虎旗\"},{\"code\":\"152200\",\"name\":\"兴安盟\"},{\"code\":\"152201\",\"name\":\"乌兰浩特市\"},{\"code\":\"152202\",\"name\":\"阿尔山市\"},{\"code\":\"152221\",\"name\":\"科尔沁右翼前旗\"},{\"code\":\"152222\",\"name\":\"科尔沁右翼中旗\"},{\"code\":\"152223\",\"name\":\"扎赉特旗\"},{\"code\":\"152224\",\"name\":\"突泉县\"},{\"code\":\"152300\",\"name\":\"哲里木盟\"},{\"code\":\"152301\",\"name\":\"通辽市\"},{\"code\":\"152302\",\"name\":\"霍林郭勒市\"},{\"code\":\"152322\",\"name\":\"科尔沁左翼中旗\"},{\"code\":\"152323\",\"name\":\"科尔沁左翼后旗\"},{\"code\":\"152324\",\"name\":\"开鲁县\"},{\"code\":\"152325\",\"name\":\"库伦旗\"},{\"code\":\"152326\",\"name\":\"奈曼旗\"},{\"code\":\"152327\",\"name\":\"扎鲁特旗\"},{\"code\":\"152500\",\"name\":\"锡林郭勒盟\"},{\"code\":\"152501\",\"name\":\"二连浩特市\"},{\"code\":\"152502\",\"name\":\"锡林浩特市\"},{\"code\":\"152522\",\"name\":\"阿巴嘎旗\"},{\"code\":\"152523\",\"name\":\"苏尼特左旗\"},{\"code\":\"152524\",\"name\":\"苏尼特右旗\"},{\"code\":\"152525\",\"name\":\"东乌珠穆沁旗\"},{\"code\":\"152526\",\"name\":\"西乌珠穆沁旗\"},{\"code\":\"152527\",\"name\":\"太仆寺旗\"},{\"code\":\"152528\",\"name\":\"镶黄旗\"},{\"code\":\"152529\",\"name\":\"正镶白旗\"},{\"code\":\"152530\",\"name\":\"正蓝旗\"},{\"code\":\"152531\",\"name\":\"多伦县\"},{\"code\":\"152600\",\"name\":\"乌兰察布盟\"},{\"code\":\"152601\",\"name\":\"集宁市\"},{\"code\":\"152602\",\"name\":\"丰镇市\"},{\"code\":\"152624\",\"name\":\"卓资县\"},{\"code\":\"152625\",\"name\":\"化德县\"},{\"code\":\"152626\",\"name\":\"商都县\"},{\"code\":\"152627\",\"name\":\"兴和县\"},{\"code\":\"152629\",\"name\":\"凉城县\"},{\"code\":\"152630\",\"name\":\"察哈尔右翼前旗\"},{\"code\":\"152631\",\"name\":\"察哈尔右翼中旗\"},{\"code\":\"152632\",\"name\":\"察哈尔右翼后旗\"},{\"code\":\"152634\",\"name\":\"四子王旗\"},{\"code\":\"152700\",\"name\":\"伊克昭盟\"},");
        area.append("{\"code\":\"152701\",\"name\":\"东胜市\"},{\"code\":\"152722\",\"name\":\"达拉特旗\"},{\"code\":\"152723\",\"name\":\"准格尔旗\"},{\"code\":\"152724\",\"name\":\"鄂托克前旗\"},{\"code\":\"152725\",\"name\":\"鄂托克旗\"},{\"code\":\"152726\",\"name\":\"杭锦旗\"},{\"code\":\"152727\",\"name\":\"乌审旗\"},{\"code\":\"152728\",\"name\":\"伊金霍洛旗\"},{\"code\":\"152800\",\"name\":\"巴彦淖尔盟\"},{\"code\":\"152801\",\"name\":\"临河市\"},{\"code\":\"152822\",\"name\":\"五原县\"},{\"code\":\"152823\",\"name\":\"磴口县\"},{\"code\":\"152824\",\"name\":\"乌拉特前旗\"},{\"code\":\"152825\",\"name\":\"乌拉特中旗\"},{\"code\":\"152826\",\"name\":\"乌拉特后旗\"},{\"code\":\"152827\",\"name\":\"杭锦后旗\"},{\"code\":\"152900\",\"name\":\"阿拉善盟\"},{\"code\":\"152921\",\"name\":\"阿拉善左旗\"},{\"code\":\"152922\",\"name\":\"阿拉善右旗\"},{\"code\":\"152923\",\"name\":\"额济纳旗\"},{\"code\":\"210000\",\"name\":\"辽宁省\"},{\"code\":\"210100\",\"name\":\"沈阳市\"},{\"code\":\"210101\",\"name\":\"市辖区\"},{\"code\":\"210102\",\"name\":\"和平区\"},{\"code\":\"210103\",\"name\":\"沈河区\"},{\"code\":\"210104\",\"name\":\"大东区\"},{\"code\":\"210105\",\"name\":\"皇姑区\"},{\"code\":\"210106\",\"name\":\"铁西区\"},{\"code\":\"210111\",\"name\":\"苏家屯区\"},{\"code\":\"210112\",\"name\":\"东陵区\"},{\"code\":\"210113\",\"name\":\"新城子区\"},{\"code\":\"210114\",\"name\":\"于洪区\"},{\"code\":\"210122\",\"name\":\"辽中县\"},{\"code\":\"210123\",\"name\":\"康平县\"},{\"code\":\"210124\",\"name\":\"法库县\"},{\"code\":\"210181\",\"name\":\"新民市\"},{\"code\":\"210200\",\"name\":\"大连市\"},{\"code\":\"210201\",\"name\":\"市辖区\"},{\"code\":\"210202\",\"name\":\"中山区\"},{\"code\":\"210203\",\"name\":\"西岗区\"},{\"code\":\"210204\",\"name\":\"沙河口区\"},{\"code\":\"210211\",\"name\":\"甘井子区\"},{\"code\":\"210212\",\"name\":\"旅顺口区\"},{\"code\":\"210213\",\"name\":\"金州区\"},{\"code\":\"210224\",\"name\":\"长海县\"},{\"code\":\"210281\",\"name\":\"瓦房店市\"},{\"code\":\"210282\",\"name\":\"普兰店市\"},{\"code\":\"210283\",\"name\":\"庄河市\"},{\"code\":\"210300\",\"name\":\"鞍山市\"},{\"code\":\"210301\",\"name\":\"市辖区\"},{\"code\":\"210302\",\"name\":\"铁东区\"},{\"code\":\"210303\",\"name\":\"铁西区\"},{\"code\":\"210304\",\"name\":\"立山区\"},{\"code\":\"210311\",\"name\":\"千山区\"},{\"code\":\"210321\",\"name\":\"台安县\"},{\"code\":\"210323\",\"name\":\"岫岩满族自治县\"},{\"code\":\"210381\",\"name\":\"海城市\"},{\"code\":\"210400\",\"name\":\"抚顺市\"},{\"code\":\"210401\",\"name\":\"市辖区\"},{\"code\":\"210402\",\"name\":\"新抚区\"},{\"code\":\"210403\",\"name\":\"露天区\"},{\"code\":\"210404\",\"name\":\"望花区\"},{\"code\":\"210411\",\"name\":\"顺城区\"},{\"code\":\"210421\",\"name\":\"抚顺县\"},{\"code\":\"210422\",\"name\":\"新宾满族自治县\"},{\"code\":\"210423\",\"name\":\"清原满族自治县\"},{\"code\":\"210500\",\"name\":\"本溪市\"},{\"code\":\"210501\",\"name\":\"市辖区\"},{\"code\":\"210502\",\"name\":\"平山区\"},{\"code\":\"210503\",\"name\":\"溪湖区\"},{\"code\":\"210504\",\"name\":\"明山区\"},{\"code\":\"210505\",\"name\":\"南芬区\"},{\"code\":\"210521\",\"name\":\"本溪满族自治县\"},{\"code\":\"210522\",\"name\":\"桓仁满族自治县\"},{\"code\":\"210600\",\"name\":\"丹东市\"},{\"code\":\"210601\",\"name\":\"市辖区\"},{\"code\":\"210602\",\"name\":\"元宝区\"},{\"code\":\"210603\",\"name\":\"振兴区\"},{\"code\":\"210604\",\"name\":\"振安区\"},{\"code\":\"210624\",\"name\":\"宽甸满族自治县\"},{\"code\":\"210681\",\"name\":\"东港市\"},{\"code\":\"210682\",\"name\":\"凤城市\"},{\"code\":\"210700\",\"name\":\"锦州市\"},{\"code\":\"210701\",\"name\":\"市辖区\"},{\"code\":\"210702\",\"name\":\"古塔区\"},{\"code\":\"210703\",\"name\":\"凌河区\"},{\"code\":\"210711\",\"name\":\"太和区\"},{\"code\":\"210726\",\"name\":\"黑山县\"},{\"code\":\"210727\",\"name\":\"义县\"},{\"code\":\"210781\",\"name\":\"凌海市\"},{\"code\":\"210782\",\"name\":\"北宁市\"},{\"code\":\"210800\",\"name\":\"营口市\"},{\"code\":\"210801\",\"name\":\"市辖区\"},{\"code\":\"210802\",\"name\":\"站前区\"},{\"code\":\"210803\",\"name\":\"西市区\"},{\"code\":\"210804\",\"name\":\"鲅鱼圈区\"},{\"code\":\"210811\",\"name\":\"老边区\"},{\"code\":\"210881\",\"name\":\"盖州市\"},{\"code\":\"210882\",\"name\":\"大石桥市\"},{\"code\":\"210900\",\"name\":\"阜新市\"},{\"code\":\"210901\",\"name\":\"市辖区\"},{\"code\":\"210902\",\"name\":\"海州区\"},{\"code\":\"210903\",\"name\":\"新邱区\"},{\"code\":\"210904\",\"name\":\"太平区\"},{\"code\":\"210905\",\"name\":\"清河门区\"},{\"code\":\"210911\",\"name\":\"细河区\"},{\"code\":\"210921\",\"name\":\"阜新蒙古族自治县\"},{\"code\":\"210922\",\"name\":\"彰武县\"},{\"code\":\"211000\",\"name\":\"辽阳市\"},{\"code\":\"211001\",\"name\":\"市辖区\"},{\"code\":\"211002\",\"name\":\"白塔区\"},{\"code\":\"211003\",\"name\":\"文圣区\"},{\"code\":\"211004\",\"name\":\"宏伟区\"},{\"code\":\"211005\",\"name\":\"弓长岭区\"},{\"code\":\"211011\",\"name\":\"太子河区\"},{\"code\":\"211021\",\"name\":\"辽阳县\"},{\"code\":\"211081\",\"name\":\"灯塔市\"},{\"code\":\"211100\",\"name\":\"盘锦市\"},{\"code\":\"211101\",\"name\":\"市辖区\"},{\"code\":\"211102\",\"name\":\"双台子区\"},{\"code\":\"211103\",\"name\":\"兴隆台区\"},{\"code\":\"211121\",\"name\":\"大洼县\"},{\"code\":\"211122\",\"name\":\"盘山县\"},{\"code\":\"211200\",\"name\":\"铁岭市\"},{\"code\":\"211201\",\"name\":\"市辖区\"},{\"code\":\"211202\",\"name\":\"银州区\"},{\"code\":\"211204\",\"name\":\"清河区\"},{\"code\":\"211221\",\"name\":\"铁岭县\"},{\"code\":\"211223\",\"name\":\"西丰县\"},{\"code\":\"211224\",\"name\":\"昌图县\"},{\"code\":\"211281\",\"name\":\"铁法市\"},{\"code\":\"211282\",\"name\":\"开原市\"},{\"code\":\"211300\",\"name\":\"朝阳市\"},{\"code\":\"211301\",\"name\":\"市辖区\"},{\"code\":\"211302\",\"name\":\"双塔区\"},{\"code\":\"211303\",\"name\":\"龙城区\"},{\"code\":\"211321\",\"name\":\"朝阳县\"},{\"code\":\"211322\",\"name\":\"建平县\"},{\"code\":\"211324\",\"name\":\"喀喇沁左翼蒙古族自治县\"},{\"code\":\"211381\",\"name\":\"北票市\"},{\"code\":\"211382\",\"name\":\"凌源市\"},{\"code\":\"211400\",\"name\":\"葫芦岛市\"},{\"code\":\"211401\",\"name\":\"市辖区\"},{\"code\":\"211402\",\"name\":\"连山区\"},{\"code\":\"211403\",\"name\":\"龙港区\"},{\"code\":\"211404\",\"name\":\"南票区\"},{\"code\":\"211421\",\"name\":\"绥中县\"},{\"code\":\"211422\",\"name\":\"建昌县\"},{\"code\":\"211481\",\"name\":\"兴城市\"},{\"code\":\"220000\",\"name\":\"吉林省\"},{\"code\":\"220100\",\"name\":\"长春市\"},{\"code\":\"220101\",\"name\":\"市辖区\"},{\"code\":\"220102\",\"name\":\"南关区\"},{\"code\":\"220103\",\"name\":\"宽城区\"},{\"code\":\"220104\",\"name\":\"朝阳区\"},{\"code\":\"220105\",\"name\":\"二道区\"},{\"code\":\"220106\",\"name\":\"绿园区\"},{\"code\":\"220112\",\"name\":\"双阳区\"},{\"code\":\"220122\",\"name\":\"农安县\"},{\"code\":\"220181\",\"name\":\"九台市\"},{\"code\":\"220182\",\"name\":\"榆树市\"},{\"code\":\"220183\",\"name\":\"德惠市\"},{\"code\":\"220200\",\"name\":\"吉林市\"},{\"code\":\"220201\",\"name\":\"市辖区\"},{\"code\":\"220202\",\"name\":\"昌邑区\"},{\"code\":\"220203\",\"name\":\"龙潭区\"},{\"code\":\"220204\",\"name\":\"船营区\"},{\"code\":\"220211\",\"name\":\"丰满区\"},{\"code\":\"220221\",\"name\":\"永吉县\"},{\"code\":\"220281\",\"name\":\"蛟河市\"},{\"code\":\"220282\",\"name\":\"桦甸市\"},{\"code\":\"220283\",\"name\":\"舒兰市\"},{\"code\":\"220284\",\"name\":\"磐石市\"},{\"code\":\"220300\",\"name\":\"四平市\"},{\"code\":\"220301\",\"name\":\"市辖区\"},{\"code\":\"220302\",\"name\":\"铁西区\"},{\"code\":\"220303\",\"name\":\"铁东区\"},{\"code\":\"220322\",\"name\":\"梨树县\"},{\"code\":\"220323\",\"name\":\"伊通满族自治县\"},{\"code\":\"220381\",\"name\":\"公主岭市\"},{\"code\":\"220382\",\"name\":\"双辽市\"},{\"code\":\"220400\",\"name\":\"辽源市\"},{\"code\":\"220401\",\"name\":\"市辖区\"},{\"code\":\"220402\",\"name\":\"龙山区\"},{\"code\":\"220403\",\"name\":\"西安区\"},{\"code\":\"220421\",\"name\":\"东丰县\"},{\"code\":\"220422\",\"name\":\"东辽县\"},{\"code\":\"220500\",\"name\":\"通化市\"},{\"code\":\"220501\",\"name\":\"市辖区\"},{\"code\":\"220502\",\"name\":\"东昌区\"},{\"code\":\"220503\",\"name\":\"二道江区\"},{\"code\":\"220521\",\"name\":\"通化县\"},{\"code\":\"220523\",\"name\":\"辉南县\"},{\"code\":\"220524\",\"name\":\"柳河县\"},{\"code\":\"220581\",\"name\":\"梅河口市\"},{\"code\":\"220582\",\"name\":\"集安市\"},{\"code\":\"220600\",\"name\":\"白山市\"},{\"code\":\"220601\",\"name\":\"市辖区\"},{\"code\":\"220602\",\"name\":\"八道江区\"},{\"code\":\"220621\",\"name\":\"抚松县\"},{\"code\":\"220622\",\"name\":\"靖宇县\"},{\"code\":\"220623\",\"name\":\"长白朝鲜族自治县\"},{\"code\":\"220625\",\"name\":\"江源县\"},{\"code\":\"220681\",\"name\":\"临江市\"},{\"code\":\"220700\",\"name\":\"松原市\"},{\"code\":\"220701\",\"name\":\"市辖区\"},{\"code\":\"220702\",\"name\":\"宁江区\"},{\"code\":\"220721\",\"name\":\"前郭尔罗斯蒙古族自治县\"},{\"code\":\"220722\",\"name\":\"长岭县\"},{\"code\":\"220723\",\"name\":\"乾安县\"},{\"code\":\"220724\",\"name\":\"扶余县\"},{\"code\":\"220800\",\"name\":\"白城市\"},{\"code\":\"220801\",\"name\":\"市辖区\"},{\"code\":\"220802\",\"name\":\"洮北区\"},{\"code\":\"220821\",\"name\":\"镇赉县\"},{\"code\":\"220822\",\"name\":\"通榆县\"},{\"code\":\"220881\",\"name\":\"洮南市\"},{\"code\":\"220882\",\"name\":\"大安市\"},{\"code\":\"222400\",\"name\":\"延边朝鲜族自治州\"},{\"code\":\"222401\",\"name\":\"延吉市\"},{\"code\":\"222402\",\"name\":\"图们市\"},{\"code\":\"222403\",\"name\":\"敦化市\"},{\"code\":\"222404\",\"name\":\"珲春市\"},{\"code\":\"222405\",\"name\":\"龙井市\"},{\"code\":\"222406\",\"name\":\"和龙市\"},{\"code\":\"222424\",\"name\":\"汪清县\"},{\"code\":\"222426\",\"name\":\"安图县\"},{\"code\":\"230000\",\"name\":\"黑龙江省\"},{\"code\":\"230100\",\"name\":\"哈尔滨市\"},{\"code\":\"230101\",\"name\":\"市辖区\"},{\"code\":\"230102\",\"name\":\"道里区\"},{\"code\":\"230103\",\"name\":\"南岗区\"},{\"code\":\"230104\",\"name\":\"道外区\"},{\"code\":\"230105\",\"name\":\"太平区\"},{\"code\":\"230106\",\"name\":\"香坊区\"},{\"code\":\"230107\",\"name\":\"动力区\"},{\"code\":\"230108\",\"name\":\"平房区\"},{\"code\":\"230121\",\"name\":\"呼兰县\"},{\"code\":\"230123\",\"name\":\"依兰县\"},");
        area.append("{\"code\":\"230124\",\"name\":\"方正县\"},{\"code\":\"230125\",\"name\":\"宾县\"},{\"code\":\"230126\",\"name\":\"巴彦县\"},{\"code\":\"230127\",\"name\":\"木兰县\"},{\"code\":\"230128\",\"name\":\"通河县\"},{\"code\":\"230129\",\"name\":\"延寿县\"},{\"code\":\"230181\",\"name\":\"阿城市\"},{\"code\":\"230182\",\"name\":\"双城市\"},{\"code\":\"230183\",\"name\":\"尚志市\"},{\"code\":\"230184\",\"name\":\"五常市\"},{\"code\":\"230200\",\"name\":\"齐齐哈尔市\"},{\"code\":\"230201\",\"name\":\"市辖区\"},{\"code\":\"230202\",\"name\":\"龙沙区\"},{\"code\":\"230203\",\"name\":\"建华区\"},{\"code\":\"230204\",\"name\":\"铁锋区\"},{\"code\":\"230205\",\"name\":\"昂昂溪区\"},{\"code\":\"230206\",\"name\":\"富拉尔基区\"},{\"code\":\"230207\",\"name\":\"碾子山区\"},{\"code\":\"230208\",\"name\":\"梅里斯达斡尔族区\"},{\"code\":\"230221\",\"name\":\"龙江县\"},{\"code\":\"230223\",\"name\":\"依安县\"},{\"code\":\"230224\",\"name\":\"泰来县\"},{\"code\":\"230225\",\"name\":\"甘南县\"},{\"code\":\"230227\",\"name\":\"富裕县\"},{\"code\":\"230229\",\"name\":\"克山县\"},{\"code\":\"230230\",\"name\":\"克东县\"},{\"code\":\"230231\",\"name\":\"拜泉县\"},{\"code\":\"230281\",\"name\":\"讷河市\"},{\"code\":\"230300\",\"name\":\"鸡西市\"},{\"code\":\"230301\",\"name\":\"市辖区\"},{\"code\":\"230302\",\"name\":\"鸡冠区\"},{\"code\":\"230303\",\"name\":\"恒山区\"},{\"code\":\"230304\",\"name\":\"滴道区\"},{\"code\":\"230305\",\"name\":\"梨树区\"},{\"code\":\"230306\",\"name\":\"城子河区\"},{\"code\":\"230307\",\"name\":\"麻山区\"},{\"code\":\"230321\",\"name\":\"鸡东县\"},{\"code\":\"230381\",\"name\":\"虎林市\"},{\"code\":\"230382\",\"name\":\"密山市\"},{\"code\":\"230400\",\"name\":\"鹤岗市\"},{\"code\":\"230401\",\"name\":\"市辖区\"},{\"code\":\"230402\",\"name\":\"向阳区\"},{\"code\":\"230403\",\"name\":\"工农区\"},{\"code\":\"230404\",\"name\":\"南山区\"},{\"code\":\"230405\",\"name\":\"兴安区\"},{\"code\":\"230406\",\"name\":\"东山区\"},{\"code\":\"230407\",\"name\":\"兴山区\"},{\"code\":\"230421\",\"name\":\"萝北县\"},{\"code\":\"230422\",\"name\":\"绥滨县\"},{\"code\":\"230500\",\"name\":\"双鸭山市\"},{\"code\":\"230501\",\"name\":\"市辖区\"},{\"code\":\"230502\",\"name\":\"尖山区\"},{\"code\":\"230503\",\"name\":\"岭东区\"},{\"code\":\"230505\",\"name\":\"四方台区\"},{\"code\":\"230506\",\"name\":\"宝山区\"},{\"code\":\"230521\",\"name\":\"集贤县\"},{\"code\":\"230522\",\"name\":\"友谊县\"},{\"code\":\"230523\",\"name\":\"宝清县\"},{\"code\":\"230524\",\"name\":\"饶河县\"},{\"code\":\"230600\",\"name\":\"大庆市\"},{\"code\":\"230601\",\"name\":\"市辖区\"},{\"code\":\"230602\",\"name\":\"萨尔图区\"},{\"code\":\"230603\",\"name\":\"龙凤区\"},{\"code\":\"230604\",\"name\":\"让胡路区\"},{\"code\":\"230605\",\"name\":\"红岗区\"},{\"code\":\"230606\",\"name\":\"大同区\"},{\"code\":\"230621\",\"name\":\"肇州县\"},{\"code\":\"230622\",\"name\":\"肇源县\"},{\"code\":\"230623\",\"name\":\"林甸县\"},{\"code\":\"230624\",\"name\":\"杜尔伯特蒙古族自治县\"},{\"code\":\"230700\",\"name\":\"伊春市\"},{\"code\":\"230701\",\"name\":\"市辖区\"},{\"code\":\"230702\",\"name\":\"伊春区\"},{\"code\":\"230703\",\"name\":\"南岔区\"},{\"code\":\"230704\",\"name\":\"友好区\"},{\"code\":\"230705\",\"name\":\"西林区\"},{\"code\":\"230706\",\"name\":\"翠峦区\"},{\"code\":\"230707\",\"name\":\"新青区\"},{\"code\":\"230708\",\"name\":\"美溪区\"},{\"code\":\"230709\",\"name\":\"金山屯区\"},{\"code\":\"230710\",\"name\":\"五营区\"},{\"code\":\"230711\",\"name\":\"乌马河区\"},{\"code\":\"230712\",\"name\":\"汤旺河区\"},{\"code\":\"230713\",\"name\":\"带岭区\"},{\"code\":\"230714\",\"name\":\"乌伊岭区\"},{\"code\":\"230715\",\"name\":\"红星区\"},{\"code\":\"230716\",\"name\":\"上甘岭区\"},{\"code\":\"230722\",\"name\":\"嘉荫县\"},{\"code\":\"230781\",\"name\":\"铁力市\"},{\"code\":\"230800\",\"name\":\"佳木斯市\"},{\"code\":\"230801\",\"name\":\"市辖区\"},{\"code\":\"230802\",\"name\":\"永红区\"},{\"code\":\"230803\",\"name\":\"向阳区\"},{\"code\":\"230804\",\"name\":\"前进区\"},{\"code\":\"230805\",\"name\":\"东风区\"},{\"code\":\"230811\",\"name\":\"郊区\"},{\"code\":\"230822\",\"name\":\"桦南县\"},{\"code\":\"230826\",\"name\":\"桦川县\"},{\"code\":\"230828\",\"name\":\"汤原县\"},{\"code\":\"230833\",\"name\":\"抚远县\"},{\"code\":\"230881\",\"name\":\"同江市\"},{\"code\":\"230882\",\"name\":\"富锦市\"},{\"code\":\"230900\",\"name\":\"七台河市\"},{\"code\":\"230901\",\"name\":\"市辖区\"},{\"code\":\"230902\",\"name\":\"新兴区\"},{\"code\":\"230903\",\"name\":\"桃山区\"},{\"code\":\"230904\",\"name\":\"茄子河区\"},{\"code\":\"230921\",\"name\":\"勃利县\"},{\"code\":\"231000\",\"name\":\"牡丹江市\"},{\"code\":\"231001\",\"name\":\"市辖区\"},{\"code\":\"231002\",\"name\":\"东安区\"},{\"code\":\"231003\",\"name\":\"阳明区\"},{\"code\":\"231004\",\"name\":\"爱民区\"},{\"code\":\"231005\",\"name\":\"西安区\"},{\"code\":\"231024\",\"name\":\"东宁县\"},{\"code\":\"231025\",\"name\":\"林口县\"},{\"code\":\"231081\",\"name\":\"绥芬河市\"},{\"code\":\"231083\",\"name\":\"海林市\"},{\"code\":\"231084\",\"name\":\"宁安市\"},{\"code\":\"231085\",\"name\":\"穆棱市\"},{\"code\":\"231100\",\"name\":\"黑河市\"},{\"code\":\"231101\",\"name\":\"市辖区\"},{\"code\":\"231102\",\"name\":\"爱辉区\"},{\"code\":\"231121\",\"name\":\"嫩江县\"},{\"code\":\"231123\",\"name\":\"逊克县\"},{\"code\":\"231124\",\"name\":\"孙吴县\"},{\"code\":\"231181\",\"name\":\"北安市\"},{\"code\":\"231182\",\"name\":\"五大连池市\"},{\"code\":\"232300\",\"name\":\"绥化地区\"},{\"code\":\"232301\",\"name\":\"绥化市\"},{\"code\":\"232302\",\"name\":\"安达市\"},{\"code\":\"232303\",\"name\":\"肇东市\"},{\"code\":\"232304\",\"name\":\"海伦市\"},{\"code\":\"232324\",\"name\":\"望奎县\"},{\"code\":\"232325\",\"name\":\"兰西县\"},{\"code\":\"232326\",\"name\":\"青冈县\"},{\"code\":\"232330\",\"name\":\"庆安县\"},{\"code\":\"232331\",\"name\":\"明水县\"},{\"code\":\"232332\",\"name\":\"绥棱县\"},{\"code\":\"232700\",\"name\":\"大兴安岭地区\"},{\"code\":\"232721\",\"name\":\"呼玛县\"},{\"code\":\"232722\",\"name\":\"塔河县\"},{\"code\":\"232723\",\"name\":\"漠河县\"},{\"code\":\"310000\",\"name\":\"上海市\"},{\"code\":\"310100\",\"name\":\"市辖区\"},{\"code\":\"310101\",\"name\":\"黄浦区\"},{\"code\":\"310102\",\"name\":\"南市区\"},{\"code\":\"310103\",\"name\":\"卢湾区\"},{\"code\":\"310104\",\"name\":\"徐汇区\"},{\"code\":\"310105\",\"name\":\"长宁区\"},{\"code\":\"310106\",\"name\":\"静安区\"},{\"code\":\"310107\",\"name\":\"普陀区\"},{\"code\":\"310108\",\"name\":\"闸北区\"},{\"code\":\"310109\",\"name\":\"虹口区\"},{\"code\":\"310110\",\"name\":\"杨浦区\"},{\"code\":\"310112\",\"name\":\"闵行区\"},{\"code\":\"310113\",\"name\":\"宝山区\"},{\"code\":\"310114\",\"name\":\"嘉定区\"},{\"code\":\"310115\",\"name\":\"浦东新区\"},{\"code\":\"310116\",\"name\":\"金山区\"},{\"code\":\"310117\",\"name\":\"松江区\"},{\"code\":\"310200\",\"name\":\"县\"},{\"code\":\"310225\",\"name\":\"南汇县\"},{\"code\":\"310226\",\"name\":\"奉贤县\"},{\"code\":\"310229\",\"name\":\"青浦县\"},{\"code\":\"310230\",\"name\":\"崇明县\"},{\"code\":\"320000\",\"name\":\"江苏省\"},{\"code\":\"320100\",\"name\":\"南京市\"},{\"code\":\"320101\",\"name\":\"市辖区\"},{\"code\":\"320102\",\"name\":\"玄武区\"},{\"code\":\"320103\",\"name\":\"白下区\"},{\"code\":\"320104\",\"name\":\"秦淮区\"},{\"code\":\"320105\",\"name\":\"建邺区\"},{\"code\":\"320106\",\"name\":\"鼓楼区\"},{\"code\":\"320107\",\"name\":\"下关区\"},{\"code\":\"320111\",\"name\":\"浦口区\"},{\"code\":\"320112\",\"name\":\"大厂区\"},{\"code\":\"320113\",\"name\":\"栖霞区\"},{\"code\":\"320114\",\"name\":\"雨花台区\"},{\"code\":\"320121\",\"name\":\"江宁县\"},{\"code\":\"320122\",\"name\":\"江浦县\"},{\"code\":\"320123\",\"name\":\"六合县\"},{\"code\":\"320124\",\"name\":\"溧水县\"},{\"code\":\"320125\",\"name\":\"高淳县\"},{\"code\":\"320200\",\"name\":\"无锡市\"},{\"code\":\"320201\",\"name\":\"市辖区\"},{\"code\":\"320202\",\"name\":\"崇安区\"},{\"code\":\"320203\",\"name\":\"南长区\"},{\"code\":\"320204\",\"name\":\"北塘区\"},{\"code\":\"320211\",\"name\":\"郊区\"},{\"code\":\"320212\",\"name\":\"马山区\"},{\"code\":\"320281\",\"name\":\"江阴市\"},{\"code\":\"320282\",\"name\":\"宜兴市\"},{\"code\":\"320283\",\"name\":\"锡山市\"},{\"code\":\"320300\",\"name\":\"徐州市\"},{\"code\":\"320301\",\"name\":\"市辖区\"},{\"code\":\"320302\",\"name\":\"鼓楼区\"},{\"code\":\"320303\",\"name\":\"云龙区\"},{\"code\":\"320304\",\"name\":\"九里区\"},{\"code\":\"320305\",\"name\":\"贾汪区\"},{\"code\":\"320311\",\"name\":\"泉山区\"},{\"code\":\"320321\",\"name\":\"丰县\"},{\"code\":\"320322\",\"name\":\"沛县\"},{\"code\":\"320323\",\"name\":\"铜山县\"},{\"code\":\"320324\",\"name\":\"睢宁县\"},{\"code\":\"320381\",\"name\":\"新沂市\"},{\"code\":\"320382\",\"name\":\"邳州市\"},{\"code\":\"320400\",\"name\":\"常州市\"},{\"code\":\"320401\",\"name\":\"市辖区\"},{\"code\":\"320402\",\"name\":\"天宁区\"},{\"code\":\"320404\",\"name\":\"钟楼区\"},{\"code\":\"320405\",\"name\":\"戚墅堰区\"},{\"code\":\"320411\",\"name\":\"郊区\"},{\"code\":\"320481\",\"name\":\"溧阳市\"},{\"code\":\"320482\",\"name\":\"金坛市\"},{\"code\":\"320483\",\"name\":\"武进市\"},{\"code\":\"320500\",\"name\":\"苏州市\"},{\"code\":\"320501\",\"name\":\"市辖区\"},{\"code\":\"320502\",\"name\":\"沧浪区\"},{\"code\":\"320503\",\"name\":\"平江区\"},{\"code\":\"320504\",\"name\":\"金阊区\"},{\"code\":\"320511\",\"name\":\"郊区\"},{\"code\":\"320581\",\"name\":\"常熟市\"},{\"code\":\"320582\",\"name\":\"张家港市\"},{\"code\":\"320583\",\"name\":\"昆山市\"},{\"code\":\"320584\",\"name\":\"吴江市\"},{\"code\":\"320585\",\"name\":\"太仓市\"},{\"code\":\"320586\",\"name\":\"吴县市\"},{\"code\":\"320600\",\"name\":\"南通市\"},{\"code\":\"320601\",\"name\":\"市辖区\"},{\"code\":\"320602\",\"name\":\"崇川区\"},{\"code\":\"320611\",\"name\":\"港闸区\"},{\"code\":\"320621\",\"name\":\"海安县\"},{\"code\":\"320623\",\"name\":\"如东县\"},{\"code\":\"320681\",\"name\":\"启东市\"},{\"code\":\"320682\",\"name\":\"如皋市\"},{\"code\":\"320683\",\"name\":\"通州市\"},{\"code\":\"320684\",\"name\":\"海门市\"},{\"code\":\"320700\",\"name\":\"连云港市\"},{\"code\":\"320701\",\"name\":\"市辖区\"},{\"code\":\"320703\",\"name\":\"连云区\"},{\"code\":\"320704\",\"name\":\"云台区\"},{\"code\":\"320705\",\"name\":\"新浦区\"},{\"code\":\"320706\",\"name\":\"海州区\"},{\"code\":\"320721\",\"name\":\"赣榆县\"},{\"code\":\"320722\",\"name\":\"东海县\"},{\"code\":\"320723\",\"name\":\"灌云县\"},{\"code\":\"320724\",\"name\":\"灌南县\"},{\"code\":\"320800\",\"name\":\"淮阴市\"},{\"code\":\"320801\",\"name\":\"市辖区\"},{\"code\":\"320802\",\"name\":\"清河区\"},{\"code\":\"320811\",\"name\":\"清浦区\"},{\"code\":\"320821\",\"name\":\"淮阴县\"},{\"code\":\"320826\",\"name\":\"涟水县\"},{\"code\":\"320829\",\"name\":\"洪泽县\"},{\"code\":\"320830\",\"name\":\"盱眙县\"},{\"code\":\"320831\",\"name\":\"金湖县\"},{\"code\":\"320882\",\"name\":\"淮安市\"},{\"code\":\"320900\",\"name\":\"盐城市\"},{\"code\":\"320901\",\"name\":\"市辖区\"},{\"code\":\"320902\",\"name\":\"城区\"},{\"code\":\"320921\",\"name\":\"响水县\"},{\"code\":\"320922\",\"name\":\"滨海县\"},{\"code\":\"320923\",\"name\":\"阜宁县\"},{\"code\":\"320924\",\"name\":\"射阳县\"},{\"code\":\"320925\",\"name\":\"建湖县\"},{\"code\":\"320928\",\"name\":\"盐都县\"},{\"code\":\"320981\",\"name\":\"东台市\"},{\"code\":\"320982\",\"name\":\"大丰市\"},{\"code\":\"321000\",\"name\":\"扬州市\"},{\"code\":\"321001\",\"name\":\"市辖区\"},{\"code\":\"321002\",\"name\":\"广陵区\"},{\"code\":\"321011\",\"name\":\"郊区\"},{\"code\":\"321023\",\"name\":\"宝应县\"},{\"code\":\"321027\",\"name\":\"邗江县\"},");
        area.append("{\"code\":\"321081\",\"name\":\"仪征市\"},{\"code\":\"321084\",\"name\":\"高邮市\"},{\"code\":\"321088\",\"name\":\"江都市\"},{\"code\":\"321100\",\"name\":\"镇江市\"},{\"code\":\"321101\",\"name\":\"市辖区\"},{\"code\":\"321102\",\"name\":\"京口区\"},{\"code\":\"321111\",\"name\":\"润州区\"},{\"code\":\"321121\",\"name\":\"丹徒县\"},{\"code\":\"321181\",\"name\":\"丹阳市\"},{\"code\":\"321182\",\"name\":\"扬中市\"},{\"code\":\"321183\",\"name\":\"句容市\"},{\"code\":\"321200\",\"name\":\"泰州市\"},{\"code\":\"321201\",\"name\":\"市辖区\"},{\"code\":\"321202\",\"name\":\"海陵区\"},{\"code\":\"321203\",\"name\":\"高港区\"},{\"code\":\"321281\",\"name\":\"兴化市\"},{\"code\":\"321282\",\"name\":\"靖江市\"},{\"code\":\"321283\",\"name\":\"泰兴市\"},{\"code\":\"321284\",\"name\":\"姜堰市\"},{\"code\":\"321300\",\"name\":\"宿迁市\"},{\"code\":\"321301\",\"name\":\"市辖区\"},{\"code\":\"321302\",\"name\":\"宿城区\"},{\"code\":\"321321\",\"name\":\"宿豫县\"},{\"code\":\"321322\",\"name\":\"沭阳县\"},{\"code\":\"321323\",\"name\":\"泗阳县\"},{\"code\":\"321324\",\"name\":\"泗洪县\"},{\"code\":\"330000\",\"name\":\"浙江省\"},{\"code\":\"330100\",\"name\":\"杭州市\"},{\"code\":\"330101\",\"name\":\"市辖区\"},{\"code\":\"330102\",\"name\":\"上城区\"},{\"code\":\"330103\",\"name\":\"下城区\"},{\"code\":\"330104\",\"name\":\"江干区\"},{\"code\":\"330105\",\"name\":\"拱墅区\"},{\"code\":\"330106\",\"name\":\"西湖区\"},{\"code\":\"330108\",\"name\":\"滨江区\"},{\"code\":\"330122\",\"name\":\"桐庐县\"},{\"code\":\"330127\",\"name\":\"淳安县\"},{\"code\":\"330181\",\"name\":\"萧山市\"},{\"code\":\"330182\",\"name\":\"建德市\"},{\"code\":\"330183\",\"name\":\"富阳市\"},{\"code\":\"330184\",\"name\":\"余杭市\"},{\"code\":\"330185\",\"name\":\"临安市\"},{\"code\":\"330200\",\"name\":\"宁波市\"},{\"code\":\"330201\",\"name\":\"市辖区\"},{\"code\":\"330203\",\"name\":\"海曙区\"},{\"code\":\"330204\",\"name\":\"江东区\"},{\"code\":\"330205\",\"name\":\"江北区\"},{\"code\":\"330206\",\"name\":\"北仑区\"},{\"code\":\"330211\",\"name\":\"镇海区\"},{\"code\":\"330225\",\"name\":\"象山县\"},{\"code\":\"330226\",\"name\":\"宁海县\"},{\"code\":\"330227\",\"name\":\"鄞县\"},{\"code\":\"330281\",\"name\":\"余姚市\"},{\"code\":\"330282\",\"name\":\"慈溪市\"},{\"code\":\"330283\",\"name\":\"奉化市\"},{\"code\":\"330300\",\"name\":\"温州市\"},{\"code\":\"330301\",\"name\":\"市辖区\"},{\"code\":\"330302\",\"name\":\"鹿城区\"},{\"code\":\"330303\",\"name\":\"龙湾区\"},{\"code\":\"330304\",\"name\":\"瓯海区\"},{\"code\":\"330322\",\"name\":\"洞头县\"},{\"code\":\"330324\",\"name\":\"永嘉县\"},{\"code\":\"330326\",\"name\":\"平阳县\"},{\"code\":\"330327\",\"name\":\"苍南县\"},{\"code\":\"330328\",\"name\":\"文成县\"},{\"code\":\"330329\",\"name\":\"泰顺县\"},{\"code\":\"330381\",\"name\":\"瑞安市\"},{\"code\":\"330382\",\"name\":\"乐清市\"},{\"code\":\"330400\",\"name\":\"嘉兴市\"},{\"code\":\"330401\",\"name\":\"市辖区\"},{\"code\":\"330402\",\"name\":\"秀城区\"},{\"code\":\"330411\",\"name\":\"郊区\"},{\"code\":\"330421\",\"name\":\"嘉善县\"},{\"code\":\"330424\",\"name\":\"海盐县\"},{\"code\":\"330481\",\"name\":\"海宁市\"},{\"code\":\"330482\",\"name\":\"平湖市\"},{\"code\":\"330483\",\"name\":\"桐乡市\"},{\"code\":\"330500\",\"name\":\"湖州市\"},{\"code\":\"330501\",\"name\":\"市辖区\"},{\"code\":\"330521\",\"name\":\"德清县\"},{\"code\":\"330522\",\"name\":\"长兴县\"},{\"code\":\"330523\",\"name\":\"安吉县\"},{\"code\":\"330600\",\"name\":\"绍兴市\"},{\"code\":\"330601\",\"name\":\"市辖区\"},{\"code\":\"330602\",\"name\":\"越城区\"},{\"code\":\"330621\",\"name\":\"绍兴县\"},{\"code\":\"330624\",\"name\":\"新昌县\"},{\"code\":\"330681\",\"name\":\"诸暨市\"},{\"code\":\"330682\",\"name\":\"上虞市\"},{\"code\":\"330683\",\"name\":\"嵊州市\"},{\"code\":\"330700\",\"name\":\"金华市\"},{\"code\":\"330701\",\"name\":\"市辖区\"},{\"code\":\"330702\",\"name\":\"婺城区\"},{\"code\":\"330721\",\"name\":\"金华县\"},{\"code\":\"330723\",\"name\":\"武义县\"},{\"code\":\"330726\",\"name\":\"浦江县\"},{\"code\":\"330727\",\"name\":\"磐安县\"},{\"code\":\"330781\",\"name\":\"兰溪市\"},{\"code\":\"330782\",\"name\":\"义乌市\"},{\"code\":\"330783\",\"name\":\"东阳市\"},{\"code\":\"330784\",\"name\":\"永康市\"},{\"code\":\"330800\",\"name\":\"衢州市\"},{\"code\":\"330801\",\"name\":\"市辖区\"},{\"code\":\"330802\",\"name\":\"柯城区\"},{\"code\":\"330821\",\"name\":\"衢县\"},{\"code\":\"330822\",\"name\":\"常山县\"},{\"code\":\"330824\",\"name\":\"开化县\"},{\"code\":\"330825\",\"name\":\"龙游县\"},{\"code\":\"330881\",\"name\":\"江山市\"},{\"code\":\"330900\",\"name\":\"舟山市\"},{\"code\":\"330901\",\"name\":\"市辖区\"},{\"code\":\"330902\",\"name\":\"定海区\"},{\"code\":\"330903\",\"name\":\"普陀区\"},{\"code\":\"330921\",\"name\":\"岱山县\"},{\"code\":\"330922\",\"name\":\"嵊泗县\"},{\"code\":\"331000\",\"name\":\"台州市\"},{\"code\":\"331001\",\"name\":\"市辖区\"},{\"code\":\"331002\",\"name\":\"椒江区\"},{\"code\":\"331003\",\"name\":\"黄岩区\"},{\"code\":\"331004\",\"name\":\"路桥区\"},{\"code\":\"331021\",\"name\":\"玉环县\"},{\"code\":\"331022\",\"name\":\"三门县\"},{\"code\":\"331023\",\"name\":\"天台县\"},{\"code\":\"331024\",\"name\":\"仙居县\"},{\"code\":\"331081\",\"name\":\"温岭市\"},{\"code\":\"331082\",\"name\":\"临海市\"},{\"code\":\"332500\",\"name\":\"丽水地区\"},{\"code\":\"332501\",\"name\":\"丽水市\"},{\"code\":\"332502\",\"name\":\"龙泉市\"},{\"code\":\"332522\",\"name\":\"青田县\"},{\"code\":\"332523\",\"name\":\"云和县\"},{\"code\":\"332525\",\"name\":\"庆元县\"},{\"code\":\"332526\",\"name\":\"缙云县\"},{\"code\":\"332527\",\"name\":\"遂昌县\"},{\"code\":\"332528\",\"name\":\"松阳县\"},{\"code\":\"332529\",\"name\":\"景宁畲族自治县\"},{\"code\":\"340000\",\"name\":\"安徽省\"},{\"code\":\"340100\",\"name\":\"合肥市\"},{\"code\":\"340101\",\"name\":\"市辖区\"},{\"code\":\"340102\",\"name\":\"东市区\"},{\"code\":\"340103\",\"name\":\"中市区\"},{\"code\":\"340104\",\"name\":\"西市区\"},{\"code\":\"340111\",\"name\":\"郊区\"},{\"code\":\"340121\",\"name\":\"长丰县\"},{\"code\":\"340122\",\"name\":\"肥东县\"},{\"code\":\"340123\",\"name\":\"肥西县\"},{\"code\":\"340200\",\"name\":\"芜湖市\"},{\"code\":\"340201\",\"name\":\"市辖区\"},{\"code\":\"340202\",\"name\":\"镜湖区\"},{\"code\":\"340203\",\"name\":\"马塘区\"},{\"code\":\"340204\",\"name\":\"新芜区\"},{\"code\":\"340207\",\"name\":\"鸠江区\"},{\"code\":\"340221\",\"name\":\"芜湖县\"},{\"code\":\"340222\",\"name\":\"繁昌县\"},{\"code\":\"340223\",\"name\":\"南陵县\"},{\"code\":\"340300\",\"name\":\"蚌埠市\"},{\"code\":\"340301\",\"name\":\"市辖区\"},{\"code\":\"340302\",\"name\":\"东市区\"},{\"code\":\"340303\",\"name\":\"中市区\"},{\"code\":\"340304\",\"name\":\"西市区\"},{\"code\":\"340311\",\"name\":\"郊区\"},{\"code\":\"340321\",\"name\":\"怀远县\"},{\"code\":\"340322\",\"name\":\"五河县\"},{\"code\":\"340323\",\"name\":\"固镇县\"},{\"code\":\"340400\",\"name\":\"淮南市\"},{\"code\":\"340401\",\"name\":\"市辖区\"},{\"code\":\"340402\",\"name\":\"大通区\"},{\"code\":\"340403\",\"name\":\"田家庵区\"},{\"code\":\"340404\",\"name\":\"谢家集区\"},{\"code\":\"340405\",\"name\":\"八公山区\"},{\"code\":\"340406\",\"name\":\"潘集区\"},{\"code\":\"340421\",\"name\":\"凤台县\"},{\"code\":\"340500\",\"name\":\"马鞍山市\"},{\"code\":\"340501\",\"name\":\"市辖区\"},{\"code\":\"340502\",\"name\":\"金家庄区\"},{\"code\":\"340503\",\"name\":\"花山区\"},{\"code\":\"340504\",\"name\":\"雨山区\"},{\"code\":\"340505\",\"name\":\"向山区\"},{\"code\":\"340521\",\"name\":\"当涂县\"},{\"code\":\"340600\",\"name\":\"淮北市\"},{\"code\":\"340601\",\"name\":\"市辖区\"},{\"code\":\"340602\",\"name\":\"杜集区\"},{\"code\":\"340603\",\"name\":\"相山区\"},{\"code\":\"340604\",\"name\":\"烈山区\"},{\"code\":\"340621\",\"name\":\"濉溪县\"},{\"code\":\"340700\",\"name\":\"铜陵市\"},{\"code\":\"340701\",\"name\":\"市辖区\"},{\"code\":\"340702\",\"name\":\"铜官山区\"},{\"code\":\"340703\",\"name\":\"狮子山区\"},{\"code\":\"340711\",\"name\":\"郊区\"},{\"code\":\"340721\",\"name\":\"铜陵县\"},{\"code\":\"340800\",\"name\":\"安庆市\"},{\"code\":\"340801\",\"name\":\"市辖区\"},{\"code\":\"340802\",\"name\":\"迎江区\"},{\"code\":\"340803\",\"name\":\"大观区\"},{\"code\":\"340811\",\"name\":\"郊区\"},{\"code\":\"340822\",\"name\":\"怀宁县\"},{\"code\":\"340823\",\"name\":\"枞阳县\"},{\"code\":\"340824\",\"name\":\"潜山县\"},{\"code\":\"340825\",\"name\":\"太湖县\"},{\"code\":\"340826\",\"name\":\"宿松县\"},{\"code\":\"340827\",\"name\":\"望江县\"},{\"code\":\"340828\",\"name\":\"岳西县\"},{\"code\":\"340881\",\"name\":\"桐城市\"},{\"code\":\"341000\",\"name\":\"黄山市\"},{\"code\":\"341001\",\"name\":\"市辖区\"},{\"code\":\"341002\",\"name\":\"屯溪区\"},{\"code\":\"341003\",\"name\":\"黄山区\"},{\"code\":\"341004\",\"name\":\"徽州区\"},{\"code\":\"341021\",\"name\":\"歙县\"},{\"code\":\"341022\",\"name\":\"休宁县\"},{\"code\":\"341023\",\"name\":\"黟县\"},{\"code\":\"341024\",\"name\":\"祁门县\"},{\"code\":\"341100\",\"name\":\"滁州市\"},{\"code\":\"341101\",\"name\":\"市辖区\"},{\"code\":\"341102\",\"name\":\"琅琊区\"},{\"code\":\"341103\",\"name\":\"南谯区\"},{\"code\":\"341122\",\"name\":\"来安县\"},{\"code\":\"341124\",\"name\":\"全椒县\"},{\"code\":\"341125\",\"name\":\"定远县\"},{\"code\":\"341126\",\"name\":\"凤阳县\"},{\"code\":\"341181\",\"name\":\"天长市\"},{\"code\":\"341182\",\"name\":\"明光市\"},{\"code\":\"341200\",\"name\":\"阜阳市\"},{\"code\":\"341201\",\"name\":\"市辖区\"},{\"code\":\"341202\",\"name\":\"颍州区\"},{\"code\":\"341203\",\"name\":\"颍东区\"},{\"code\":\"341204\",\"name\":\"颍泉区\"},{\"code\":\"341221\",\"name\":\"临泉县\"},{\"code\":\"341222\",\"name\":\"太和县\"},{\"code\":\"341223\",\"name\":\"涡阳县\"},{\"code\":\"341224\",\"name\":\"蒙城县\"},{\"code\":\"341225\",\"name\":\"阜南县\"},{\"code\":\"341226\",\"name\":\"颍上县\"},{\"code\":\"341227\",\"name\":\"利辛县\"},{\"code\":\"341281\",\"name\":\"亳州市\"},{\"code\":\"341282\",\"name\":\"界首市\"},{\"code\":\"341300\",\"name\":\"宿州市\"},{\"code\":\"341301\",\"name\":\"市辖区\"},{\"code\":\"341302\",\"name\":\" 墉桥区\"},{\"code\":\"341321\",\"name\":\"砀山县\"},{\"code\":\"341322\",\"name\":\"萧县\"},{\"code\":\"341323\",\"name\":\"灵璧县\"},{\"code\":\"341324\",\"name\":\"泗县\"},{\"code\":\"342400\",\"name\":\"六安地区\"},{\"code\":\"342401\",\"name\":\"六安市\"},{\"code\":\"342422\",\"name\":\"寿县\"},{\"code\":\"342423\",\"name\":\"霍邱县\"},{\"code\":\"342425\",\"name\":\"舒城县\"},{\"code\":\"342426\",\"name\":\"金寨县\"},{\"code\":\"342427\",\"name\":\"霍山县\"},{\"code\":\"342500\",\"name\":\"宣城地区\"},{\"code\":\"342501\",\"name\":\"宣州市\"},{\"code\":\"342502\",\"name\":\"宁国市\"},{\"code\":\"342522\",\"name\":\"郎溪县\"},{\"code\":\"342523\",\"name\":\"广德县\"},{\"code\":\"342529\",\"name\":\"泾县\"},{\"code\":\"342530\",\"name\":\"旌德县\"},{\"code\":\"342531\",\"name\":\"绩溪县\"},{\"code\":\"342600\",\"name\":\"巢湖地区\"},{\"code\":\"342601\",\"name\":\"巢湖市\"},{\"code\":\"342622\",\"name\":\"庐江县\"},{\"code\":\"342623\",\"name\":\"无为县\"},{\"code\":\"342625\",\"name\":\"含山县\"},{\"code\":\"342626\",\"name\":\"和县\"},{\"code\":\"342900\",\"name\":\"池州地区\"},{\"code\":\"342901\",\"name\":\"贵池市\"},{\"code\":\"342921\",\"name\":\"东至县\"},{\"code\":\"342922\",\"name\":\"石台县\"},{\"code\":\"342923\",\"name\":\"青阳县\"},{\"code\":\"350000\",\"name\":\"福建省\"},{\"code\":\"350100\",\"name\":\"福州市\"},{\"code\":\"350101\",\"name\":\"市辖区\"},{\"code\":\"350102\",\"name\":\"鼓楼区\"},{\"code\":\"350103\",\"name\":\"台江区\"},{\"code\":\"350104\",\"name\":\"仓山区\"},{\"code\":\"350105\",\"name\":\"马尾区\"},{\"code\":\"350111\",\"name\":\"晋安区\"},{\"code\":\"350121\",\"name\":\"闽侯县\"},{\"code\":\"350122\",\"name\":\"连江县\"},{\"code\":\"350123\",\"name\":\"罗源县\"},{\"code\":\"350124\",\"name\":\"闽清县\"},{\"code\":\"350125\",\"name\":\"永泰县\"},{\"code\":\"350128\",\"name\":\"平潭县\"},{\"code\":\"350181\",\"name\":\"福清市\"},{\"code\":\"350182\",\"name\":\"长乐市\"},{\"code\":\"350200\",\"name\":\"厦门市\"},{\"code\":\"350201\",\"name\":\"市辖区\"},{\"code\":\"350202\",\"name\":\"鼓浪屿区\"},{\"code\":\"350203\",\"name\":\"思明区\"},{\"code\":\"350204\",\"name\":\"开元区\"},{\"code\":\"350205\",\"name\":\"杏林区\"},{\"code\":\"350206\",\"name\":\"湖里区\"},{\"code\":\"350211\",\"name\":\"集美区\"},{\"code\":\"350212\",\"name\":\"同安区\"},{\"code\":\"350300\",\"name\":\"莆田市\"},{\"code\":\"350301\",\"name\":\"市辖区\"},{\"code\":\"350302\",\"name\":\"城厢区\"},{\"code\":\"350303\",\"name\":\"涵江区\"},{\"code\":\"350321\",\"name\":\"莆田县\"},{\"code\":\"350322\",\"name\":\"仙游县\"},{\"code\":\"350400\",\"name\":\"三明市\"},{\"code\":\"350401\",\"name\":\"市辖区\"},{\"code\":\"350402\",\"name\":\"梅列区\"},{\"code\":\"350403\",\"name\":\"三元区\"},{\"code\":\"350421\",\"name\":\"明溪县\"},{\"code\":\"350423\",\"name\":\"清流县\"},{\"code\":\"350424\",\"name\":\"宁化县\"},{\"code\":\"350425\",\"name\":\"大田县\"},{\"code\":\"350426\",\"name\":\"尤溪县\"},{\"code\":\"350427\",\"name\":\"沙县\"},{\"code\":\"350428\",\"name\":\"将乐县\"},{\"code\":\"350429\",\"name\":\"泰宁县\"},{\"code\":\"350430\",\"name\":\"建宁县\"},{\"code\":\"350481\",\"name\":\"永安市\"},{\"code\":\"350500\",\"name\":\"泉州市\"},{\"code\":\"350501\",\"name\":\"市辖区\"},{\"code\":\"350502\",\"name\":\"鲤城区\"},{\"code\":\"350503\",\"name\":\"丰泽区\"},{\"code\":\"350504\",\"name\":\"洛江区\"},{\"code\":\"350521\",\"name\":\"惠安县\"},{\"code\":\"350524\",\"name\":\"安溪县\"},{\"code\":\"350525\",\"name\":\"永春县\"},{\"code\":\"350526\",\"name\":\"德化县\"},{\"code\":\"350527\",\"name\":\"金门县\"},{\"code\":\"350581\",\"name\":\"石狮市\"},{\"code\":\"350582\",\"name\":\"晋江市\"},{\"code\":\"350583\",\"name\":\"南安市\"},{\"code\":\"350600\",\"name\":\"漳州市\"},{\"code\":\"350601\",\"name\":\"市辖区\"},{\"code\":\"350602\",\"name\":\"芗城区\"},{\"code\":\"350603\",\"name\":\"龙文区\"},{\"code\":\"350622\",\"name\":\"云霄县\"},{\"code\":\"350623\",\"name\":\"漳浦县\"},{\"code\":\"350624\",\"name\":\"诏安县\"},{\"code\":\"350625\",\"name\":\"长泰县\"},{\"code\":\"350626\",\"name\":\"东山县\"},{\"code\":\"350627\",\"name\":\"南靖县\"},{\"code\":\"350628\",\"name\":\"平和县\"},{\"code\":\"350629\",\"name\":\"华安县\"},{\"code\":\"350681\",\"name\":\"龙海市\"},{\"code\":\"350700\",\"name\":\"南平市\"},{\"code\":\"350701\",\"name\":\"市辖区\"},{\"code\":\"350702\",\"name\":\"延平区\"},{\"code\":\"350721\",\"name\":\"顺昌县\"},{\"code\":\"350722\",\"name\":\"浦城县\"},{\"code\":\"350723\",\"name\":\"光泽县\"},{\"code\":\"350724\",\"name\":\"松溪县\"},{\"code\":\"350725\",\"name\":\"政和县\"},{\"code\":\"350781\",\"name\":\"邵武市\"},{\"code\":\"350782\",\"name\":\"武夷山市\"},{\"code\":\"350783\",\"name\":\"建瓯市\"},{\"code\":\"350784\",\"name\":\"建阳市\"},{\"code\":\"350800\",\"name\":\"龙岩市\"},{\"code\":\"350801\",\"name\":\"市辖区\"},{\"code\":\"350802\",\"name\":\"新罗区\"},{\"code\":\"350821\",\"name\":\"长汀县\"},{\"code\":\"350822\",\"name\":\"永定县\"},{\"code\":\"350823\",\"name\":\"上杭县\"},{\"code\":\"350824\",\"name\":\"武平县\"},{\"code\":\"350825\",\"name\":\"连城县\"},{\"code\":\"350881\",\"name\":\"漳平市\"},{\"code\":\"352200\",\"name\":\"宁德地区\"},{\"code\":\"352201\",\"name\":\"宁德市\"},{\"code\":\"352202\",\"name\":\"福安市\"},{\"code\":\"352203\",\"name\":\"福鼎市\"},{\"code\":\"352225\",\"name\":\"霞浦县\"},{\"code\":\"352227\",\"name\":\"古田县\"},{\"code\":\"352228\",\"name\":\"屏南县\"},{\"code\":\"352229\",\"name\":\"寿宁县\"},{\"code\":\"352230\",\"name\":\"周宁县\"},{\"code\":\"352231\",\"name\":\"柘荣县\"},{\"code\":\"360000\",\"name\":\"江西省\"},{\"code\":\"360100\",\"name\":\"南昌市\"},{\"code\":\"360101\",\"name\":\"市辖区\"},{\"code\":\"360102\",\"name\":\"东湖区\"},{\"code\":\"360103\",\"name\":\"西湖区\"},{\"code\":\"360104\",\"name\":\"青云谱区\"},{\"code\":\"360105\",\"name\":\"湾里区\"},{\"code\":\"360111\",\"name\":\"郊区\"},{\"code\":\"360121\",\"name\":\"南昌县\"},{\"code\":\"360122\",\"name\":\"新建县\"},{\"code\":\"360123\",\"name\":\"安义县\"},{\"code\":\"360124\",\"name\":\"进贤县\"},{\"code\":\"360200\",\"name\":\"景德镇市\"},{\"code\":\"360201\",\"name\":\"市辖区\"},{\"code\":\"360202\",\"name\":\"昌江区\"},{\"code\":\"360203\",\"name\":\"珠山区\"},{\"code\":\"360222\",\"name\":\"浮梁县\"},{\"code\":\"360281\",\"name\":\"乐平市\"},{\"code\":\"360300\",\"name\":\"萍乡市\"},{\"code\":\"360301\",\"name\":\"市辖区\"},{\"code\":\"360302\",\"name\":\"安源区\"},{\"code\":\"360313\",\"name\":\"湘东区\"},{\"code\":\"360321\",\"name\":\"莲花县\"},{\"code\":\"360322\",\"name\":\"上栗县\"},{\"code\":\"360323\",\"name\":\"芦溪县\"},{\"code\":\"360400\",\"name\":\"九江市\"},{\"code\":\"360401\",\"name\":\"市辖区\"},{\"code\":\"360402\",\"name\":\"庐山区\"},{\"code\":\"360403\",\"name\":\"浔阳区\"},{\"code\":\"360421\",\"name\":\"九江县\"},{\"code\":\"360423\",\"name\":\"武宁县\"},{\"code\":\"360424\",\"name\":\"修水县\"},{\"code\":\"360425\",\"name\":\"永修县\"},{\"code\":\"360426\",\"name\":\"德安县\"},{\"code\":\"360427\",\"name\":\"星子县\"},{\"code\":\"360428\",\"name\":\"都昌县\"},{\"code\":\"360429\",\"name\":\"湖口县\"},{\"code\":\"360430\",\"name\":\"彭泽县\"},{\"code\":\"360481\",\"name\":\"瑞昌市\"},{\"code\":\"360500\",\"name\":\"新余市\"},{\"code\":\"360501\",\"name\":\"市辖区\"},{\"code\":\"360502\",\"name\":\"渝水区\"},{\"code\":\"360521\",\"name\":\"分宜县\"},{\"code\":\"360600\",\"name\":\"鹰潭市\"},{\"code\":\"360601\",\"name\":\"市辖区\"},{\"code\":\"360602\",\"name\":\"月湖区\"},{\"code\":\"360622\",\"name\":\"余江县\"},{\"code\":\"360681\",\"name\":\"贵溪市\"},{\"code\":\"360700\",\"name\":\"赣州市\"},{\"code\":\"360701\",\"name\":\"市辖区\"},{\"code\":\"360702\",\"name\":\"章贡区\"},{\"code\":\"360721\",\"name\":\"赣县\"},{\"code\":\"360722\",\"name\":\"信丰县\"},{\"code\":\"360723\",\"name\":\"大余县\"},{\"code\":\"360724\",\"name\":\"上犹县\"},{\"code\":\"360725\",\"name\":\"崇义县\"},{\"code\":\"360726\",\"name\":\"安远县\"},{\"code\":\"360727\",\"name\":\"龙南县\"},{\"code\":\"360728\",\"name\":\"定南县\"},{\"code\":\"360729\",\"name\":\"全南县\"},{\"code\":\"360730\",\"name\":\"宁都县\"},{\"code\":\"360731\",\"name\":\"于都县\"},{\"code\":\"360732\",\"name\":\"兴国县\"},{\"code\":\"360733\",\"name\":\"会昌县\"},{\"code\":\"360734\",\"name\":\"寻乌县\"},{\"code\":\"360735\",\"name\":\"石城县\"},{\"code\":\"360781\",\"name\":\"瑞金市\"},{\"code\":\"360782\",\"name\":\"南康市\"},{\"code\":\"362200\",\"name\":\"宜春地区\"},{\"code\":\"362201\",\"name\":\"宜春市\"},{\"code\":\"362202\",\"name\":\"丰城市\"},{\"code\":\"362203\",\"name\":\"樟树市\"},{\"code\":\"362204\",\"name\":\"高安市\"},{\"code\":\"362226\",\"name\":\"奉新县\"},{\"code\":\"362227\",\"name\":\"万载县\"},{\"code\":\"362228\",\"name\":\"上高县\"},{\"code\":\"362229\",\"name\":\"宜丰县\"},{\"code\":\"362232\",\"name\":\"靖安县\"},{\"code\":\"362233\",\"name\":\"铜鼓县\"},{\"code\":\"362300\",\"name\":\"上饶地区\"},{\"code\":\"362301\",\"name\":\"上饶市\"},{\"code\":\"362302\",\"name\":\"德兴市\"},{\"code\":\"362321\",\"name\":\"上饶县\"},{\"code\":\"362322\",\"name\":\"广丰县\"},{\"code\":\"362323\",\"name\":\"玉山县\"},{\"code\":\"362324\",\"name\":\"铅山县\"},{\"code\":\"362325\",\"name\":\"横峰县\"},{\"code\":\"362326\",\"name\":\"弋阳县\"},{\"code\":\"362329\",\"name\":\"余干县\"},{\"code\":\"362330\",\"name\":\"波阳县\"},{\"code\":\"362331\",\"name\":\"万年县\"},{\"code\":\"362334\",\"name\":\"婺源县\"},{\"code\":\"362400\",\"name\":\"吉安地区\"},{\"code\":\"362401\",\"name\":\"吉安市\"},{\"code\":\"362402\",\"name\":\"井冈山市\"},{\"code\":\"362421\",\"name\":\"吉安县\"},{\"code\":\"362422\",\"name\":\"吉水县\"},{\"code\":\"362423\",\"name\":\"峡江县\"},{\"code\":\"362424\",\"name\":\"新干县\"},{\"code\":\"362425\",\"name\":\"永丰县\"},{\"code\":\"362426\",\"name\":\"泰和县\"},{\"code\":\"362427\",\"name\":\"遂川县\"},{\"code\":\"362428\",\"name\":\"万安县\"},{\"code\":\"362429\",\"name\":\"安福县\"},{\"code\":\"362430\",\"name\":\"永新县\"},{\"code\":\"362432\",");
        area.append("\"name\":\"宁冈县\"},{\"code\":\"362500\",\"name\":\"抚州地区\"},{\"code\":\"362502\",\"name\":\"临川市\"},{\"code\":\"362522\",\"name\":\"南城县\"},{\"code\":\"362523\",\"name\":\"黎川县\"},{\"code\":\"362524\",\"name\":\"南丰县\"},{\"code\":\"362525\",\"name\":\"崇仁县\"},{\"code\":\"362526\",\"name\":\"乐安县\"},{\"code\":\"362527\",\"name\":\"宜黄县\"},{\"code\":\"362528\",\"name\":\"金溪县\"},{\"code\":\"362529\",\"name\":\"资溪县\"},{\"code\":\"362531\",\"name\":\"东乡县\"},{\"code\":\"362532\",\"name\":\"广昌县\"},{\"code\":\"370000\",\"name\":\"山东省\"},{\"code\":\"370100\",\"name\":\"济南市\"},{\"code\":\"370101\",\"name\":\"市辖区\"},{\"code\":\"370102\",\"name\":\"历下区\"},{\"code\":\"370103\",\"name\":\"市中区\"},{\"code\":\"370104\",\"name\":\"槐荫区\"},{\"code\":\"370105\",\"name\":\"天桥区\"},{\"code\":\"370112\",\"name\":\"历城区\"},{\"code\":\"370123\",\"name\":\"长清县\"},{\"code\":\"370124\",\"name\":\"平阴县\"},{\"code\":\"370125\",\"name\":\"济阳县\"},{\"code\":\"370126\",\"name\":\"商河县\"},{\"code\":\"370181\",\"name\":\"章丘市\"},{\"code\":\"370200\",\"name\":\"青岛市\"},{\"code\":\"370201\",\"name\":\"市辖区\"},{\"code\":\"370202\",\"name\":\"市南区\"},{\"code\":\"370203\",\"name\":\"市北区\"},{\"code\":\"370205\",\"name\":\"四方区\"},{\"code\":\"370211\",\"name\":\"黄岛区\"},{\"code\":\"370212\",\"name\":\"崂山区\"},{\"code\":\"370213\",\"name\":\"李沧区\"},{\"code\":\"370214\",\"name\":\"城阳区\"},{\"code\":\"370281\",\"name\":\"胶州市\"},{\"code\":\"370282\",\"name\":\"即墨市\"},{\"code\":\"370283\",\"name\":\"平度市\"},{\"code\":\"370284\",\"name\":\"胶南市\"},{\"code\":\"370285\",\"name\":\"莱西市\"},{\"code\":\"370300\",\"name\":\"淄博市\"},{\"code\":\"370301\",\"name\":\"市辖区\"},{\"code\":\"370302\",\"name\":\"淄川区\"},{\"code\":\"370303\",\"name\":\"张店区\"},{\"code\":\"370304\",\"name\":\"博山区\"},{\"code\":\"370305\",\"name\":\"临淄区\"},{\"code\":\"370306\",\"name\":\"周村区\"},{\"code\":\"370321\",\"name\":\"桓台县\"},{\"code\":\"370322\",\"name\":\"高青县\"},{\"code\":\"370323\",\"name\":\"沂源县\"},{\"code\":\"370400\",\"name\":\"枣庄市\"},{\"code\":\"370401\",\"name\":\"市辖区\"},{\"code\":\"370402\",\"name\":\"市中区\"},{\"code\":\"370403\",\"name\":\"薛城区\"},{\"code\":\"370404\",\"name\":\"峄城区\"},{\"code\":\"370405\",\"name\":\"台儿庄区\"},{\"code\":\"370406\",\"name\":\"山亭区\"},{\"code\":\"370481\",\"name\":\"滕州市\"},{\"code\":\"370500\",\"name\":\"东营市\"},{\"code\":\"370501\",\"name\":\"市辖区\"},{\"code\":\"370502\",\"name\":\"东营区\"},{\"code\":\"370503\",\"name\":\"河口区\"},{\"code\":\"370521\",\"name\":\"垦利县\"},{\"code\":\"370522\",\"name\":\"利津县\"},{\"code\":\"370523\",\"name\":\"广饶县\"},{\"code\":\"370600\",\"name\":\"烟台市\"},{\"code\":\"370601\",\"name\":\"市辖区\"},{\"code\":\"370602\",\"name\":\"芝罘区\"},{\"code\":\"370611\",\"name\":\"福山区\"},{\"code\":\"370612\",\"name\":\"牟平区\"},{\"code\":\"370613\",\"name\":\"莱山区\"},{\"code\":\"370634\",\"name\":\"长岛县\"},{\"code\":\"370681\",\"name\":\"龙口市\"},{\"code\":\"370682\",\"name\":\"莱阳市\"},{\"code\":\"370683\",\"name\":\"莱州市\"},{\"code\":\"370684\",\"name\":\"蓬莱市\"},{\"code\":\"370685\",\"name\":\"招远市\"},{\"code\":\"370686\",\"name\":\"栖霞市\"},{\"code\":\"370687\",\"name\":\"海阳市\"},{\"code\":\"370700\",\"name\":\"潍坊市\"},{\"code\":\"370701\",\"name\":\"市辖区\"},{\"code\":\"370702\",\"name\":\"潍城区\"},{\"code\":\"370703\",\"name\":\"寒亭区\"},{\"code\":\"370704\",\"name\":\"坊子区\"},{\"code\":\"370705\",\"name\":\"奎文区\"},{\"code\":\"370724\",\"name\":\"临朐县\"},{\"code\":\"370725\",\"name\":\"昌乐县\"},{\"code\":\"370781\",\"name\":\"青州市\"},{\"code\":\"370782\",\"name\":\"诸城市\"},{\"code\":\"370783\",\"name\":\"寿光市\"},{\"code\":\"370784\",\"name\":\"安丘市\"},{\"code\":\"370785\",\"name\":\"高密市\"},{\"code\":\"370786\",\"name\":\"昌邑市\"},{\"code\":\"370800\",\"name\":\"济宁市\"},{\"code\":\"370801\",\"name\":\"市辖区\"},{\"code\":\"370802\",\"name\":\"市中区\"},{\"code\":\"370811\",\"name\":\"任城区\"},{\"code\":\"370826\",\"name\":\"微山县\"},{\"code\":\"370827\",\"name\":\"鱼台县\"},{\"code\":\"370828\",\"name\":\"金乡县\"},{\"code\":\"370829\",\"name\":\"嘉祥县\"},{\"code\":\"370830\",\"name\":\"汶上县\"},{\"code\":\"370831\",\"name\":\"泗水县\"},{\"code\":\"370832\",\"name\":\"梁山县\"},{\"code\":\"370881\",\"name\":\"曲阜市\"},{\"code\":\"370882\",\"name\":\"兖州市\"},{\"code\":\"370883\",\"name\":\"邹城市\"},{\"code\":\"370900\",\"name\":\"泰安市\"},{\"code\":\"370901\",\"name\":\"市辖区\"},{\"code\":\"370902\",\"name\":\"泰山区\"},{\"code\":\"370911\",\"name\":\"郊区\"},{\"code\":\"370921\",\"name\":\"宁阳县\"},{\"code\":\"370923\",\"name\":\"东平县\"},{\"code\":\"370982\",\"name\":\"新泰市\"},{\"code\":\"370983\",\"name\":\"肥城市\"},{\"code\":\"371000\",\"name\":\"威海市\"},{\"code\":\"371001\",\"name\":\"市辖区\"},{\"code\":\"371002\",\"name\":\"环翠区\"},{\"code\":\"371081\",\"name\":\"文登市\"},{\"code\":\"371082\",\"name\":\"荣成市\"},{\"code\":\"371083\",\"name\":\"乳山市\"},{\"code\":\"371100\",\"name\":\"日照市\"},{\"code\":\"371101\",\"name\":\"市辖区\"},{\"code\":\"371102\",\"name\":\"东港区\"},{\"code\":\"371121\",\"name\":\"五莲县\"},{\"code\":\"371122\",\"name\":\"莒县\"},{\"code\":\"371200\",\"name\":\"莱芜市\"},{\"code\":\"371201\",\"name\":\"市辖区\"},{\"code\":\"371202\",\"name\":\"莱城区\"},{\"code\":\"371203\",\"name\":\"钢城区\"},{\"code\":\"371300\",\"name\":\"临沂市\"},{\"code\":\"371301\",\"name\":\"市辖区\"},{\"code\":\"371302\",\"name\":\"兰山区\"},{\"code\":\"371311\",\"name\":\"罗庄区\"},{\"code\":\"371312\",\"name\":\"河东区\"},{\"code\":\"371321\",\"name\":\"沂南县\"},{\"code\":\"371322\",\"name\":\"郯城县\"},{\"code\":\"371323\",\"name\":\"沂水县\"},{\"code\":\"371324\",\"name\":\"苍山县\"},{\"code\":\"371325\",\"name\":\"费县\"},{\"code\":\"371326\",\"name\":\"平邑县\"},{\"code\":\"371327\",\"name\":\"莒南县\"},{\"code\":\"371328\",\"name\":\"蒙阴县\"},{\"code\":\"371329\",\"name\":\"临沭县\"},{\"code\":\"371400\",\"name\":\"德州市\"},{\"code\":\"371401\",\"name\":\"市辖区\"},{\"code\":\"371402\",\"name\":\"德城区\"},{\"code\":\"371421\",\"name\":\"陵县\"},{\"code\":\"371422\",\"name\":\"宁津县\"},{\"code\":\"371423\",\"name\":\"庆云县\"},{\"code\":\"371424\",\"name\":\"临邑县\"},{\"code\":\"371425\",\"name\":\"齐河县\"},{\"code\":\"371426\",\"name\":\"平原县\"},{\"code\":\"371427\",\"name\":\"夏津县\"},{\"code\":\"371428\",\"name\":\"武城县\"},{\"code\":\"371481\",\"name\":\"乐陵市\"},{\"code\":\"371482\",\"name\":\"禹城市\"},{\"code\":\"371500\",\"name\":\"聊城市\"},{\"code\":\"371501\",\"name\":\"市辖区\"},{\"code\":\"371502\",\"name\":\"东昌府区\"},{\"code\":\"371521\",\"name\":\"阳谷县\"},{\"code\":\"371522\",\"name\":\"莘县\"},{\"code\":\"371523\",\"name\":\"茌平县\"},{\"code\":\"371524\",\"name\":\"东阿县\"},{\"code\":\"371525\",\"name\":\"冠县\"},{\"code\":\"371526\",\"name\":\"高唐县\"},{\"code\":\"371581\",\"name\":\"临清市\"},{\"code\":\"372300\",\"name\":\"滨州地区\"},{\"code\":\"372301\",\"name\":\"滨州市\"},{\"code\":\"372321\",\"name\":\"惠民县\"},{\"code\":\"372323\",\"name\":\"阳信县\"},{\"code\":\"372324\",\"name\":\"无棣县\"},{\"code\":\"372325\",\"name\":\"沾化县\"},{\"code\":\"372328\",\"name\":\"博兴县\"},{\"code\":\"372330\",\"name\":\"邹平县\"},{\"code\":\"372900\",\"name\":\"菏泽地区\"},{\"code\":\"372901\",\"name\":\"菏泽市\"},{\"code\":\"372922\",\"name\":\"曹县\"},{\"code\":\"372923\",\"name\":\"定陶县\"},{\"code\":\"372924\",\"name\":\"成武县\"},{\"code\":\"372925\",\"name\":\"单县\"},{\"code\":\"372926\",\"name\":\"巨野县\"},{\"code\":\"372928\",\"name\":\"郓城县\"},{\"code\":\"372929\",\"name\":\"鄄城县\"},{\"code\":\"372930\",\"name\":\"东明县\"},{\"code\":\"410000\",\"name\":\"河南省\"},{\"code\":\"410100\",\"name\":\"郑州市\"},{\"code\":\"410101\",\"name\":\"市辖区\"},{\"code\":\"410102\",\"name\":\"中原区\"},{\"code\":\"410103\",\"name\":\"二七区\"},{\"code\":\"410104\",\"name\":\"管城回族区\"},{\"code\":\"410105\",\"name\":\"金水区\"},{\"code\":\"410106\",\"name\":\"上街区\"},{\"code\":\"410108\",\"name\":\"邙山区\"},{\"code\":\"410122\",\"name\":\"中牟县\"},{\"code\":\"410181\",\"name\":\"巩义市\"},{\"code\":\"410182\",\"name\":\"荥阳市\"},{\"code\":\"410183\",\"name\":\"新密市\"},{\"code\":\"410184\",\"name\":\"新郑市\"},{\"code\":\"410185\",\"name\":\"登封市\"},{\"code\":\"410200\",\"name\":\"开封市\"},{\"code\":\"410201\",\"name\":\"市辖区\"},{\"code\":\"410202\",\"name\":\"龙亭区\"},{\"code\":\"410203\",\"name\":\"顺河回族区\"},{\"code\":\"410204\",\"name\":\"鼓楼区\"},{\"code\":\"410205\",\"name\":\"南关区\"},{\"code\":\"410211\",\"name\":\"郊区\"},{\"code\":\"410221\",\"name\":\"杞县\"},{\"code\":\"410222\",\"name\":\"通许县\"},{\"code\":\"410223\",\"name\":\"尉氏县\"},{\"code\":\"410224\",\"name\":\"开封县\"},{\"code\":\"410225\",\"name\":\"兰考县\"},{\"code\":\"410300\",\"name\":\"洛阳市\"},{\"code\":\"410301\",\"name\":\"市辖区\"},{\"code\":\"410302\",\"name\":\"老城区\"},");
        area.append("{\"code\":\"410303\",\"name\":\"西工区\"},{\"code\":\"410304\",\"name\":\"廛河回族区\"},{\"code\":\"410305\",\"name\":\"涧西区\"},{\"code\":\"410306\",\"name\":\"吉利区\"},{\"code\":\"410311\",\"name\":\"郊区\"},{\"code\":\"410322\",\"name\":\"孟津县\"},{\"code\":\"410323\",\"name\":\"新安县\"},{\"code\":\"410324\",\"name\":\"栾川县\"},{\"code\":\"410325\",\"name\":\"嵩县\"},{\"code\":\"410326\",\"name\":\"汝阳县\"},{\"code\":\"410327\",\"name\":\"宜阳县\"},{\"code\":\"410328\",\"name\":\"洛宁县\"},{\"code\":\"410329\",\"name\":\"伊川县\"},{\"code\":\"410381\",\"name\":\"偃师市\"},{\"code\":\"410400\",\"name\":\"平顶山市\"},{\"code\":\"410401\",\"name\":\"市辖区\"},{\"code\":\"410402\",\"name\":\"新华区\"},{\"code\":\"410403\",\"name\":\"卫东区\"},{\"code\":\"410404\",\"name\":\"石龙区\"},{\"code\":\"410411\",\"name\":\"湛河区\"},{\"code\":\"410421\",\"name\":\"宝丰县\"},{\"code\":\"410422\",\"name\":\"叶县\"},{\"code\":\"410423\",\"name\":\"鲁山县\"},{\"code\":\"410425\",\"name\":\"郏县\"},{\"code\":\"410481\",\"name\":\"舞钢市\"},{\"code\":\"410482\",\"name\":\"汝州市\"},{\"code\":\"410500\",\"name\":\"安阳市\"},{\"code\":\"410501\",\"name\":\"市辖区\"},{\"code\":\"410502\",\"name\":\"文峰区\"},{\"code\":\"410503\",\"name\":\"北关区\"},{\"code\":\"410504\",\"name\":\"铁西区\"},{\"code\":\"410511\",\"name\":\"郊区\"},{\"code\":\"410522\",\"name\":\"安阳县\"},{\"code\":\"410523\",\"name\":\"汤阴县\"},{\"code\":\"410526\",\"name\":\"滑县\"},{\"code\":\"410527\",\"name\":\"内黄县\"},{\"code\":\"410581\",\"name\":\"林州市\"},{\"code\":\"410600\",\"name\":\"鹤壁市\"},{\"code\":\"410601\",\"name\":\"市辖区\"},{\"code\":\"410602\",\"name\":\"鹤山区\"},{\"code\":\"410603\",\"name\":\"山城区\"},{\"code\":\"410611\",\"name\":\"郊区\"},{\"code\":\"410621\",\"name\":\"浚县\"},{\"code\":\"410622\",\"name\":\"淇县\"},{\"code\":\"410700\",\"name\":\"新乡市\"},{\"code\":\"410701\",\"name\":\"市辖区\"},{\"code\":\"410702\",\"name\":\"红旗区\"},{\"code\":\"410703\",\"name\":\"新华区\"},{\"code\":\"410704\",\"name\":\"北站区\"},{\"code\":\"410711\",\"name\":\"郊区\"},{\"code\":\"410721\",\"name\":\"新乡县\"},{\"code\":\"410724\",\"name\":\"获嘉县\"},{\"code\":\"410725\",\"name\":\"原阳县\"},{\"code\":\"410726\",\"name\":\"延津县\"},{\"code\":\"410727\",\"name\":\"封丘县\"},{\"code\":\"410728\",\"name\":\"长垣县\"},{\"code\":\"410781\",\"name\":\"卫辉市\"},{\"code\":\"410782\",\"name\":\"辉县市\"},{\"code\":\"410800\",\"name\":\"焦作市\"},{\"code\":\"410801\",\"name\":\"市辖区\"},{\"code\":\"410802\",\"name\":\"解放区\"},{\"code\":\"410803\",\"name\":\"中站区\"},{\"code\":\"410804\",\"name\":\"马村区\"},{\"code\":\"410811\",\"name\":\"山阳区\"},{\"code\":\"410821\",\"name\":\"修武县\"},{\"code\":\"410822\",\"name\":\"博爱县\"},{\"code\":\"410823\",\"name\":\"武陟县\"},{\"code\":\"410825\",\"name\":\"温县\"},{\"code\":\"410881\",\"name\":\"济源市\"},{\"code\":\"410882\",\"name\":\"沁阳市\"},{\"code\":\"410883\",\"name\":\"孟州市\"},{\"code\":\"410900\",\"name\":\"濮阳市\"},{\"code\":\"410901\",\"name\":\"市辖区\"},{\"code\":\"410902\",\"name\":\"市区\"},{\"code\":\"410922\",\"name\":\"清丰县\"},{\"code\":\"410923\",\"name\":\"南乐县\"},{\"code\":\"410926\",\"name\":\"范县\"},{\"code\":\"410927\",\"name\":\"台前县\"},{\"code\":\"410928\",\"name\":\"濮阳县\"},{\"code\":\"411000\",\"name\":\"许昌市\"},{\"code\":\"411001\",\"name\":\"市辖区\"},{\"code\":\"411002\",\"name\":\"魏都区\"},{\"code\":\"411023\",\"name\":\"许昌县\"},{\"code\":\"411024\",\"name\":\"鄢陵县\"},{\"code\":\"411025\",\"name\":\"襄城县\"},{\"code\":\"411081\",\"name\":\"禹州市\"},{\"code\":\"411082\",\"name\":\"长葛市\"},{\"code\":\"411100\",\"name\":\"漯河市\"},{\"code\":\"411101\",\"name\":\"市辖区\"},{\"code\":\"411102\",\"name\":\"源汇区\"},{\"code\":\"411121\",\"name\":\"舞阳县\"},{\"code\":\"411122\",\"name\":\"临颍县\"},{\"code\":\"411123\",\"name\":\"郾城县\"},{\"code\":\"411200\",\"name\":\"三门峡市\"},{\"code\":\"411201\",\"name\":\"市辖区\"},{\"code\":\"411202\",\"name\":\"湖滨区\"},{\"code\":\"411221\",\"name\":\"渑池县\"},{\"code\":\"411222\",\"name\":\"陕县\"},{\"code\":\"411224\",\"name\":\"卢氏县\"},{\"code\":\"411281\",\"name\":\"义马市\"},{\"code\":\"411282\",\"name\":\"灵宝市\"},{\"code\":\"411300\",\"name\":\"南阳市\"},{\"code\":\"411301\",\"name\":\"市辖区\"},{\"code\":\"411302\",\"name\":\"宛城区\"},{\"code\":\"411303\",\"name\":\"卧龙区\"},{\"code\":\"411321\",\"name\":\"南召县\"},{\"code\":\"411322\",\"name\":\"方城县\"},{\"code\":\"411323\",\"name\":\"西峡县\"},{\"code\":\"411324\",\"name\":\"镇平县\"},{\"code\":\"411325\",\"name\":\"内乡县\"},{\"code\":\"411326\",\"name\":\"淅川县\"},{\"code\":\"411327\",\"name\":\"社旗县\"},{\"code\":\"411328\",\"name\":\"唐河县\"},{\"code\":\"411329\",\"name\":\"新野县\"},{\"code\":\"411330\",\"name\":\"桐柏县\"},{\"code\":\"411381\",\"name\":\"邓州市\"},{\"code\":\"411400\",\"name\":\"商丘市\"},{\"code\":\"411401\",\"name\":\"市辖区\"},{\"code\":\"411402\",\"name\":\"梁园区\"},{\"code\":\"411403\",\"name\":\"睢阳区\"},{\"code\":\"411421\",\"name\":\"民权县\"},{\"code\":\"411422\",\"name\":\"睢县\"},{\"code\":\"411423\",\"name\":\"宁陵县\"},{\"code\":\"411424\",\"name\":\"柘城县\"},{\"code\":\"411425\",\"name\":\"虞城县\"},{\"code\":\"411426\",\"name\":\"夏邑县\"},{\"code\":\"411481\",\"name\":\"永城市\"},{\"code\":\"411500\",\"name\":\"信阳市\"},{\"code\":\"411501\",\"name\":\"市辖区\"},{\"code\":\"411502\",\"name\":\"师河区\"},{\"code\":\"411503\",\"name\":\"平桥区\"},{\"code\":\"411521\",\"name\":\"罗山县\"},{\"code\":\"411522\",\"name\":\"光山县\"},{\"code\":\"411523\",\"name\":\"新县\"},{\"code\":\"411524\",\"name\":\"商城县\"},{\"code\":\"411525\",\"name\":\"固始县\"},{\"code\":\"411526\",\"name\":\"潢川县\"},{\"code\":\"411527\",\"name\":\"淮滨县\"},{\"code\":\"411528\",\"name\":\"息县\"},{\"code\":\"412700\",\"name\":\"周口地区\"},{\"code\":\"412701\",\"name\":\"周口市\"},{\"code\":\"412702\",\"name\":\"项城市\"},{\"code\":\"412721\",\"name\":\"扶沟县\"},{\"code\":\"412722\",\"name\":\"西华县\"},{\"code\":\"412723\",\"name\":\"商水县\"},{\"code\":\"412724\",\"name\":\"太康县\"},{\"code\":\"412725\",\"name\":\"鹿邑县\"},{\"code\":\"412726\",\"name\":\"郸城县\"},{\"code\":\"412727\",\"name\":\"淮阳县\"},{\"code\":\"412728\",\"name\":\"沈丘县\"},{\"code\":\"412800\",\"name\":\"驻马店地区\"},{\"code\":\"412801\",\"name\":\"驻马店市\"},{\"code\":\"412821\",\"name\":\"确山县\"},{\"code\":\"412822\",\"name\":\"泌阳县\"},{\"code\":\"412823\",\"name\":\"遂平县\"},{\"code\":\"412824\",\"name\":\"西平县\"},{\"code\":\"412825\",\"name\":\"上蔡县\"},{\"code\":\"412826\",\"name\":\"汝南县\"},{\"code\":\"412827\",\"name\":\"平舆县\"},{\"code\":\"412828\",\"name\":\"新蔡县\"},{\"code\":\"412829\",\"name\":\"正阳县\"},{\"code\":\"420000\",\"name\":\"湖北省\"},{\"code\":\"420100\",\"name\":\"武汉市\"},{\"code\":\"420101\",\"name\":\"市辖区\"},{\"code\":\"420102\",\"name\":\"江岸区\"},{\"code\":\"420103\",\"name\":\"江汉区\"},{\"code\":\"420104\",\"name\":\"乔口区\"},{\"code\":\"420105\",\"name\":\"汉阳区\"},{\"code\":\"420106\",\"name\":\"武昌区\"},{\"code\":\"420107\",\"name\":\"青山区\"},{\"code\":\"420111\",\"name\":\"洪山区\"},{\"code\":\"420112\",\"name\":\"东西湖区\"},{\"code\":\"420113\",\"name\":\"汉南区\"},{\"code\":\"420114\",\"name\":\"蔡甸区\"},{\"code\":\"420115\",\"name\":\"江夏区\"},{\"code\":\"420116\",\"name\":\"黄陂区\"},{\"code\":\"420117\",\"name\":\"新洲区\"},{\"code\":\"420200\",\"name\":\"黄石市\"},{\"code\":\"420201\",\"name\":\"市辖区\"},{\"code\":\"420202\",\"name\":\"黄石港区\"},{\"code\":\"420203\",\"name\":\"石灰窑区\"},{\"code\":\"420204\",\"name\":\"下陆区\"},{\"code\":\"420205\",\"name\":\"铁山区\"},{\"code\":\"420222\",\"name\":\"阳新县\"},{\"code\":\"420281\",\"name\":\"大冶市\"},{\"code\":\"420300\",\"name\":\"十堰市\"},{\"code\":\"420301\",\"name\":\"市辖区\"},{\"code\":\"420302\",\"name\":\"茅箭区\"},{\"code\":\"420303\",\"name\":\"张湾区\"},{\"code\":\"420321\",\"name\":\"郧县\"},{\"code\":\"420322\",\"name\":\"郧西县\"},{\"code\":\"420323\",\"name\":\"竹山县\"},{\"code\":\"420324\",\"name\":\"竹溪县\"},{\"code\":\"420325\",\"name\":\"房县\"},{\"code\":\"420381\",\"name\":\"丹江口市\"},{\"code\":\"420500\",\"name\":\"宜昌市\"},{\"code\":\"420501\",\"name\":\"市辖区\"},{\"code\":\"420502\",\"name\":\"西陵区\"},{\"code\":\"420503\",\"name\":\"伍家岗区\"},{\"code\":\"420504\",\"name\":\"点军区\"},{\"code\":\"420505\",\"name\":\"虎亭区\"},{\"code\":\"420521\",\"name\":\"宜昌县\"},{\"code\":\"420525\",\"name\":\"远安县\"},{\"code\":\"420526\",\"name\":\"兴山县\"},{\"code\":\"420527\",\"name\":\"秭归县\"},{\"code\":\"420528\",\"name\":\"长阳土家族自治县\"},{\"code\":\"420529\",\"name\":\"五峰土家族自治县\"},{\"code\":\"420581\",\"name\":\"宜都市\"},{\"code\":\"420582\",\"name\":\"当阳市\"},{\"code\":\"420583\",\"name\":\"枝江市\"},{\"code\":\"420600\",\"name\":\"襄樊市\"},{\"code\":\"420601\",\"name\":\"市辖区\"},{\"code\":\"420602\",\"name\":\"襄城区\"},{\"code\":\"420606\",\"name\":\"樊城区\"},{\"code\":\"420621\",\"name\":\"襄阳县\"},{\"code\":\"420624\",\"name\":\"南漳县\"},{\"code\":\"420625\",\"name\":\"谷城县\"},{\"code\":\"420626\",\"name\":\"保康县\"},{\"code\":\"420682\",\"name\":\"老河口市\"},{\"code\":\"420683\",\"name\":\"枣阳市\"},{\"code\":\"420684\",\"name\":\"宜城市\"},{\"code\":\"420700\",\"name\":\"鄂州市\"},{\"code\":\"420701\",\"name\":\"市辖区\"},{\"code\":\"420702\",\"name\":\"梁子湖区\"},{\"code\":\"420703\",\"name\":\"华容区\"},{\"code\":\"420704\",\"name\":\"鄂城区\"},{\"code\":\"420800\",\"name\":\"荆门市\"},{\"code\":\"420801\",\"name\":\"市辖区\"},{\"code\":\"420802\",\"name\":\"东宝区\"},{\"code\":\"420821\",\"name\":\"京山县\"},{\"code\":\"420822\",\"name\":\"沙洋县\"},{\"code\":\"420881\",\"name\":\"钟祥市\"},{\"code\":\"420900\",\"name\":\"孝感市\"},{\"code\":\"420901\",\"name\":\"市辖区\"},{\"code\":\"420902\",\"name\":\"孝南区\"},{\"code\":\"420921\",\"name\":\"孝昌县\"},{\"code\":\"420922\",\"name\":\"大悟县\"},{\"code\":\"420923\",\"name\":\"云梦县\"},{\"code\":\"420981\",\"name\":\"应城市\"},{\"code\":\"420982\",\"name\":\"安陆市\"},{\"code\":\"420983\",\"name\":\"广水市\"},{\"code\":\"420984\",\"name\":\"汉川市\"},{\"code\":\"421000\",\"name\":\"荆州市\"},{\"code\":\"421001\",\"name\":\"市辖区\"},{\"code\":\"421002\",\"name\":\"沙市区\"},{\"code\":\"421003\",\"name\":\"荆州区\"},{\"code\":\"421022\",\"name\":\"公安县\"},{\"code\":\"421023\",\"name\":\"监利县\"},{\"code\":\"421024\",\"name\":\"江陵县\"},{\"code\":\"421081\",\"name\":\"石首市\"},{\"code\":\"421083\",\"name\":\"洪湖市\"},{\"code\":\"421087\",\"name\":\"松滋市\"},{\"code\":\"421100\",\"name\":\"黄冈市\"},{\"code\":\"421101\",\"name\":\"市辖区\"},{\"code\":\"421102\",\"name\":\"黄州区\"},{\"code\":\"421121\",\"name\":\"团风县\"},{\"code\":\"421122\",\"name\":\"红安县\"},{\"code\":\"421123\",\"name\":\"罗田县\"},{\"code\":\"421124\",\"name\":\"英山县\"},{\"code\":\"421125\",\"name\":\"浠水县\"},{\"code\":\"421126\",\"name\":\"蕲春县\"},{\"code\":\"421127\",\"name\":\"黄梅县\"},{\"code\":\"421181\",\"name\":\"麻城市\"},{\"code\":\"421182\",\"name\":\"武穴市\"},{\"code\":\"421200\",\"name\":\"咸宁市\"},{\"code\":\"421201\",\"name\":\"市辖区\"},{\"code\":\"421202\",\"name\":\" 咸安区\"},{\"code\":\"421221\",\"name\":\"嘉鱼县\"},{\"code\":\"421222\",\"name\":\"通城县\"},{\"code\":\"421223\",\"name\":\"崇阳县\"},{\"code\":\"421224\",\"name\":\"通山县\"},{\"code\":\"421281\",\"name\":\"赤壁市\"},{\"code\":\"422800\",\"name\":\"恩施土家族苗族自治州\"},{\"code\":\"422801\",\"name\":\"恩施市\"},{\"code\":\"422802\",\"name\":\"利川市\"},{\"code\":\"422822\",\"name\":\"建始县\"},{\"code\":\"422823\",\"name\":\"巴东县\"},{\"code\":\"422825\",\"name\":\"宣恩县\"},{\"code\":\"422826\",\"name\":\"咸丰县\"},{\"code\":\"422827\",\"name\":\"来凤县\"},{\"code\":\"422828\",\"name\":\"鹤峰县\"},{\"code\":\"429000\",\"name\":\"省直辖行政单位\"},{\"code\":\"429001\",\"name\":\"随州市\"},{\"code\":\"429004\",\"name\":\"仙桃市\"},{\"code\":\"429005\",\"name\":\"潜江市\"},{\"code\":\"429006\",\"name\":\"天门市\"},{\"code\":\"429021\",\"name\":\"神农架林区\"},{\"code\":\"430000\",\"name\":\"湖南省\"},{\"code\":\"430100\",\"name\":\"长沙市\"},{\"code\":\"430101\",\"name\":\"市辖区\"},{\"code\":\"430102\",\"name\":\"芙蓉区\"},{\"code\":\"430103\",\"name\":\"天心区\"},{\"code\":\"430104\",\"name\":\"岳麓区\"},{\"code\":\"430105\",\"name\":\"开福区\"},{\"code\":\"430111\",\"name\":\"雨花区\"},{\"code\":\"430121\",\"name\":\"长沙县\"},{\"code\":\"430122\",\"name\":\"望城县\"},{\"code\":\"430124\",\"name\":\"宁乡县\"},{\"code\":\"430181\",\"name\":\"浏阳市\"},{\"code\":\"430200\",\"name\":\"株洲市\"},{\"code\":\"430201\",\"name\":\"市辖区\"},{\"code\":\"430202\",\"name\":\"荷塘区\"},{\"code\":\"430203\",\"name\":\"芦淞区\"},{\"code\":\"430204\",\"name\":\"石峰区\"},");
        area.append("{\"code\":\"430211\",\"name\":\"天元区\"},{\"code\":\"430221\",\"name\":\"株洲县\"},{\"code\":\"430223\",\"name\":\"攸县\"},{\"code\":\"430224\",\"name\":\"茶陵县\"},{\"code\":\"430225\",\"name\":\"炎陵县\"},{\"code\":\"430281\",\"name\":\"醴陵市\"},{\"code\":\"430300\",\"name\":\"湘潭市\"},{\"code\":\"430301\",\"name\":\"市辖区\"},{\"code\":\"430302\",\"name\":\"雨湖区\"},{\"code\":\"430304\",\"name\":\"岳塘区\"},{\"code\":\"430321\",\"name\":\"湘潭县\"},{\"code\":\"430381\",\"name\":\"湘乡市\"},{\"code\":\"430382\",\"name\":\"韶山市\"},{\"code\":\"430400\",\"name\":\"衡阳市\"},{\"code\":\"430401\",\"name\":\"市辖区\"},{\"code\":\"430402\",\"name\":\"江东区\"},{\"code\":\"430403\",\"name\":\"城南区\"},{\"code\":\"430404\",\"name\":\"城北区\"},{\"code\":\"430411\",\"name\":\"郊 区\"},{\"code\":\"430412\",\"name\":\"南岳区\"},{\"code\":\"430421\",\"name\":\"衡阳县\"},{\"code\":\"430422\",\"name\":\"衡南县\"},{\"code\":\"430423\",\"name\":\"衡山县\"},{\"code\":\"430424\",\"name\":\"衡东县\"},{\"code\":\"430426\",\"name\":\"祁东县\"},{\"code\":\"430481\",\"name\":\"耒阳市\"},{\"code\":\"430482\",\"name\":\"常宁市\"},{\"code\":\"430500\",\"name\":\"邵阳市\"},{\"code\":\"430501\",\"name\":\"市辖区\"},{\"code\":\"430502\",\"name\":\"双清区\"},{\"code\":\"430503\",\"name\":\"大祥区\"},{\"code\":\"430511\",\"name\":\"北塔区\"},{\"code\":\"430521\",\"name\":\"邵东县\"},{\"code\":\"430522\",\"name\":\"新邵县\"},{\"code\":\"430523\",\"name\":\"邵阳县\"},{\"code\":\"430524\",\"name\":\"隆回县\"},{\"code\":\"430525\",\"name\":\"洞口县\"},{\"code\":\"430527\",\"name\":\"绥宁县\"},{\"code\":\"430528\",\"name\":\"新宁县\"},{\"code\":\"430529\",\"name\":\"城步苗族自治县\"},{\"code\":\"430581\",\"name\":\"武冈市\"},{\"code\":\"430600\",\"name\":\"岳阳市\"},{\"code\":\"430601\",\"name\":\"市辖区\"},{\"code\":\"430602\",\"name\":\"岳阳楼区\"},{\"code\":\"430603\",\"name\":\"云溪区\"},{\"code\":\"430611\",\"name\":\"君山区\"},{\"code\":\"430621\",\"name\":\"岳阳县\"},{\"code\":\"430623\",\"name\":\"华容县\"},{\"code\":\"430624\",\"name\":\"湘阴县\"},{\"code\":\"430626\",\"name\":\"平江县\"},{\"code\":\"430681\",\"name\":\"汨罗市\"},{\"code\":\"430682\",\"name\":\"临湘市\"},{\"code\":\"430700\",\"name\":\"常德市\"},{\"code\":\"430701\",\"name\":\"市辖区\"},{\"code\":\"430702\",\"name\":\"武陵区\"},{\"code\":\"430703\",\"name\":\"鼎城区\"},{\"code\":\"430721\",\"name\":\"安乡县\"},{\"code\":\"430722\",\"name\":\"汉寿县\"},{\"code\":\"430723\",\"name\":\"澧县\"},{\"code\":\"430724\",\"name\":\"临澧县\"},{\"code\":\"430725\",\"name\":\"桃源县\"},{\"code\":\"430726\",\"name\":\"石门县\"},{\"code\":\"430781\",\"name\":\"津市市\"},{\"code\":\"430800\",\"name\":\"张家界市\"},{\"code\":\"430801\",\"name\":\"市辖区\"},{\"code\":\"430802\",\"name\":\"永定区\"},{\"code\":\"430811\",\"name\":\"武陵源区\"},{\"code\":\"430821\",\"name\":\"慈利县\"},{\"code\":\"430822\",\"name\":\"桑植县\"},{\"code\":\"430900\",\"name\":\"益阳市\"},{\"code\":\"430901\",\"name\":\"市辖区\"},{\"code\":\"430902\",\"name\":\"资阳区\"},{\"code\":\"430903\",\"name\":\"赫山区\"},{\"code\":\"430921\",\"name\":\"南县\"},{\"code\":\"430922\",\"name\":\"桃江县\"},{\"code\":\"430923\",\"name\":\"安化县\"},{\"code\":\"430981\",\"name\":\"沅江市\"},{\"code\":\"431000\",\"name\":\"郴州市\"},{\"code\":\"431001\",\"name\":\"市辖区\"},{\"code\":\"431002\",\"name\":\"北湖区\"},{\"code\":\"431003\",\"name\":\"苏仙区\"},{\"code\":\"431021\",\"name\":\"桂阳县\"},{\"code\":\"431022\",\"name\":\"宜章县\"},{\"code\":\"431023\",\"name\":\"永兴县\"},{\"code\":\"431024\",\"name\":\"嘉禾县\"},{\"code\":\"431025\",\"name\":\"临武县\"},{\"code\":\"431026\",\"name\":\"汝城县\"},{\"code\":\"431027\",\"name\":\"桂东县\"},{\"code\":\"431028\",\"name\":\"安仁县\"},{\"code\":\"431081\",\"name\":\"资兴市\"},{\"code\":\"431100\",\"name\":\"永州市\"},{\"code\":\"431101\",\"name\":\"市辖区\"},{\"code\":\"431102\",\"name\":\"芝山区\"},{\"code\":\"431103\",\"name\":\"冷水滩区\"},{\"code\":\"431121\",\"name\":\"祁阳县\"},{\"code\":\"431122\",\"name\":\"东安县\"},{\"code\":\"431123\",\"name\":\"双牌县\"},{\"code\":\"431124\",\"name\":\"道县\"},{\"code\":\"431125\",\"name\":\"江永县\"},{\"code\":\"431126\",\"name\":\"宁远县\"},{\"code\":\"431127\",\"name\":\"蓝山县\"},{\"code\":\"431128\",\"name\":\"新田县\"},{\"code\":\"431129\",\"name\":\"江华瑶族自治县\"},{\"code\":\"431200\",\"name\":\"怀化市\"},{\"code\":\"431201\",\"name\":\"市辖区\"},{\"code\":\"431202\",\"name\":\"鹤城区\"},{\"code\":\"431221\",\"name\":\"中方县\"},{\"code\":\"431222\",\"name\":\"沅陵县\"},{\"code\":\"431223\",\"name\":\"辰溪县\"},{\"code\":\"431224\",\"name\":\"溆浦县\"},{\"code\":\"431225\",\"name\":\"会同县\"},{\"code\":\"431226\",\"name\":\"麻阳苗族自治县\"},{\"code\":\"431227\",\"name\":\"新晃侗族自治县\"},{\"code\":\"431228\",\"name\":\"芷江侗族自治县\"},{\"code\":\"431229\",\"name\":\"靖州苗族侗族自治县\"},{\"code\":\"431230\",\"name\":\"通道侗族自治县\"},{\"code\":\"431281\",\"name\":\"洪江市\"},{\"code\":\"432500\",\"name\":\"娄底地区\"},{\"code\":\"432501\",\"name\":\"娄底市\"},{\"code\":\"432502\",\"name\":\"冷水江市\"},{\"code\":\"432503\",\"name\":\"涟源市\"},{\"code\":\"432522\",\"name\":\"双峰县\"},{\"code\":\"432524\",\"name\":\"新化县\"},{\"code\":\"433100\",\"name\":\"湘西土家族苗族自治州\"},{\"code\":\"433101\",\"name\":\"吉首市\"},{\"code\":\"433122\",\"name\":\"泸溪县\"},{\"code\":\"433123\",\"name\":\"凤凰县\"},{\"code\":\"433124\",\"name\":\"花垣县\"},{\"code\":\"433125\",\"name\":\"保靖县\"},{\"code\":\"433126\",\"name\":\"古丈县\"},{\"code\":\"433127\",\"name\":\"永顺县\"},{\"code\":\"433130\",\"name\":\"龙山县\"},{\"code\":\"440000\",\"name\":\"广东省\"},{\"code\":\"440100\",\"name\":\"广州市\"},{\"code\":\"440101\",\"name\":\"市辖区\"},{\"code\":\"440102\",\"name\":\"东山区\"},{\"code\":\"440103\",\"name\":\"荔湾区\"},{\"code\":\"440104\",\"name\":\"越秀区\"},{\"code\":\"440105\",\"name\":\"海珠区\"},{\"code\":\"440106\",\"name\":\"天河区\"},{\"code\":\"440107\",\"name\":\"芳村区\"},{\"code\":\"440111\",\"name\":\"白云区\"},{\"code\":\"440112\",\"name\":\"黄埔区\"},{\"code\":\"440181\",\"name\":\"番禺市\"},{\"code\":\"440182\",\"name\":\"花都市\"},{\"code\":\"440183\",\"name\":\"增城市\"},{\"code\":\"440184\",\"name\":\"从化市\"},{\"code\":\"440200\",\"name\":\"韶关市\"},{\"code\":\"440201\",\"name\":\"市辖区\"},{\"code\":\"440202\",\"name\":\"北江区\"},{\"code\":\"440203\",\"name\":\"武江区\"},{\"code\":\"440204\",\"name\":\"浈江区\"},{\"code\":\"440221\",\"name\":\"曲江县\"},{\"code\":\"440222\",\"name\":\"始兴县\"},");
        area.append("{\"code\":\"440224\",\"name\":\"仁化县\"},{\"code\":\"440229\",\"name\":\"翁源县\"},{\"code\":\"440232\",\"name\":\"乳源瑶族自治县\"},{\"code\":\"440233\",\"name\":\"新丰县\"},{\"code\":\"440281\",\"name\":\"乐昌市\"},{\"code\":\"440282\",\"name\":\"南雄市\"},{\"code\":\"440300\",\"name\":\"深圳市\"},{\"code\":\"440301\",\"name\":\"市辖区\"},{\"code\":\"440303\",\"name\":\"罗湖区\"},{\"code\":\"440304\",\"name\":\"福田区\"},{\"code\":\"440305\",\"name\":\"南山区\"},{\"code\":\"440306\",\"name\":\"宝安区\"},{\"code\":\"440307\",\"name\":\"龙岗区\"},{\"code\":\"440308\",\"name\":\"盐田区\"},{\"code\":\"440400\",\"name\":\"珠海市\"},{\"code\":\"440401\",\"name\":\"市辖区\"},{\"code\":\"440402\",\"name\":\"香洲区\"},{\"code\":\"440421\",\"name\":\"斗门县\"},{\"code\":\"440500\",\"name\":\"汕头市\"},{\"code\":\"440501\",\"name\":\"市辖区\"},{\"code\":\"440506\",\"name\":\"达濠区\"},{\"code\":\"440507\",\"name\":\"龙湖区\"},{\"code\":\"440508\",\"name\":\"金园区\"},{\"code\":\"440509\",\"name\":\"升平区\"},{\"code\":\"440510\",\"name\":\"河浦区\"},{\"code\":\"440523\",\"name\":\"南澳县\"},{\"code\":\"440582\",\"name\":\"潮阳市\"},{\"code\":\"440583\",\"name\":\"澄海市\"},{\"code\":\"440600\",\"name\":\"佛山市\"},{\"code\":\"440601\",\"name\":\"市辖区\"},{\"code\":\"440602\",\"name\":\"城区\"},{\"code\":\"440603\",\"name\":\"石湾区\"},{\"code\":\"440681\",\"name\":\"顺德市\"},{\"code\":\"440682\",\"name\":\"南海市\"},{\"code\":\"440683\",\"name\":\"三水市\"},{\"code\":\"440684\",\"name\":\"高明市\"},{\"code\":\"440700\",\"name\":\"江门市\"},{\"code\":\"440701\",\"name\":\"市辖区\"},{\"code\":\"440703\",\"name\":\"蓬江区\"},{\"code\":\"440704\",\"name\":\"江海区\"},{\"code\":\"440781\",\"name\":\"台山市\"},{\"code\":\"440782\",\"name\":\"新会市\"},{\"code\":\"440783\",\"name\":\"开平市\"},{\"code\":\"440784\",\"name\":\"鹤山市\"},{\"code\":\"440785\",\"name\":\"恩平市\"},{\"code\":\"440800\",\"name\":\"湛江市\"},{\"code\":\"440801\",\"name\":\"市辖区\"},{\"code\":\"440802\",\"name\":\"赤坎区\"},{\"code\":\"440803\",\"name\":\"霞山区\"},{\"code\":\"440804\",\"name\":\"坡头区\"},{\"code\":\"440811\",\"name\":\"麻章区\"},{\"code\":\"440823\",\"name\":\"遂溪县\"},{\"code\":\"440825\",\"name\":\"徐闻县\"},{\"code\":\"440881\",\"name\":\"廉江市\"},{\"code\":\"440882\",\"name\":\"雷州市\"},{\"code\":\"440883\",\"name\":\"吴川市\"},{\"code\":\"440900\",\"name\":\"茂名市\"},{\"code\":\"440901\",\"name\":\"市辖区\"},{\"code\":\"440902\",\"name\":\"茂南区\"},{\"code\":\"440923\",\"name\":\"电白县\"},{\"code\":\"440981\",\"name\":\"高州市\"},{\"code\":\"440982\",\"name\":\"化州市\"},{\"code\":\"440983\",\"name\":\"信宜市\"},{\"code\":\"441200\",\"name\":\"肇庆市\"},{\"code\":\"441201\",\"name\":\"市辖区\"},{\"code\":\"441202\",\"name\":\"端州区\"},{\"code\":\"441203\",\"name\":\"鼎湖区\"},{\"code\":\"441223\",\"name\":\"广宁县\"},{\"code\":\"441224\",\"name\":\"怀集县\"},{\"code\":\"441225\",\"name\":\"封开县\"},{\"code\":\"441226\",\"name\":\"德庆县\"},{\"code\":\"441283\",\"name\":\"高要市\"},{\"code\":\"441284\",\"name\":\"四会市\"},{\"code\":\"441300\",\"name\":\"惠州市\"},{\"code\":\"441301\",\"name\":\"市辖区\"},{\"code\":\"441302\",\"name\":\"惠城区\"},{\"code\":\"441322\",\"name\":\"博罗县\"},{\"code\":\"441323\",\"name\":\"惠东县\"},{\"code\":\"441324\",\"name\":\"龙门县\"},{\"code\":\"441381\",\"name\":\"惠阳市\"},{\"code\":\"441400\",\"name\":\"梅州市\"},{\"code\":\"441401\",\"name\":\"市辖区\"},{\"code\":\"441402\",\"name\":\"梅江区\"},{\"code\":\"441421\",\"name\":\"梅县\"},{\"code\":\"441422\",\"name\":\"大埔县\"},{\"code\":\"441423\",\"name\":\"丰顺县\"},{\"code\":\"441424\",\"name\":\"五华县\"},{\"code\":\"441426\",\"name\":\"平远县\"},{\"code\":\"441427\",\"name\":\"蕉岭县\"},{\"code\":\"441481\",\"name\":\"兴宁市\"},{\"code\":\"441500\",\"name\":\"汕尾市\"},{\"code\":\"441501\",\"name\":\"市辖区\"},{\"code\":\"441502\",\"name\":\"城区\"},{\"code\":\"441521\",\"name\":\"海丰县\"},{\"code\":\"441523\",\"name\":\"陆河县\"},{\"code\":\"441581\",\"name\":\"陆丰市\"},{\"code\":\"441600\",\"name\":\"河源市\"},{\"code\":\"441601\",\"name\":\"市辖区\"},{\"code\":\"441602\",\"name\":\"源城区\"},{\"code\":\"441621\",\"name\":\"紫金县\"},{\"code\":\"441622\",\"name\":\"龙川县\"},{\"code\":\"441623\",\"name\":\"连平县\"},{\"code\":\"441624\",\"name\":\"和平县\"},{\"code\":\"441625\",\"name\":\"东源县\"},{\"code\":\"441700\",\"name\":\"阳江市\"},{\"code\":\"441701\",\"name\":\"市辖区\"},{\"code\":\"441702\",\"name\":\"江城区\"},{\"code\":\"441721\",\"name\":\"阳西县\"},{\"code\":\"441723\",\"name\":\"阳东县\"},{\"code\":\"441781\",\"name\":\"阳春市\"},{\"code\":\"441800\",\"name\":\"清远市\"},{\"code\":\"441801\",\"name\":\"市辖区\"},{\"code\":\"441802\",\"name\":\"清城区\"},{\"code\":\"441821\",\"name\":\"佛冈县\"},{\"code\":\"441823\",\"name\":\"阳山县\"},{\"code\":\"441825\",\"name\":\"连山壮族瑶族自治县\"},{\"code\":\"441826\",\"name\":\"连南瑶族自治县\"},{\"code\":\"441827\",\"name\":\"清新县\"},{\"code\":\"441881\",\"name\":\"英德市\"},{\"code\":\"441882\",\"name\":\"连州市\"},{\"code\":\"441900\",\"name\":\"东莞市\"},{\"code\":\"442000\",\"name\":\"中山市\"},{\"code\":\"445100\",\"name\":\"潮州市\"},{\"code\":\"445101\",\"name\":\"市辖区\"},{\"code\":\"445102\",\"name\":\"湘桥区\"},{\"code\":\"445121\",\"name\":\"潮安县\"},{\"code\":\"445122\",\"name\":\"饶平县\"},{\"code\":\"445200\",\"name\":\"揭阳市\"},{\"code\":\"445201\",\"name\":\"市辖区\"},{\"code\":\"445202\",\"name\":\"榕城区\"},{\"code\":\"445221\",\"name\":\"揭东县\"},{\"code\":\"445222\",\"name\":\"揭西县\"},{\"code\":\"445224\",\"name\":\"惠来县\"},{\"code\":\"445281\",\"name\":\"普宁市\"},{\"code\":\"445300\",\"name\":\"云浮市\"},{\"code\":\"445301\",\"name\":\"市辖区\"},{\"code\":\"445302\",\"name\":\"云城区\"},{\"code\":\"445321\",\"name\":\"新兴县\"},{\"code\":\"445322\",\"name\":\"郁南县\"},{\"code\":\"445323\",\"name\":\"云安县\"},{\"code\":\"445381\",\"name\":\"罗定市\"},{\"code\":\"450000\",\"name\":\"广西壮族自治区\"},{\"code\":\"450100\",\"name\":\"南宁市\"},{\"code\":\"450101\",\"name\":\"市辖区\"},{\"code\":\"450102\",\"name\":\"兴宁区\"},{\"code\":\"450103\",\"name\":\"新城区\"},{\"code\":\"450104\",\"name\":\"城北区\"},{\"code\":\"450105\",\"name\":\"江南区\"},{\"code\":\"450106\",\"name\":\"永新区\"},{\"code\":\"450111\",\"name\":\"市郊区\"},{\"code\":\"450121\",\"name\":\"邕宁县\"},{\"code\":\"450122\",\"name\":\"武鸣县\"},{\"code\":\"450200\",\"name\":\"柳州市\"},{\"code\":\"450201\",\"name\":\"市辖区\"},{\"code\":\"450202\",\"name\":\"城中区\"},{\"code\":\"450203\",\"name\":\"鱼峰区\"},{\"code\":\"450204\",\"name\":\"柳南区\"},{\"code\":\"450205\",\"name\":\"柳北区\"},{\"code\":\"450211\",\"name\":\"市郊区\"},{\"code\":\"450221\",\"name\":\"柳江县\"},{\"code\":\"450222\",\"name\":\"柳城县\"},{\"code\":\"450300\",\"name\":\"桂林市\"},{\"code\":\"450301\",\"name\":\"市辖区\"},{\"code\":\"450302\",\"name\":\"秀峰区\"},{\"code\":\"450303\",\"name\":\"叠彩区\"},{\"code\":\"450304\",\"name\":\"象山区\"},{\"code\":\"450305\",\"name\":\"七星区\"},{\"code\":\"450311\",\"name\":\"雁山区\"},{\"code\":\"450321\",\"name\":\"阳朔县\"},{\"code\":\"450322\",\"name\":\"临桂县\"},{\"code\":\"450323\",\"name\":\"灵川县\"},{\"code\":\"450324\",\"name\":\"全州县\"},{\"code\":\"450325\",\"name\":\"兴安县\"},{\"code\":\"450326\",\"name\":\"永福县\"},{\"code\":\"450327\",\"name\":\"灌阳县\"},{\"code\":\"450328\",\"name\":\"龙胜各县自治区\"},{\"code\":\"450329\",\"name\":\"资源县\"},{\"code\":\"450330\",\"name\":\"平乐县\"},{\"code\":\"450331\",\"name\":\"荔蒲县\"},{\"code\":\"450332\",\"name\":\"恭城瑶族自治县\"},{\"code\":\"450400\",\"name\":\"梧州市\"},{\"code\":\"450401\",\"name\":\"市辖区\"},{\"code\":\"450403\",\"name\":\"万秀区\"},{\"code\":\"450404\",\"name\":\"蝶山区\"},{\"code\":\"450411\",\"name\":\"市郊区\"},{\"code\":\"450421\",\"name\":\"苍梧县\"},{\"code\":\"450422\",\"name\":\"藤县\"},{\"code\":\"450423\",\"name\":\"蒙山县\"},{\"code\":\"450481\",\"name\":\"岑溪市\"},{\"code\":\"450500\",\"name\":\"北海市\"},{\"code\":\"450501\",\"name\":\"市辖区\"},{\"code\":\"450502\",\"name\":\"海城区\"},{\"code\":\"450503\",\"name\":\"银海区\"},{\"code\":\"450512\",\"name\":\"铁山港区\"},{\"code\":\"450521\",\"name\":\"合浦县\"},{\"code\":\"450600\",\"name\":\"防城港市\"},{\"code\":\"450601\",\"name\":\"市辖区\"},{\"code\":\"450602\",\"name\":\"港口区\"},{\"code\":\"450603\",\"name\":\"防城区\"},{\"code\":\"450621\",\"name\":\"上思县\"},{\"code\":\"450681\",\"name\":\"东兴市\"},{\"code\":\"450700\",\"name\":\"钦州市\"},{\"code\":\"450701\",\"name\":\"市辖区\"},{\"code\":\"450702\",\"name\":\"钦南区\"},{\"code\":\"450703\",\"name\":\"钦北区\"},{\"code\":\"450721\",\"name\":\"浦北县\"},{\"code\":\"450722\",\"name\":\"灵山县\"},{\"code\":\"450800\",\"name\":\"贵港市\"},{\"code\":\"450801\",\"name\":\"市辖区\"},{\"code\":\"450802\",\"name\":\"港北区\"},{\"code\":\"450803\",\"name\":\"港南区\"},{\"code\":\"450821\",\"name\":\"平南县\"},{\"code\":\"450881\",\"name\":\"桂平市\"},{\"code\":\"450900\",\"name\":\"玉林市\"},{\"code\":\"450901\",\"name\":\"市辖区\"},{\"code\":\"450902\",\"name\":\"玉州区\"},{\"code\":\"450921\",\"name\":\"容县\"},{\"code\":\"450922\",\"name\":\"陆川县\"},{\"code\":\"450923\",\"name\":\"博白县\"},{\"code\":\"450924\",\"name\":\"兴业县\"},{\"code\":\"450981\",\"name\":\"北流市\"},{\"code\":\"452100\",\"name\":\"南宁地区\"},{\"code\":\"452101\",\"name\":\"凭祥市\"},{\"code\":\"452122\",\"name\":\"横县\"},{\"code\":\"452123\",\"name\":\"宾阳县\"},{\"code\":\"452124\",\"name\":\"上林县\"},{\"code\":\"452126\",\"name\":\"隆安县\"},{\"code\":\"452127\",\"name\":\"马山县\"},{\"code\":\"452128\",\"name\":\"扶绥县\"},{\"code\":\"452129\",\"name\":\"崇左县\"},{\"code\":\"452130\",\"name\":\"大新县\"},{\"code\":\"452131\",\"name\":\"天等县\"},{\"code\":\"452132\",\"name\":\"宁明县\"},{\"code\":\"452133\",\"name\":\"龙州县\"},{\"code\":\"452200\",\"name\":\"柳州地区\"},{\"code\":\"452201\",\"name\":\"合山市\"},{\"code\":\"452223\",\"name\":\"鹿寨县\"},{\"code\":\"452224\",\"name\":\"象州县\"},{\"code\":\"452225\",\"name\":\"武宣县\"},{\"code\":\"452226\",\"name\":\"来宾县\"},{\"code\":\"452227\",\"name\":\"融安县\"},{\"code\":\"452228\",\"name\":\"三江侗族自治县\"},{\"code\":\"452229\",\"name\":\"融水苗族自治县\"},{\"code\":\"452230\",\"name\":\"金秀瑶族自治县\"},{\"code\":\"452231\",\"name\":\"忻城县\"},{\"code\":\"452400\",\"name\":\"贺州地区\"},{\"code\":\"452402\",\"name\":\"贺州市\"},{\"code\":\"452424\",\"name\":\"昭平县\"},{\"code\":\"452427\",\"name\":\"钟山县\"},{\"code\":\"452428\",\"name\":\"富川瑶族自治县\"},{\"code\":\"452600\",\"name\":\"百色地区\"},{\"code\":\"452601\",\"name\":\"百色市\"},{\"code\":\"452622\",\"name\":\"田阳县\"},{\"code\":\"452623\",\"name\":\"田东县\"},{\"code\":\"452624\",\"name\":\"平果县\"},{\"code\":\"452625\",\"name\":\"德保县\"},{\"code\":\"452626\",\"name\":\"靖西县\"},{\"code\":\"452627\",\"name\":\"那坡县\"},{\"code\":\"452628\",\"name\":\"凌云县\"},{\"code\":\"452629\",\"name\":\"乐业县\"},{\"code\":\"452630\",\"name\":\"田林县\"},{\"code\":\"452631\",\"name\":\"隆林各族自治县\"},{\"code\":\"452632\",\"name\":\"西林县\"},{\"code\":\"452700\",\"name\":\"河池地区\"},{\"code\":\"452701\",\"name\":\"河池市\"},{\"code\":\"452702\",\"name\":\"宜州市\"},{\"code\":\"452723\",\"name\":\"罗城仫佬族自治县\"},{\"code\":\"452724\",\"name\":\"环江毛南族自治县\"},{\"code\":\"452725\",\"name\":\"南丹县\"},{\"code\":\"452726\",\"name\":\"天峨县\"},{\"code\":\"452727\",\"name\":\"凤山县\"},{\"code\":\"452728\",\"name\":\"东兰县\"},{\"code\":\"452729\",\"name\":\"巴马瑶族自治县\"},{\"code\":\"452730\",\"name\":\"都安瑶族自治县\"},{\"code\":\"452731\",\"name\":\"大化瑶族自治县\"},{\"code\":\"460000\",\"name\":\"海南省\"},{\"code\":\"460001\",\"name\":\"通什市\"},{\"code\":\"460002\",\"name\":\"琼海市\"},{\"code\":\"460003\",\"name\":\"儋州市\"},{\"code\":\"460004\",\"name\":\"琼山市\"},{\"code\":\"460005\",\"name\":\"文昌市\"},{\"code\":\"460006\",\"name\":\"万宁市\"},{\"code\":\"460007\",\"name\":\"东方市\"},{\"code\":\"460025\",\"name\":\"定安县\"},{\"code\":\"460026\",\"name\":\"屯昌县\"},{\"code\":\"460027\",\"name\":\"澄迈县\"},{\"code\":\"460028\",\"name\":\"临高县\"},{\"code\":\"460030\",\"name\":\"白沙黎族自治县\"},{\"code\":\"460031\",\"name\":\"昌江黎族自治县\"},{\"code\":\"460033\",\"name\":\"乐东黎族自治县\"},{\"code\":\"460034\",\"name\":\"陵水黎族自治县\"},{\"code\":\"460035\",\"name\":\"保亭黎族苗族自治县\"},{\"code\":\"460036\",\"name\":\"琼中黎族苗族自治县\"},{\"code\":\"460037\",\"name\":\"西沙群岛\"},{\"code\":\"460038\",\"name\":\"南沙群岛\"},{\"code\":\"460039\",\"name\":\"中沙群岛的岛礁及其海域\"},{\"code\":\"460100\",\"name\":\"海口市\"},{\"code\":\"460101\",\"name\":\"市辖区\"},{\"code\":\"460102\",\"name\":\"振东区\"},{\"code\":\"460103\",\"name\":\"新华区\"},{\"code\":\"460104\",\"name\":\"秀英区\"},{\"code\":\"460200\",\"name\":\"三亚市\"},{\"code\":\"460201\",\"name\":\"市辖区\"},{\"code\":\"500000\",\"name\":\"重庆市\"},{\"code\":\"500100\",\"name\":\"市辖区\"},{\"code\":\"500101\",\"name\":\"万州区\"},{\"code\":\"500102\",\"name\":\"涪陵区\"},{\"code\":\"500103\",\"name\":\"渝中区\"},{\"code\":\"500104\",\"name\":\"大渡口区\"},{\"code\":\"500105\",\"name\":\"江北区\"},{\"code\":\"500106\",\"name\":\"沙坪坝区\"},{\"code\":\"500107\",\"name\":\"九龙坡区\"},{\"code\":\"500108\",\"name\":\"南岸区\"},{\"code\":\"500109\",\"name\":\"北碚区\"},{\"code\":\"500110\",\"name\":\"万盛区\"},{\"code\":\"500111\",\"name\":\"双桥区\"},{\"code\":\"500112\",\"name\":\"渝北区\"},{\"code\":\"500113\",\"name\":\"巴南区\"},{\"code\":\"500200\",\"name\":\"县\"},{\"code\":\"500221\",\"name\":\"长寿县\"},{\"code\":\"500222\",\"name\":\"綦江县\"},{\"code\":\"500223\",\"name\":\"潼南县\"},{\"code\":\"500224\",\"name\":\"铜梁县\"},{\"code\":\"500225\",\"name\":\"大足县\"},{\"code\":\"500226\",\"name\":\"荣昌县\"},{\"code\":\"500227\",\"name\":\"璧山县\"},{\"code\":\"500228\",\"name\":\"梁平县\"},{\"code\":\"500229\",\"name\":\"城口县\"},{\"code\":\"500230\",\"name\":\"丰都县\"},{\"code\":\"500231\",\"name\":\"垫江县\"},{\"code\":\"500232\",\"name\":\"武隆县\"},{\"code\":\"500233\",\"name\":\"忠县\"},{\"code\":\"500234\",\"name\":\"开县\"},{\"code\":\"500235\",\"name\":\"云阳县\"},{\"code\":\"500236\",\"name\":\"奉节县\"},{\"code\":\"500237\",\"name\":\"巫山县\"},{\"code\":\"500238\",\"name\":\"巫溪县\"},{\"code\":\"500239\",\"name\":\"黔江土家族苗族自治县\"},{\"code\":\"500240\",\"name\":\"石柱土家族自治县\"},{\"code\":\"500241\",\"name\":\"秀山土家族苗族自治县\"},{\"code\":\"500242\",\"name\":\"酉阳土家族苗族自治县\"},{\"code\":\"500243\",\"name\":\"彭水苗族土家族自治县\"},{\"code\":\"500300\",\"name\":\"市\"},{\"code\":\"500381\",\"name\":\"江津市\"},{\"code\":\"500382\",\"name\":\"合川市\"},{\"code\":\"500383\",\"name\":\"永川市\"},{\"code\":\"500384\",\"name\":\"南川市\"},{\"code\":\"510000\",\"name\":\"四川省\"},{\"code\":\"510100\",\"name\":\"成都市\"},{\"code\":\"510101\",\"name\":\"市辖区\"},{\"code\":\"510104\",\"name\":\"锦江区\"},{\"code\":\"510105\",\"name\":\"青羊区\"},{\"code\":\"510106\",\"name\":\"金牛区\"},{\"code\":\"510107\",\"name\":\"武侯区\"},{\"code\":\"510108\",\"name\":\"成华区\"},{\"code\":\"510112\",\"name\":\"龙泉驿区\"},{\"code\":\"510113\",\"name\":\"青白江区\"},{\"code\":\"510121\",\"name\":\"金堂县\"},{\"code\":\"510122\",\"name\":\"双流县\"},{\"code\":\"510123\",\"name\":\"温江县\"},{\"code\":\"510124\",\"name\":\"郫县\"},{\"code\":\"510125\",\"name\":\"新都县\"},{\"code\":\"510129\",\"name\":\"大邑县\"},{\"code\":\"510131\",\"name\":\"蒲江县\"},{\"code\":\"510132\",\"name\":\"新津县\"},{\"code\":\"510181\",\"name\":\"都江堰市\"},{\"code\":\"510182\",\"name\":\"彭州市\"},{\"code\":\"510183\",\"name\":\"邛崃市\"},{\"code\":\"510184\",\"name\":\"崇州市\"},{\"code\":\"510300\",\"name\":\"自贡市\"},{\"code\":\"510301\",\"name\":\"市辖区\"},{\"code\":\"510302\",\"name\":\"自流井区\"},{\"code\":\"510303\",\"name\":\"贡井区\"},{\"code\":\"510304\",\"name\":\"大安区\"},{\"code\":\"510311\",\"name\":\"沿滩区\"},{\"code\":\"510321\",\"name\":\"荣县\"},{\"code\":\"510322\",\"name\":\"富顺县\"},{\"code\":\"510400\",\"name\":\"攀枝花市\"},{\"code\":\"510401\",\"name\":\"市辖区\"},{\"code\":\"510402\",\"name\":\"东区\"},{\"code\":\"510403\",\"name\":\"西区\"},{\"code\":\"510411\",\"name\":\"仁和区\"},{\"code\":\"510421\",\"name\":\"米易县\"},{\"code\":\"510422\",\"name\":\"盐边县\"},{\"code\":\"510500\",\"name\":\"泸州市\"},{\"code\":\"510501\",\"name\":\"市辖区\"},{\"code\":\"510502\",\"name\":\"江阳区\"},{\"code\":\"510503\",\"name\":\"纳溪区\"},{\"code\":\"510504\",\"name\":\"龙马潭区\"},{\"code\":\"510521\",\"name\":\"泸县\"},{\"code\":\"510522\",\"name\":\"合江县\"},{\"code\":\"510524\",\"name\":\"叙永县\"},{\"code\":\"510525\",\"name\":\"古蔺县\"},{\"code\":\"510600\",\"name\":\"德阳市\"},{\"code\":\"510601\",\"name\":\"市辖区\"},{\"code\":\"510603\",\"name\":\"旌阳区\"},{\"code\":\"510623\",\"name\":\"中江县\"},{\"code\":\"510626\",\"name\":\"罗江县\"},{\"code\":\"510681\",\"name\":\"广汉市\"},{\"code\":\"510682\",\"name\":\"什邡市\"},{\"code\":\"510683\",\"name\":\"绵竹市\"},{\"code\":\"510700\",\"name\":\"绵阳市\"},{\"code\":\"510701\",\"name\":\"市辖区\"},{\"code\":\"510703\",\"name\":\"涪城区\"},{\"code\":\"510704\",\"name\":\"游仙区\"},{\"code\":\"510722\",\"name\":\"三台县\"},{\"code\":\"510723\",\"name\":\"盐亭县\"},{\"code\":\"510724\",\"name\":\"安县\"},{\"code\":\"510725\",\"name\":\"梓潼县\"},{\"code\":\"510726\",\"name\":\"北川县\"},{\"code\":\"510727\",\"name\":\"平武县\"},{\"code\":\"510781\",\"name\":\"江油市\"},{\"code\":\"510800\",\"name\":\"广元市\"},{\"code\":\"510801\",\"name\":\"市辖区\"},{\"code\":\"510802\",\"name\":\"市中区\"},{\"code\":\"510811\",\"name\":\"元坝区\"},{\"code\":\"510812\",\"name\":\"朝天区\"},{\"code\":\"510821\",\"name\":\"旺苍县\"},{\"code\":\"510822\",\"name\":\"青川县\"},{\"code\":\"510823\",\"name\":\"剑阁县\"},{\"code\":\"510824\",\"name\":\"苍溪县\"},{\"code\":\"510900\",\"name\":\"遂宁市\"},{\"code\":\"510901\",\"name\":\"市辖区\"},{\"code\":\"510902\",\"name\":\"市中区\"},{\"code\":\"510921\",\"name\":\"蓬溪县\"},{\"code\":\"510922\",\"name\":\"射洪县\"},{\"code\":\"510923\",\"name\":\"大英县\"},{\"code\":\"511000\",\"name\":\"内江市\"},{\"code\":\"511001\",\"name\":\"市辖区\"},{\"code\":\"511002\",\"name\":\"市中区\"},{\"code\":\"511011\",\"name\":\"东兴区\"},{\"code\":\"511024\",\"name\":\"威远县\"},{\"code\":\"511025\",\"name\":\"资中县\"},{\"code\":\"511028\",\"name\":\"隆昌县\"},{\"code\":\"511100\",\"name\":\"乐山市\"},{\"code\":\"511101\",\"name\":\"市辖区\"},{\"code\":\"511102\",\"name\":\"市中区\"},{\"code\":\"511111\",\"name\":\"沙湾区\"},{\"code\":\"511112\",\"name\":\"五通桥区\"},{\"code\":\"511113\",\"name\":\"金口河区\"},{\"code\":\"511123\",\"name\":\"犍为县\"},{\"code\":\"511124\",\"name\":\"井研县\"},{\"code\":\"511126\",\"name\":\"夹江县\"},{\"code\":\"511129\",\"name\":\"沐川县\"},{\"code\":\"511132\",\"name\":\"峨边彝族自治县\"},{\"code\":\"511133\",\"name\":\"马边彝族自治县\"},{\"code\":\"511181\",\"name\":\"峨眉山市\"},{\"code\":\"511300\",\"name\":\"南充市\"},{\"code\":\"511301\",\"name\":\"市辖区\"},{\"code\":\"511302\",\"name\":\"顺庆区\"},{\"code\":\"511303\",\"name\":\"高坪区\"},{\"code\":\"511304\",\"name\":\"嘉陵区\"},{\"code\":\"511321\",\"name\":\"南部县\"},{\"code\":\"511322\",\"name\":\"营山县\"},{\"code\":\"511323\",\"name\":\"蓬安县\"},{\"code\":\"511324\",\"name\":\"仪陇县\"},{\"code\":\"511325\",\"name\":\"西充县\"},{\"code\":\"511381\",\"name\":\"阆中市\"},{\"code\":\"511500\",\"name\":\"宜宾市\"},{\"code\":\"511501\",\"name\":\"市辖区\"},{\"code\":\"511502\",\"name\":\"翠屏区\"},{\"code\":\"511521\",\"name\":\"宜宾县\"},{\"code\":\"511522\",\"name\":\"南溪县\"},{\"code\":\"511523\",\"name\":\"江安县\"},{\"code\":\"511524\",\"name\":\"长宁县\"},{\"code\":\"511525\",\"name\":\"高县\"},{\"code\":\"511526\",\"name\":\"珙县\"},{\"code\":\"511527\",\"name\":\"筠连县\"},{\"code\":\"511528\",\"name\":\"兴文县\"},{\"code\":\"511529\",\"name\":\"屏山县\"},{\"code\":\"511600\",\"name\":\"广安市\"},{\"code\":\"511601\",\"name\":\"市辖区\"},");
        area.append("{\"code\":\"511602\",\"name\":\"广安区\"},{\"code\":\"511621\",\"name\":\" 岳池县\"},{\"code\":\"511622\",\"name\":\" 武胜县\"},{\"code\":\"511623\",\"name\":\" 邻水县\"},{\"code\":\"511681\",\"name\":\" 华莹市\"},{\"code\":\"513000\",\"name\":\"达川地区\"},{\"code\":\"513001\",\"name\":\"达川市\"},{\"code\":\"513002\",\"name\":\"万源市\"},{\"code\":\"513021\",\"name\":\"达县\"},{\"code\":\"513022\",\"name\":\"宣汉县\"},{\"code\":\"513023\",\"name\":\"开江县\"},{\"code\":\"513029\",\"name\":\"大竹县\"},{\"code\":\"513030\",\"name\":\"渠县\"},{\"code\":\"513100\",\"name\":\"雅安地区\"},{\"code\":\"513101\",\"name\":\"雅安市\"},{\"code\":\"513122\",\"name\":\"名山县\"},{\"code\":\"513123\",\"name\":\"荥经县\"},{\"code\":\"513124\",\"name\":\"汉源县\"},{\"code\":\"513125\",\"name\":\"石棉县\"},{\"code\":\"513126\",\"name\":\"天全县\"},{\"code\":\"513127\",\"name\":\"芦山县\"},{\"code\":\"513128\",\"name\":\"宝兴县\"},{\"code\":\"513200\",\"name\":\"阿坝藏族羌族自治州\"},{\"code\":\"513221\",\"name\":\"汶川县\"},{\"code\":\"513222\",\"name\":\"理县\"},{\"code\":\"513223\",\"name\":\"茂县\"},{\"code\":\"513224\",\"name\":\"松潘县\"},{\"code\":\"513225\",\"name\":\"九寨沟县\"},{\"code\":\"513226\",\"name\":\"金川县\"},{\"code\":\"513227\",\"name\":\"小金县\"},{\"code\":\"513228\",\"name\":\"黑水县\"},{\"code\":\"513229\",\"name\":\"马尔康县\"},{\"code\":\"513230\",\"name\":\"壤塘县\"},{\"code\":\"513231\",\"name\":\"阿坝县\"},{\"code\":\"513232\",\"name\":\"若尔盖县\"},{\"code\":\"513233\",\"name\":\"红原县\"},{\"code\":\"513300\",\"name\":\"甘孜藏族自治州\"},{\"code\":\"513321\",\"name\":\"康定县\"},{\"code\":\"513322\",\"name\":\"泸定县\"},{\"code\":\"513323\",\"name\":\"丹巴县\"},");
        area.append("{\"code\":\"513324\",\"name\":\"九龙县\"},{\"code\":\"513325\",\"name\":\"雅江县\"},{\"code\":\"513326\",\"name\":\"道孚县\"},{\"code\":\"513327\",\"name\":\"炉霍县\"},{\"code\":\"513328\",\"name\":\"甘孜县\"},{\"code\":\"513329\",\"name\":\"新龙县\"},{\"code\":\"513330\",\"name\":\"德格县\"},{\"code\":\"513331\",\"name\":\"白玉县\"},{\"code\":\"513332\",\"name\":\"石渠县\"},{\"code\":\"513333\",\"name\":\"色达县\"},{\"code\":\"513334\",\"name\":\"理塘县\"},{\"code\":\"513335\",\"name\":\"巴塘县\"},{\"code\":\"513336\",\"name\":\"乡城县\"},{\"code\":\"513337\",\"name\":\"稻城县\"},{\"code\":\"513338\",\"name\":\"得荣县\"},{\"code\":\"513400\",\"name\":\"凉山彝族自治州\"},{\"code\":\"513401\",\"name\":\"西昌市\"},{\"code\":\"513422\",\"name\":\"木里藏族自治县\"},{\"code\":\"513423\",\"name\":\"盐源县\"},{\"code\":\"513424\",\"name\":\"德昌县\"},{\"code\":\"513425\",\"name\":\"会理县\"},{\"code\":\"513426\",\"name\":\"会东县\"},{\"code\":\"513427\",\"name\":\"宁南县\"},{\"code\":\"513428\",\"name\":\"普格县\"},{\"code\":\"513429\",\"name\":\"布拖县\"},{\"code\":\"513430\",\"name\":\"金阳县\"},{\"code\":\"513431\",\"name\":\"昭觉县\"},{\"code\":\"513432\",\"name\":\"喜德县\"},{\"code\":\"513433\",\"name\":\"冕宁县\"},{\"code\":\"513434\",\"name\":\"越西县\"},{\"code\":\"513435\",\"name\":\"甘洛县\"},{\"code\":\"513436\",\"name\":\"美姑县\"},{\"code\":\"513437\",\"name\":\"雷波县\"},{\"code\":\"513700\",\"name\":\"巴中地区\"},{\"code\":\"513701\",\"name\":\"巴中市\"},{\"code\":\"513721\",\"name\":\"通江县\"},{\"code\":\"513722\",\"name\":\"南江县\"},{\"code\":\"513723\",\"name\":\"平昌县\"},{\"code\":\"513800\",\"name\":\"眉山地区\"},{\"code\":\"513821\",\"name\":\"眉山县\"},{\"code\":\"513822\",\"name\":\"仁寿县\"},{\"code\":\"513823\",\"name\":\"彭山县\"},{\"code\":\"513824\",\"name\":\"洪雅县\"},{\"code\":\"513825\",\"name\":\"丹棱县\"},{\"code\":\"513826\",\"name\":\"青神县\"},{\"code\":\"513900\",\"name\":\"资阳地区\"},{\"code\":\"513901\",\"name\":\"资阳市\"},{\"code\":\"513902\",\"name\":\"简阳市\"},{\"code\":\"513921\",\"name\":\"安岳县\"},{\"code\":\"513922\",\"name\":\"乐至县\"},{\"code\":\"520000\",\"name\":\"贵州省\"},{\"code\":\"520100\",\"name\":\"贵阳市\"},{\"code\":\"520101\",\"name\":\"市辖区\"},{\"code\":\"520102\",\"name\":\"南明区\"},{\"code\":\"520103\",\"name\":\"云岩区\"},{\"code\":\"520111\",\"name\":\"花溪区\"},{\"code\":\"520112\",\"name\":\"乌当区\"},{\"code\":\"520113\",\"name\":\"白云区\"},{\"code\":\"520121\",\"name\":\"开阳县\"},{\"code\":\"520122\",\"name\":\"息烽县\"},{\"code\":\"520123\",\"name\":\"修文县\"},{\"code\":\"520181\",\"name\":\"清镇市\"},{\"code\":\"520200\",\"name\":\"六盘水市\"},{\"code\":\"520201\",\"name\":\"钟山区\"},{\"code\":\"520202\",\"name\":\"盘县特区\"},{\"code\":\"520203\",\"name\":\"六枝特区\"},{\"code\":\"520221\",\"name\":\"水城县\"},{\"code\":\"520300\",\"name\":\"遵义市\"},{\"code\":\"520301\",\"name\":\"市辖区\"},{\"code\":\"520302\",\"name\":\"红花岗区\"},{\"code\":\"520321\",\"name\":\"遵义县\"},{\"code\":\"520322\",\"name\":\"桐梓县\"},{\"code\":\"520323\",\"name\":\"绥阳县\"},{\"code\":\"520324\",\"name\":\"正安县\"},{\"code\":\"520325\",\"name\":\"道真仡佬族苗族自治县\"},{\"code\":\"520326\",\"name\":\"务川仡佬族苗族自治县\"},{\"code\":\"520327\",\"name\":\"凤冈县\"},{\"code\":\"520328\",\"name\":\"湄潭县\"},{\"code\":\"520329\",\"name\":\"余庆县\"},{\"code\":\"520330\",\"name\":\"习水县\"},{\"code\":\"520381\",\"name\":\"赤水市\"},{\"code\":\"520382\",\"name\":\"仁怀市\"},{\"code\":\"522200\",\"name\":\"铜仁地区\"},{\"code\":\"522201\",\"name\":\"铜仁市\"},{\"code\":\"522222\",\"name\":\"江口县\"},{\"code\":\"522223\",\"name\":\"玉屏侗族自治县\"},{\"code\":\"522224\",\"name\":\"石阡县\"},{\"code\":\"522225\",\"name\":\"思南县\"},{\"code\":\"522226\",\"name\":\"印江土家族苗族自治县\"},{\"code\":\"522227\",\"name\":\"德江县\"},{\"code\":\"522228\",\"name\":\"沿河土家族自治县\"},{\"code\":\"522229\",\"name\":\"松桃苗族自治县\"},{\"code\":\"522230\",\"name\":\"万山特区\"},{\"code\":\"522300\",\"name\":\"黔西南布依族苗族自治州\"},{\"code\":\"522301\",\"name\":\"兴义市\"},{\"code\":\"522322\",\"name\":\"兴仁县\"},{\"code\":\"522323\",\"name\":\"普安县\"},{\"code\":\"522324\",\"name\":\"晴隆县\"},{\"code\":\"522325\",\"name\":\"贞丰县\"},{\"code\":\"522326\",\"name\":\"望谟县\"},{\"code\":\"522327\",\"name\":\"册亨县\"},{\"code\":\"522328\",\"name\":\"安龙县\"},{\"code\":\"522400\",\"name\":\"毕节地区\"},{\"code\":\"522401\",\"name\":\"毕节市\"},{\"code\":\"522422\",\"name\":\"大方县\"},{\"code\":\"522423\",\"name\":\"黔西县\"},{\"code\":\"522424\",\"name\":\"金沙县\"},{\"code\":\"522425\",\"name\":\"织金县\"},{\"code\":\"522426\",\"name\":\"纳雍县\"},{\"code\":\"522427\",\"name\":\"威宁彝族回族苗族自治县\"},{\"code\":\"522428\",\"name\":\"赫章县\"},{\"code\":\"522500\",\"name\":\"安顺地区\"},{\"code\":\"522501\",\"name\":\"安顺市\"},{\"code\":\"522526\",\"name\":\"平坝县\"},{\"code\":\"522527\",\"name\":\"普定县\"},{\"code\":\"522528\",\"name\":\"关岭布依族苗族自治县\"},{\"code\":\"522529\",\"name\":\"镇宁布依族苗族自治县\"},{\"code\":\"522530\",\"name\":\"紫云苗族布依族自治县\"},{\"code\":\"522600\",\"name\":\"黔东南苗族侗族自治州\"},{\"code\":\"522601\",\"name\":\"凯里市\"},{\"code\":\"522622\",\"name\":\"黄平县\"},{\"code\":\"522623\",\"name\":\"施秉县\"},{\"code\":\"522624\",\"name\":\"三穗县\"},{\"code\":\"522625\",\"name\":\"镇远县\"},{\"code\":\"522626\",\"name\":\"岑巩县\"},{\"code\":\"522627\",\"name\":\"天柱县\"},{\"code\":\"522628\",\"name\":\"锦屏县\"},{\"code\":\"522629\",\"name\":\"剑河县\"},{\"code\":\"522630\",\"name\":\"台江县\"},{\"code\":\"522631\",\"name\":\"黎平县\"},{\"code\":\"522632\",\"name\":\"榕江县\"},{\"code\":\"522633\",\"name\":\"从江县\"},{\"code\":\"522634\",\"name\":\"雷山县\"},{\"code\":\"522635\",\"name\":\"麻江县\"},{\"code\":\"522636\",\"name\":\"丹寨县\"},{\"code\":\"522700\",\"name\":\"黔南布依族苗族自治州\"},{\"code\":\"522701\",\"name\":\"都匀市\"},{\"code\":\"522702\",\"name\":\"福泉市\"},{\"code\":\"522722\",\"name\":\"荔波县\"},{\"code\":\"522723\",\"name\":\"贵定县\"},{\"code\":\"522725\",\"name\":\"瓮安县\"},{\"code\":\"522726\",\"name\":\"独山县\"},{\"code\":\"522727\",\"name\":\"平塘县\"},{\"code\":\"522728\",\"name\":\"罗甸县\"},{\"code\":\"522729\",\"name\":\"长顺县\"},{\"code\":\"522730\",\"name\":\"龙里县\"},{\"code\":\"522731\",\"name\":\"惠水县\"},{\"code\":\"522732\",\"name\":\"三都水族自治县\"},{\"code\":\"530000\",\"name\":\"云南省\"},{\"code\":\"530100\",\"name\":\"昆明市\"},{\"code\":\"530101\",\"name\":\"市辖区\"},{\"code\":\"530102\",\"name\":\"五华区\"},{\"code\":\"530103\",\"name\":\"盘龙区\"},{\"code\":\"530111\",\"name\":\"官渡区\"},{\"code\":\"530112\",\"name\":\"西山区\"},{\"code\":\"530113\",\"name\":\"东川区\"},{\"code\":\"530121\",\"name\":\"呈贡县\"},{\"code\":\"530122\",\"name\":\"晋宁县\"},{\"code\":\"530124\",\"name\":\"富民县\"},{\"code\":\"530125\",\"name\":\"宜良县\"},{\"code\":\"530126\",\"name\":\"石林彝族自治县\"},{\"code\":\"530127\",\"name\":\"嵩明县\"},{\"code\":\"530128\",\"name\":\"禄劝彝族苗族自治县\"},{\"code\":\"530129\",\"name\":\"寻甸回族彝族自治县\"},{\"code\":\"530181\",\"name\":\"安宁市\"},{\"code\":\"530300\",\"name\":\"曲靖市\"},{\"code\":\"530301\",\"name\":\"市辖区\"},{\"code\":\"530302\",\"name\":\"麒麟区\"},{\"code\":\"530321\",\"name\":\"马龙县\"},{\"code\":\"530322\",\"name\":\"陆良县\"},{\"code\":\"530323\",\"name\":\"师宗县\"},{\"code\":\"530324\",\"name\":\"罗平县\"},{\"code\":\"530325\",\"name\":\"富源县\"},{\"code\":\"530326\",\"name\":\"会泽县\"},{\"code\":\"530328\",\"name\":\"沾益县\"},{\"code\":\"530381\",\"name\":\"宣威市\"},{\"code\":\"530400\",\"name\":\"玉溪市\"},{\"code\":\"530401\",\"name\":\"市辖区\"},{\"code\":\"530402\",\"name\":\"红塔区\"},{\"code\":\"530421\",\"name\":\"江川县\"},{\"code\":\"530422\",\"name\":\"澄江县\"},{\"code\":\"530423\",\"name\":\"通海县\"},{\"code\":\"530424\",\"name\":\"华宁县\"},{\"code\":\"530425\",\"name\":\"易门县\"},{\"code\":\"530426\",\"name\":\"峨山彝族自治县\"},{\"code\":\"530427\",\"name\":\"新平彝族傣族自治县\"},{\"code\":\"530428\",\"name\":\"元江哈尼族彝族傣族自治县\"},{\"code\":\"532100\",\"name\":\"昭通地区\"},{\"code\":\"532101\",\"name\":\"昭通市\"},{\"code\":\"532122\",\"name\":\"鲁甸县\"},{\"code\":\"532123\",\"name\":\"巧家县\"},{\"code\":\"532124\",\"name\":\"盐津县\"},{\"code\":\"532125\",\"name\":\"大关县\"},{\"code\":\"532126\",\"name\":\"永善县\"},{\"code\":\"532127\",\"name\":\"绥江县\"},{\"code\":\"532128\",\"name\":\"镇雄县\"},{\"code\":\"532129\",\"name\":\"彝良县\"},{\"code\":\"532130\",\"name\":\"威信县\"},{\"code\":\"532131\",\"name\":\"水富县\"},{\"code\":\"532300\",\"name\":\"楚雄彝族自治州\"},{\"code\":\"532301\",\"name\":\"楚雄市\"},{\"code\":\"532322\",\"name\":\"双柏县\"},{\"code\":\"532323\",\"name\":\"牟定县\"},{\"code\":\"532324\",\"name\":\"南华县\"},{\"code\":\"532325\",\"name\":\"姚安县\"},{\"code\":\"532326\",\"name\":\"大姚县\"},{\"code\":\"532327\",\"name\":\"永仁县\"},{\"code\":\"532328\",\"name\":\"元谋县\"},{\"code\":\"532329\",\"name\":\"武定县\"},{\"code\":\"532331\",\"name\":\"禄丰县\"},{\"code\":\"532500\",\"name\":\"红河哈尼族彝族自治州\"},{\"code\":\"532501\",\"name\":\"个旧市\"},{\"code\":\"532502\",\"name\":\"开远市\"},{\"code\":\"532522\",\"name\":\"蒙自县\"},{\"code\":\"532523\",\"name\":\"屏边苗族自治县\"},{\"code\":\"532524\",\"name\":\"建水县\"},{\"code\":\"532525\",\"name\":\"石屏县\"},{\"code\":\"532526\",\"name\":\"弥勒县\"},{\"code\":\"532527\",\"name\":\"泸西县\"},{\"code\":\"532528\",\"name\":\"元阳县\"},{\"code\":\"532529\",\"name\":\"红河县\"},{\"code\":\"532530\",\"name\":\"金平苗族瑶族傣族自治县\"},{\"code\":\"532531\",\"name\":\"绿春县\"},{\"code\":\"532532\",\"name\":\"河口瑶族自治县\"},{\"code\":\"532600\",\"name\":\"文山壮族苗族自治州\"},{\"code\":\"532621\",\"name\":\"文山县\"},{\"code\":\"532622\",\"name\":\"砚山县\"},{\"code\":\"532623\",\"name\":\"西畴县\"},{\"code\":\"532624\",\"name\":\"麻栗坡县\"},{\"code\":\"532625\",\"name\":\"马关县\"},{\"code\":\"532626\",\"name\":\"丘北县\"},{\"code\":\"532627\",\"name\":\"广南县\"},{\"code\":\"532628\",\"name\":\"富宁县\"},{\"code\":\"532700\",\"name\":\"思茅地区\"},{\"code\":\"532701\",\"name\":\"思茅市\"},{\"code\":\"532722\",\"name\":\"普洱哈尼族彝族自治县\"},{\"code\":\"532723\",\"name\":\"墨江哈尼族自治县\"},{\"code\":\"532724\",\"name\":\"景东彝族自治县\"},{\"code\":\"532725\",\"name\":\"景谷傣族彝族自治县\"},{\"code\":\"532726\",\"name\":\"镇沅彝族哈尼族拉祜族自治县\"},{\"code\":\"532727\",\"name\":\"江城哈尼族彝族自治县\"},{\"code\":\"532728\",\"name\":\"孟连傣族拉祜族佤族自治县\"},{\"code\":\"532729\",\"name\":\"澜沧拉祜族自治县\"},{\"code\":\"532730\",\"name\":\"西盟佤族自治县\"},{\"code\":\"532800\",\"name\":\"西双版纳傣族自治州\"},{\"code\":\"532801\",\"name\":\"景洪市\"},{\"code\":\"532822\",\"name\":\"勐海县\"},{\"code\":\"532823\",\"name\":\"勐腊县\"},{\"code\":\"532900\",\"name\":\"大理白族自治州\"},{\"code\":\"532901\",\"name\":\"大理市\"},{\"code\":\"532922\",\"name\":\"漾濞彝族自治县\"},{\"code\":\"532923\",\"name\":\"祥云县\"},{\"code\":\"532924\",\"name\":\"宾川县\"},{\"code\":\"532925\",\"name\":\"弥渡县\"},{\"code\":\"532926\",\"name\":\"南涧彝族自治县\"},{\"code\":\"532927\",\"name\":\"巍山彝族回族自治县\"},{\"code\":\"532928\",\"name\":\"永平县\"},{\"code\":\"532929\",\"name\":\"云龙县\"},{\"code\":\"532930\",\"name\":\"洱源县\"},{\"code\":\"532931\",\"name\":\"剑川县\"},{\"code\":\"532932\",\"name\":\"鹤庆县\"},{\"code\":\"533000\",\"name\":\"保山地区\"},{\"code\":\"533001\",\"name\":\"保山市\"},{\"code\":\"533022\",\"name\":\"施甸县\"},{\"code\":\"533023\",\"name\":\"腾冲县\"},{\"code\":\"533024\",\"name\":\"龙陵县\"},{\"code\":\"533025\",\"name\":\"昌宁县\"},{\"code\":\"533100\",\"name\":\"德宏傣族景颇族自治州\"},{\"code\":\"533101\",\"name\":\"畹町市\"},{\"code\":\"533102\",\"name\":\"瑞丽市\"},{\"code\":\"533103\",\"name\":\"潞西市\"},{\"code\":\"533122\",\"name\":\"梁河县\"},{\"code\":\"533123\",\"name\":\"盈江县\"},{\"code\":\"533124\",\"name\":\"陇川县\"},{\"code\":\"533200\",\"name\":\"丽江地区\"},{\"code\":\"533221\",\"name\":\"丽江纳西族自治县\"},{\"code\":\"533222\",\"name\":\"永胜县\"},{\"code\":\"533223\",\"name\":\"华坪县\"},{\"code\":\"533224\",\"name\":\"宁蒗彝族自治县\"},{\"code\":\"533300\",\"name\":\"怒江傈僳族自治州\"},{\"code\":\"533321\",\"name\":\"泸水县\"},{\"code\":\"533323\",\"name\":\"福贡县\"},{\"code\":\"533324\",\"name\":\"贡山独龙族怒族自治县\"},{\"code\":\"533325\",\"name\":\"兰坪白族普米族自治县\"},{\"code\":\"533400\",\"name\":\"迪庆藏族自治州\"},{\"code\":\"533421\",\"name\":\"中甸县\"},{\"code\":\"533422\",\"name\":\"德钦县\"},{\"code\":\"533423\",\"name\":\"维西傈僳族自治县\"},{\"code\":\"533500\",\"name\":\"临沧地区\"},{\"code\":\"533521\",\"name\":\"临沧县\"},{\"code\":\"533522\",\"name\":\"凤庆县\"},{\"code\":\"533523\",\"name\":\"云县\"},{\"code\":\"533524\",\"name\":\"永德县\"},{\"code\":\"533525\",\"name\":\"镇康县\"},{\"code\":\"533526\",\"name\":\"双江拉祜族佤族布朗族傣族自治县\"},{\"code\":\"533527\",\"name\":\"耿马傣族佤族自治县\"},{\"code\":\"533528\",\"name\":\"沧源佤族自治县\"},{\"code\":\"540000\",\"name\":\"西藏自治区\"},{\"code\":\"540100\",\"name\":\"拉萨市\"},{\"code\":\"540101\",\"name\":\"市辖区\"},{\"code\":\"540102\",\"name\":\"城关区\"},{\"code\":\"540121\",\"name\":\"林周县\"},{\"code\":\"540122\",\"name\":\"当雄县\"},{\"code\":\"540123\",\"name\":\"尼木县\"},{\"code\":\"540124\",\"name\":\"曲水县\"},{\"code\":\"540125\",\"name\":\"堆龙德庆县\"},{\"code\":\"540126\",\"name\":\"达孜县\"},{\"code\":\"540127\",\"name\":\"墨竹工卡县\"},{\"code\":\"542100\",\"name\":\"昌都地区\"},{\"code\":\"542121\",\"name\":\"昌都县\"},{\"code\":\"542122\",\"name\":\"江达县\"},{\"code\":\"542123\",\"name\":\"贡觉县\"},{\"code\":\"542124\",\"name\":\"类乌齐县\"},{\"code\":\"542125\",\"name\":\"丁青县\"},{\"code\":\"542126\",\"name\":\"察雅县\"},{\"code\":\"542127\",\"name\":\"八宿县\"},{\"code\":\"542128\",\"name\":\"左贡县\"},{\"code\":\"542129\",\"name\":\"芒康县\"},{\"code\":\"542132\",\"name\":\"洛隆县\"},{\"code\":\"542133\",\"name\":\"边坝县\"},{\"code\":\"542134\",\"name\":\"盐井县\"},{\"code\":\"542135\",\"name\":\"碧土县\"},{\"code\":\"542136\",\"name\":\"妥坝县\"},{\"code\":\"542137\",\"name\":\"生达县\"},{\"code\":\"542200\",\"name\":\"山南地区\"},{\"code\":\"542221\",\"name\":\"乃东县\"},{\"code\":\"542222\",\"name\":\"扎囊县\"},{\"code\":\"542223\",\"name\":\"贡嘎县\"},{\"code\":\"542224\",\"name\":\"桑日县\"},{\"code\":\"542225\",\"name\":\"琼结县\"},{\"code\":\"542226\",\"name\":\"曲松县\"},{\"code\":\"542227\",\"name\":\"措美县\"},{\"code\":\"542228\",\"name\":\"洛扎县\"},{\"code\":\"542229\",\"name\":\"加查县\"},{\"code\":\"542231\",\"name\":\"隆子县\"},{\"code\":\"542232\",\"name\":\"错那县\"},{\"code\":\"542233\",\"name\":\"浪卡子县\"},{\"code\":\"542300\",\"name\":\"日喀则地区\"},{\"code\":\"542301\",\"name\":\"日喀则市\"},{\"code\":\"542322\",\"name\":\"南木林县\"},{\"code\":\"542323\",\"name\":\"江孜县\"},{\"code\":\"542324\",\"name\":\"定日县\"},{\"code\":\"542325\",\"name\":\"萨迦县\"},{\"code\":\"542326\",\"name\":\"拉孜县\"},{\"code\":\"542327\",\"name\":\"昂仁县\"},{\"code\":\"542328\",\"name\":\"谢通门县\"},{\"code\":\"542329\",\"name\":\"白朗县\"},{\"code\":\"542330\",\"name\":\"仁布县\"},{\"code\":\"542331\",\"name\":\"康马县\"},{\"code\":\"542332\",\"name\":\"定结县\"},");
        area.append("{\"code\":\"542333\",\"name\":\"仲巴县\"},{\"code\":\"542334\",\"name\":\"亚东县\"},{\"code\":\"542335\",\"name\":\"吉隆县\"},{\"code\":\"542336\",\"name\":\"聂拉木县\"},{\"code\":\"542337\",\"name\":\"萨嘎县\"},{\"code\":\"542338\",\"name\":\"岗巴县\"},{\"code\":\"542400\",\"name\":\"那曲地区\"},{\"code\":\"542421\",\"name\":\"那曲县\"},{\"code\":\"542422\",\"name\":\"嘉黎县\"},{\"code\":\"542423\",\"name\":\"比如县\"},{\"code\":\"542424\",\"name\":\"聂荣县\"},{\"code\":\"542425\",\"name\":\"安多县\"},{\"code\":\"542426\",\"name\":\"申扎县\"},{\"code\":\"542427\",\"name\":\"索县\"},{\"code\":\"542428\",\"name\":\"班戈县\"},{\"code\":\"542429\",\"name\":\"巴青县\"},{\"code\":\"542430\",\"name\":\"尼玛县\"},{\"code\":\"542500\",\"name\":\"阿里地区\"},{\"code\":\"542521\",\"name\":\"普兰县\"},{\"code\":\"542522\",\"name\":\"札达县\"},{\"code\":\"542523\",\"name\":\"噶尔县\"},{\"code\":\"542524\",\"name\":\"日土县\"},{\"code\":\"542525\",\"name\":\"革吉县\"},{\"code\":\"542526\",\"name\":\"改则县\"},{\"code\":\"542527\",\"name\":\"措勤县\"},{\"code\":\"542528\",\"name\":\"隆格尔县\"},{\"code\":\"542600\",\"name\":\"林芝地区\"},{\"code\":\"542621\",\"name\":\"林芝县\"},{\"code\":\"542622\",\"name\":\"工布江达县\"},{\"code\":\"542623\",\"name\":\"米林县\"},{\"code\":\"542624\",\"name\":\"墨脱县\"},{\"code\":\"542625\",\"name\":\"波密县\"},{\"code\":\"542626\",\"name\":\"察隅县\"},{\"code\":\"542627\",\"name\":\"朗县\"},{\"code\":\"610000\",\"name\":\"陕西省\"},{\"code\":\"610100\",\"name\":\"西安市\"},{\"code\":\"610101\",\"name\":\"市辖区\"},{\"code\":\"610102\",\"name\":\"新城区\"},{\"code\":\"610103\",\"name\":\"碑林区\"},{\"code\":\"610104\",\"name\":\"莲湖区\"},{\"code\":\"610111\",\"name\":\"灞桥区\"},{\"code\":\"610112\",\"name\":\"未央区\"},{\"code\":\"610113\",\"name\":\"雁塔区\"},{\"code\":\"610114\",\"name\":\"阎良区\"},{\"code\":\"610115\",\"name\":\"临潼区\"},{\"code\":\"610121\",\"name\":\"长安县\"},{\"code\":\"610122\",\"name\":\"蓝田县\"},{\"code\":\"610124\",\"name\":\"周至县\"},{\"code\":\"610125\",\"name\":\"户县\"},{\"code\":\"610126\",\"name\":\"高陵县\"},{\"code\":\"610200\",\"name\":\"铜川市\"},{\"code\":\"610201\",\"name\":\"市辖区\"},{\"code\":\"610202\",\"name\":\"城区\"},{\"code\":\"610203\",\"name\":\"郊区\"},{\"code\":\"610221\",\"name\":\"耀县\"},{\"code\":\"610222\",\"name\":\"宜君县\"},{\"code\":\"610300\",\"name\":\"宝鸡市\"},{\"code\":\"610301\",\"name\":\"市辖区\"},{\"code\":\"610302\",\"name\":\"渭滨区\"},{\"code\":\"610303\",\"name\":\"金台区\"},{\"code\":\"610321\",\"name\":\"宝鸡县\"},{\"code\":\"610322\",\"name\":\"凤翔县\"},{\"code\":\"610323\",\"name\":\"岐山县\"},{\"code\":\"610324\",\"name\":\"扶风县\"},{\"code\":\"610326\",\"name\":\"眉县\"},{\"code\":\"610327\",\"name\":\"陇县\"},{\"code\":\"610328\",\"name\":\"千阳县\"},{\"code\":\"610329\",\"name\":\"麟游县\"},{\"code\":\"610330\",\"name\":\"凤县\"},{\"code\":\"610331\",\"name\":\"太白县\"},{\"code\":\"610400\",\"name\":\"咸阳市\"},{\"code\":\"610401\",\"name\":\"市辖区\"},{\"code\":\"610402\",\"name\":\"秦都区\"},{\"code\":\"610403\",\"name\":\"杨陵区\"},{\"code\":\"610404\",\"name\":\"渭城区\"},{\"code\":\"610422\",\"name\":\"三原县\"},{\"code\":\"610423\",\"name\":\"泾阳县\"},{\"code\":\"610424\",\"name\":\"乾县\"},{\"code\":\"610425\",\"name\":\"礼泉县\"},{\"code\":\"610426\",\"name\":\"永寿县\"},{\"code\":\"610427\",\"name\":\"彬县\"},{\"code\":\"610428\",\"name\":\"长武县\"},{\"code\":\"610429\",\"name\":\"旬邑县\"},{\"code\":\"610430\",\"name\":\"淳化县\"},{\"code\":\"610431\",\"name\":\"武功县\"},{\"code\":\"610481\",\"name\":\"兴平市\"},{\"code\":\"610500\",\"name\":\"渭南市\"},{\"code\":\"610501\",\"name\":\"市辖区\"},{\"code\":\"610502\",\"name\":\"临渭区\"},{\"code\":\"610521\",\"name\":\"华县\"},{\"code\":\"610522\",\"name\":\"潼关县\"},{\"code\":\"610523\",\"name\":\"大荔县\"},{\"code\":\"610524\",\"name\":\"合阳县\"},{\"code\":\"610525\",\"name\":\"澄城县\"},{\"code\":\"610526\",\"name\":\"蒲城县\"},{\"code\":\"610527\",\"name\":\"白水县\"},{\"code\":\"610528\",\"name\":\"富平县\"},{\"code\":\"610581\",\"name\":\"韩城市\"},{\"code\":\"610582\",\"name\":\"华阴市\"},{\"code\":\"610600\",\"name\":\"延安市\"},{\"code\":\"610601\",\"name\":\"市辖区\"},{\"code\":\"610602\",\"name\":\"宝塔区\"},{\"code\":\"610621\",\"name\":\"延长县\"},{\"code\":\"610622\",\"name\":\"延川县\"},{\"code\":\"610623\",\"name\":\"子长县\"},{\"code\":\"610624\",\"name\":\"安塞县\"},{\"code\":\"610625\",\"name\":\"志丹县\"},{\"code\":\"610626\",\"name\":\"吴旗县\"},{\"code\":\"610627\",\"name\":\"甘泉县\"},{\"code\":\"610628\",\"name\":\"富县\"},{\"code\":\"610629\",\"name\":\"洛川县\"},{\"code\":\"610630\",\"name\":\"宜川县\"},{\"code\":\"610631\",\"name\":\"黄龙县\"},{\"code\":\"610632\",\"name\":\"黄陵县\"},{\"code\":\"610700\",\"name\":\"汉中市\"},{\"code\":\"610701\",\"name\":\"市辖区\"},{\"code\":\"610702\",\"name\":\"汉台区\"},{\"code\":\"610721\",\"name\":\"南郑县\"},{\"code\":\"610722\",\"name\":\"城固县\"},{\"code\":\"610723\",\"name\":\"洋县\"},{\"code\":\"610724\",\"name\":\"西乡县\"},{\"code\":\"610725\",\"name\":\"勉县\"},{\"code\":\"610726\",\"name\":\"宁强县\"},{\"code\":\"610727\",\"name\":\"略阳县\"},{\"code\":\"610728\",\"name\":\"镇巴县\"},{\"code\":\"610729\",\"name\":\"留坝县\"},{\"code\":\"610730\",\"name\":\"佛坪县\"},{\"code\":\"612400\",\"name\":\"安康地区\"},{\"code\":\"612401\",\"name\":\"安康市\"},{\"code\":\"612422\",\"name\":\"汉阴县\"},{\"code\":\"612423\",\"name\":\"石泉县\"},{\"code\":\"612424\",\"name\":\"宁陕县\"},{\"code\":\"612425\",\"name\":\"紫阳县\"},{\"code\":\"612426\",\"name\":\"岚皋县\"},{\"code\":\"612427\",\"name\":\"平利县\"},{\"code\":\"612428\",\"name\":\"镇坪县\"},{\"code\":\"612429\",\"name\":\"旬阳县\"},{\"code\":\"612430\",\"name\":\"白河县\"},{\"code\":\"612500\",\"name\":\"商洛地区\"},{\"code\":\"612501\",\"name\":\"商州市\"},{\"code\":\"612522\",\"name\":\"洛南县\"},{\"code\":\"612523\",\"name\":\"丹凤县\"},{\"code\":\"612524\",\"name\":\"商南县\"},{\"code\":\"612525\",\"name\":\"山阳县\"},{\"code\":\"612526\",\"name\":\"镇安县\"},{\"code\":\"612527\",\"name\":\"柞水县\"},{\"code\":\"612700\",\"name\":\"榆林地区\"},{\"code\":\"612701\",\"name\":\"榆林市\"},{\"code\":\"612722\",\"name\":\"神木县\"},{\"code\":\"612723\",\"name\":\"府谷县\"},{\"code\":\"612724\",\"name\":\"横山县\"},{\"code\":\"612725\",\"name\":\"靖边县\"},{\"code\":\"612726\",\"name\":\"定边县\"},{\"code\":\"612727\",\"name\":\"绥德县\"},{\"code\":\"612728\",\"name\":\"米脂县\"},{\"code\":\"612729\",\"name\":\"佳县\"},{\"code\":\"612730\",\"name\":\"吴堡县\"},{\"code\":\"612731\",\"name\":\"清涧县\"},{\"code\":\"612732\",\"name\":\"子洲县\"},{\"code\":\"620000\",\"name\":\"甘肃省\"},{\"code\":\"620100\",\"name\":\"兰州市\"},{\"code\":\"620101\",\"name\":\"市辖区\"},{\"code\":\"620102\",\"name\":\"城关区\"},{\"code\":\"620103\",\"name\":\"七里河区\"},{\"code\":\"620104\",\"name\":\"西固区\"},{\"code\":\"620105\",\"name\":\"安宁区\"},{\"code\":\"620111\",\"name\":\"红古区\"},{\"code\":\"620121\",\"name\":\"永登县\"},{\"code\":\"620122\",\"name\":\"皋兰县\"},{\"code\":\"620123\",\"name\":\"榆中县\"},{\"code\":\"620200\",\"name\":\"嘉峪关市\"},{\"code\":\"620201\",\"name\":\"市辖区\"},{\"code\":\"620300\",\"name\":\"金昌市\"},{\"code\":\"620301\",\"name\":\"市辖区\"},{\"code\":\"620302\",\"name\":\"金川区\"},{\"code\":\"620321\",\"name\":\"永昌县\"},{\"code\":\"620400\",\"name\":\"白银市\"},{\"code\":\"620401\",\"name\":\"市辖区\"},{\"code\":\"620402\",\"name\":\"白银区\"},{\"code\":\"620403\",\"name\":\"平川区\"},{\"code\":\"620421\",\"name\":\"靖远县\"},{\"code\":\"620422\",\"name\":\"会宁县\"},{\"code\":\"620423\",\"name\":\"景泰县\"},{\"code\":\"620500\",\"name\":\"天水市\"},{\"code\":\"620501\",\"name\":\"市辖区\"},{\"code\":\"620502\",\"name\":\"秦城区\"},{\"code\":\"620503\",\"name\":\"北道区\"},{\"code\":\"620521\",\"name\":\"清水县\"},{\"code\":\"620522\",\"name\":\"秦安县\"},{\"code\":\"620523\",\"name\":\"甘谷县\"},{\"code\":\"620524\",\"name\":\"武山县\"},{\"code\":\"620525\",\"name\":\"张家川回族自治县\"},{\"code\":\"622100\",\"name\":\"酒泉地区\"},{\"code\":\"622101\",\"name\":\"玉门市\"},{\"code\":\"622102\",\"name\":\"酒泉市\"},{\"code\":\"622103\",\"name\":\"敦煌市\"},{\"code\":\"622123\",\"name\":\"金塔县\"},{\"code\":\"622124\",\"name\":\"肃北蒙古族自治县\"},{\"code\":\"622125\",\"name\":\"阿克塞哈萨克族自治县\"},{\"code\":\"622126\",\"name\":\"安西县\"},{\"code\":\"622200\",\"name\":\"张掖地区\"},{\"code\":\"622201\",\"name\":\"张掖市\"},{\"code\":\"622222\",\"name\":\"肃南裕固族自治县\"},{\"code\":\"622223\",\"name\":\"民乐县\"},{\"code\":\"622224\",\"name\":\"临泽县\"},{\"code\":\"622225\",\"name\":\"高台县\"},{\"code\":\"622226\",\"name\":\"山丹县\"},{\"code\":\"622300\",\"name\":\"武威地区\"},{\"code\":\"622301\",\"name\":\"武威市\"},{\"code\":\"622322\",\"name\":\"民勤县\"},{\"code\":\"622323\",\"name\":\"古浪县\"},{\"code\":\"622326\",\"name\":\"天祝藏族自治县\"},{\"code\":\"622400\",\"name\":\"定西地区\"},{\"code\":\"622421\",\"name\":\"定西县\"},{\"code\":\"622424\",\"name\":\"通渭县\"},{\"code\":\"622425\",\"name\":\"陇西县\"},{\"code\":\"622426\",\"name\":\"渭源县\"},{\"code\":\"622427\",\"name\":\"临洮县\"},{\"code\":\"622428\",\"name\":\"漳县\"},{\"code\":\"622429\",\"name\":\"岷县\"},{\"code\":\"622600\",\"name\":\"陇南地区\"},{\"code\":\"622621\",\"name\":\"武都县\"},{\"code\":\"622623\",\"name\":\"宕昌县\"},{\"code\":\"622624\",\"name\":\"成县\"},{\"code\":\"622625\",\"name\":\"康县\"},{\"code\":\"622626\",\"name\":\"文县\"},{\"code\":\"622627\",\"name\":\"西和县\"},{\"code\":\"622628\",\"name\":\"礼县\"},{\"code\":\"622629\",\"name\":\"两当县\"},{\"code\":\"622630\",\"name\":\"徽县\"},{\"code\":\"622700\",\"name\":\"平凉地区\"},{\"code\":\"622701\",\"name\":\"平凉市\"},{\"code\":\"622722\",\"name\":\"泾川县\"},{\"code\":\"622723\",\"name\":\"灵台县\"},{\"code\":\"622724\",\"name\":\"崇信县\"},{\"code\":\"622725\",\"name\":\"华亭县\"},{\"code\":\"622726\",\"name\":\"庄浪县\"},{\"code\":\"622727\",\"name\":\"静宁县\"},{\"code\":\"622800\",\"name\":\"庆阳地区\"},{\"code\":\"622801\",\"name\":\"西峰市\"},{\"code\":\"622821\",\"name\":\"庆阳县\"},{\"code\":\"622822\",\"name\":\"环县\"},{\"code\":\"622823\",\"name\":\"华池县\"},{\"code\":\"622824\",\"name\":\"合水县\"},{\"code\":\"622825\",\"name\":\"正宁县\"},{\"code\":\"622826\",\"name\":\"宁县\"},{\"code\":\"622827\",\"name\":\"镇原县\"},{\"code\":\"622900\",\"name\":\"临夏回族自治州\"},{\"code\":\"622901\",\"name\":\"临夏市\"},{\"code\":\"622921\",\"name\":\"临夏县\"},{\"code\":\"622922\",\"name\":\"康乐县\"},{\"code\":\"622923\",\"name\":\"永靖县\"},{\"code\":\"622924\",\"name\":\"广河县\"},{\"code\":\"622925\",\"name\":\"和政县\"},{\"code\":\"622926\",\"name\":\"东乡族自治县\"},{\"code\":\"622927\",\"name\":\"积石山保安族东乡族撒拉族自治县\"},{\"code\":\"623000\",\"name\":\"甘南藏族自治州\"},{\"code\":\"623001\",\"name\":\"合作市\"},{\"code\":\"623021\",\"name\":\"临潭县\"},{\"code\":\"623022\",\"name\":\"卓尼县\"},{\"code\":\"623023\",\"name\":\"舟曲县\"},{\"code\":\"623024\",\"name\":\"迭部县\"},{\"code\":\"623025\",\"name\":\"玛曲县\"},{\"code\":\"623026\",\"name\":\"碌曲县\"},{\"code\":\"623027\",\"name\":\"夏河县\"},{\"code\":\"630000\",\"name\":\"青海省\"},{\"code\":\"630100\",\"name\":\"西宁市\"},{\"code\":\"630101\",\"name\":\"市辖区\"},{\"code\":\"630102\",\"name\":\"城东区\"},{\"code\":\"630103\",\"name\":\"城中区\"},{\"code\":\"630104\",\"name\":\"城西区\"},{\"code\":\"630105\",\"name\":\"城北区\"},{\"code\":\"630121\",\"name\":\"大通回族土族自治县\"},{\"code\":\"632100\",\"name\":\"海东地区\"},{\"code\":\"632121\",\"name\":\"平安县\"},{\"code\":\"632122\",\"name\":\"民和回族土族自治县\"},{\"code\":\"632123\",\"name\":\"乐都县\"},{\"code\":\"632124\",\"name\":\"湟中县\"},{\"code\":\"632125\",\"name\":\"湟源县\"},{\"code\":\"632126\",\"name\":\"互助土族自治县\"},{\"code\":\"632127\",\"name\":\"化隆回族自治县\"},{\"code\":\"632128\",\"name\":\"循化撒拉族自治县\"},{\"code\":\"632200\",\"name\":\"海北藏族自治州\"},{\"code\":\"632221\",\"name\":\"门源回族自治县\"},{\"code\":\"632222\",\"name\":\"祁连县\"},{\"code\":\"632223\",\"name\":\"海晏县\"},{\"code\":\"632224\",\"name\":\"刚察县\"},{\"code\":\"632300\",\"name\":\"黄南藏族自治州\"},{\"code\":\"632321\",\"name\":\"同仁县\"},{\"code\":\"632322\",\"name\":\"尖扎县\"},{\"code\":\"632323\",\"name\":\"泽库县\"},{\"code\":\"632324\",\"name\":\"河南蒙古族自治县\"},{\"code\":\"632500\",\"name\":\"海南藏族自治州\"},{\"code\":\"632521\",\"name\":\"共和县\"},{\"code\":\"632522\",\"name\":\"同德县\"},{\"code\":\"632523\",\"name\":\"贵德县\"},{\"code\":\"632524\",\"name\":\"兴海县\"},{\"code\":\"632525\",\"name\":\"贵南县\"},{\"code\":\"632600\",\"name\":\"果洛藏族自治州\"},{\"code\":\"632621\",\"name\":\"玛沁县\"},{\"code\":\"632622\",\"name\":\"班玛县\"},{\"code\":\"632623\",\"name\":\"甘德县\"},{\"code\":\"632624\",\"name\":\"达日县\"},{\"code\":\"632625\",\"name\":\"久治县\"},{\"code\":\"632626\",\"name\":\"玛多县\"},{\"code\":\"632700\",\"name\":\"玉树藏族自治州\"},{\"code\":\"632721\",\"name\":\"玉树县\"},{\"code\":\"632722\",\"name\":\"杂多县\"},{\"code\":\"632723\",\"name\":\"称多县\"},{\"code\":\"632724\",\"name\":\"治多县\"},{\"code\":\"632725\",\"name\":\"囊谦县\"},{\"code\":\"632726\",\"name\":\"曲麻莱县\"},{\"code\":\"632800\",\"name\":\"海西蒙古族藏族自治州\"},{\"code\":\"632801\",\"name\":\"格尔木市\"},{\"code\":\"632802\",\"name\":\"德令哈市\"},{\"code\":\"632821\",\"name\":\"乌兰县\"},{\"code\":\"632822\",\"name\":\"都兰县\"},{\"code\":\"632823\",\"name\":\"天峻县\"},{\"code\":\"640000\",\"name\":\"宁夏回族自治区\"},{\"code\":\"640100\",\"name\":\"银川市\"},{\"code\":\"640101\",\"name\":\"市辖区\"},{\"code\":\"640102\",\"name\":\"城区\"},{\"code\":\"640103\",\"name\":\"新城区\"},{\"code\":\"640111\",\"name\":\"郊区\"},{\"code\":\"640121\",\"name\":\"永宁县\"},{\"code\":\"640122\",\"name\":\"贺兰县\"},{\"code\":\"640200\",\"name\":\"石嘴山市\"},{\"code\":\"640201\",\"name\":\"市辖区\"},{\"code\":\"640202\",\"name\":\"大武口区\"},{\"code\":\"640203\",\"name\":\"石嘴山区\"},{\"code\":\"640204\",\"name\":\"石炭井区\"},{\"code\":\"640221\",\"name\":\"平罗县\"},{\"code\":\"640222\",\"name\":\"陶乐县\"},{\"code\":\"640223\",\"name\":\"惠农县\"},{\"code\":\"640300\",\"name\":\"吴忠市\"},{\"code\":\"640301\",\"name\":\"市辖区\"},{\"code\":\"640302\",\"name\":\"利通区\"},{\"code\":\"640321\",\"name\":\"中卫县\"},{\"code\":\"640322\",\"name\":\"中宁县\"},{\"code\":\"640323\",\"name\":\"盐池县\"},{\"code\":\"640324\",\"name\":\"同心县\"},{\"code\":\"640381\",\"name\":\"青铜峡市\"},{\"code\":\"640382\",\"name\":\"灵武市\"},{\"code\":\"642200\",\"name\":\"固原地区\"},{\"code\":\"642221\",\"name\":\"固原县\"},{\"code\":\"642222\",\"name\":\"海原县\"},{\"code\":\"642223\",\"name\":\"西吉县\"},{\"code\":\"642224\",\"name\":\"隆德县\"},{\"code\":\"642225\",\"name\":\"泾源县\"},{\"code\":\"642226\",\"name\":\"彭阳县\"},{\"code\":\"650000\",\"name\":\"新疆维吾尔自治区\"},{\"code\":\"650100\",\"name\":\"乌鲁木齐市\"},{\"code\":\"650101\",\"name\":\"市辖区\"},{\"code\":\"650102\",\"name\":\"天山区\"},{\"code\":\"650103\",\"name\":\"沙依巴克区\"},{\"code\":\"650104\",\"name\":\"新市区\"},{\"code\":\"650105\",\"name\":\"水磨沟区\"},{\"code\":\"650106\",\"name\":\"头屯河区\"},{\"code\":\"650107\",\"name\":\"南山矿区\"},{\"code\":\"650108\",\"name\":\"东山区\"},{\"code\":\"650121\",\"name\":\"乌鲁木齐县\"},{\"code\":\"650200\",\"name\":\"克拉玛依市\"},{\"code\":\"650201\",\"name\":\"市辖区\"},{\"code\":\"650202\",\"name\":\"独山子区\"},{\"code\":\"650203\",\"name\":\"克拉玛依区\"},{\"code\":\"650204\",\"name\":\"白碱滩区\"},{\"code\":\"650205\",\"name\":\"乌尔禾区\"},{\"code\":\"652100\",\"name\":\"吐鲁番地区\"},{\"code\":\"652101\",\"name\":\"吐鲁番市\"},{\"code\":\"652122\",\"name\":\"鄯善县\"},{\"code\":\"652123\",\"name\":\"托克逊县\"},{\"code\":\"652200\",\"name\":\"哈密地区\"},{\"code\":\"652201\",\"name\":\"哈密市\"},{\"code\":\"652222\",\"name\":\"巴里坤哈萨克自治县\"},{\"code\":\"652223\",\"name\":\"伊吾县\"},{\"code\":\"652300\",\"name\":\"昌吉回族自治州\"},{\"code\":\"652301\",\"name\":\"昌吉市\"},{\"code\":\"652302\",\"name\":\"阜康市\"},{\"code\":\"652303\",\"name\":\"米泉市\"},{\"code\":\"652323\",\"name\":\"呼图壁县\"},{\"code\":\"652324\",\"name\":\"玛纳斯县\"},{\"code\":\"652325\",\"name\":\"奇台县\"},{\"code\":\"652327\",\"name\":\"吉木萨尔县\"},{\"code\":\"652328\",\"name\":\"木垒哈萨克自治县\"},{\"code\":\"652700\",\"name\":\"博尔塔拉蒙古自治州\"},{\"code\":\"652701\",\"name\":\"博乐市\"},{\"code\":\"652722\",\"name\":\"精河县\"},{\"code\":\"652723\",\"name\":\"温泉县\"},{\"code\":\"652800\",\"name\":\"巴音郭楞蒙古自治州\"},{\"code\":\"652801\",\"name\":\"库尔勒市\"},{\"code\":\"652822\",\"name\":\"轮台县\"},{\"code\":\"652823\",\"name\":\"尉犁县\"},{\"code\":\"652824\",\"name\":\"若羌县\"},{\"code\":\"652825\",\"name\":\"且末县\"},{\"code\":\"652826\",\"name\":\"焉耆回族自治县\"},{\"code\":\"652827\",\"name\":\"和静县\"},{\"code\":\"652828\",\"name\":\"和硕县\"},{\"code\":\"652829\",\"name\":\"博湖县\"},{\"code\":\"652900\",\"name\":\"阿克苏地区\"},{\"code\":\"652901\",\"name\":\"阿克苏市\"},{\"code\":\"652922\",\"name\":\"温宿县\"},{\"code\":\"652923\",\"name\":\"库车县\"},{\"code\":\"652924\",\"name\":\"沙雅县\"},{\"code\":\"652925\",\"name\":\"新和县\"},{\"code\":\"652926\",\"name\":\"拜城县\"},{\"code\":\"652927\",\"name\":\"乌什县\"},{\"code\":\"652928\",\"name\":\"阿瓦提县\"},{\"code\":\"652929\",\"name\":\"柯坪县\"},{\"code\":\"653000\",\"name\":\"克孜勒苏柯尔克孜自治州\"},{\"code\":\"653001\",\"name\":\"阿图什市\"},{\"code\":\"653022\",\"name\":\"阿克陶县\"},{\"code\":\"653023\",\"name\":\"阿合奇县\"},{\"code\":\"653024\",\"name\":\"乌恰县\"},{\"code\":\"653100\",\"name\":\"喀什地区\"},{\"code\":\"653101\",\"name\":\"喀什市\"},{\"code\":\"653121\",\"name\":\"疏附县\"},{\"code\":\"653122\",\"name\":\"疏勒县\"},{\"code\":\"653123\",\"name\":\"英吉沙县\"},{\"code\":\"653124\",\"name\":\"泽普县\"},{\"code\":\"653125\",\"name\":\"莎车县\"},{\"code\":\"653126\",\"name\":\"叶城县\"},{\"code\":\"653127\",\"name\":\"麦盖提县\"},{\"code\":\"653128\",\"name\":\"岳普湖县\"},{\"code\":\"653129\",\"name\":\"伽师县\"},{\"code\":\"653130\",\"name\":\"巴楚县\"},{\"code\":\"653131\",\"name\":\"塔什库尔干塔吉克自治县\"},{\"code\":\"653200\",\"name\":\"和田地区\"},{\"code\":\"653201\",\"name\":\"和田市\"},{\"code\":\"653221\",\"name\":\"和田县\"},{\"code\":\"653222\",\"name\":\"墨玉县\"},{\"code\":\"653223\",\"name\":\"皮山县\"},{\"code\":\"653224\",\"name\":\"洛浦县\"},{\"code\":\"653225\",\"name\":\"策勒县\"},{\"code\":\"653226\",\"name\":\"于田县\"},{\"code\":\"653227\",\"name\":\"民丰县\"},{\"code\":\"654000\",\"name\":\"伊犁哈萨克自治州\"},{\"code\":\"654001\",\"name\":\"奎屯市\"},{\"code\":\"654100\",\"name\":\"伊犁地区\"},{\"code\":\"654101\",\"name\":\"伊宁市\"},{\"code\":\"654121\",\"name\":\"伊宁县\"},{\"code\":\"654122\",\"name\":\"察布查尔锡伯自治县\"},{\"code\":\"654123\",\"name\":\"霍城县\"},{\"code\":\"654124\",\"name\":\"巩留县\"},{\"code\":\"654125\",\"name\":\"新源县\"},{\"code\":\"654126\",\"name\":\"昭苏县\"},{\"code\":\"654127\",\"name\":\"特克斯县\"},{\"code\":\"654128\",\"name\":\"尼勒克县\"},{\"code\":\"654200\",\"name\":\"塔城地区\"},{\"code\":\"654201\",\"name\":\"塔城市\"},{\"code\":\"654202\",\"name\":\"乌苏市\"},{\"code\":\"654221\",\"name\":\"额敏县\"},{\"code\":\"654223\",\"name\":\"沙湾县\"},{\"code\":\"654224\",\"name\":\"托里县\"},{\"code\":\"654225\",\"name\":\"裕民县\"},{\"code\":\"654226\",\"name\":\"和布克赛尔蒙古自治县\"},{\"code\":\"654300\",\"name\":\"阿勒泰地区\"},{\"code\":\"654301\",\"name\":\"阿勒泰市\"},{\"code\":\"654321\",\"name\":\"布尔津县\"},{\"code\":\"654322\",\"name\":\"富蕴县\"},{\"code\":\"654323\",\"name\":\"福海县\"},{\"code\":\"654324\",\"name\":\"哈巴河县\"},{\"code\":\"654325\",\"name\":\"青河县\"},{\"code\":\"654326\",\"name\":\"吉木乃县\"},{\"code\":\"659000\",\"name\":\"省直辖行政单位\"},{\"code\":\"659001\",\"name\":\"石河子市\"},{\"code\":\"710000\",\"name\":\"台湾省\"},{\"code\":\"810000\",\"name\":\"香港特别行政区\"}]}");
        return area.toString();
    }
    /**
     * 从String[] 数组中随机取出其中一个String字符串
     *
     * @return
     */
    public String randomOne(String list[]) {
        return list[new Random().nextInt(list.length - 1)];
    }

    /**
     * 生成末尾验证码
     * @param notIdNo 前17位号码
     * @return
     */
    public String generateCheck(String notIdNo) {
        String check = "";
        // 将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7－9－10－5－8－4－2－1－6－3－7－9－10－5－8－4－2。
        // 将这17位数字和系数相乘的结果相加
        // 用加出来和除以11，取余数
        // 余数只可能有0－1－2－3－4－5－6－7－8－9－10这11个数字。其分别对应的最后一位身份证的号码为1－0－X －9－8－7－6－5－4－3－2
        Integer ratio[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String checks[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        int sum = 0;
        for(int i=0; i<notIdNo.length();i++) {
            char s = notIdNo.charAt(i);
            int si = Integer.parseInt(s+"");
            sum += si * ratio[i];
        }
        check = checks[sum % 11];
        return check;
    }
    /**
     * 随机生成两位数的字符串（01-max）,不足两位的前面补0
     *
     * @param max
     * @return
     */
    public String randomCityCode(int max) {
        int i = new Random().nextInt(max) + 1;
        return i > 9 ? i + "" : "0" + i;
    }
    /**
     * 随机生成minAge到maxAge年龄段的人的生日日期
     *
     * @param minAge
     * @param maxAge
     * @return
     */
    public String randomBirth(int minAge, int maxAge) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());// 设置当前日期
        // 随机设置日期为前maxAge年到前minAge年的任意一天
        int randomDay = 365 * minAge + new Random().nextInt(365 * (maxAge - minAge));
        date.set(Calendar.DATE, date.get(Calendar.DATE) - randomDay);
        return dft.format(date.getTime());
    }
    // 18位身份证号码各位的含义:
    // 1-2位省、自治区、直辖市代码；
    // 3-4位地级市、盟、自治州代码；
    // 5-6位县、县级市、区代码；
    // 7-14位出生年月日，比如19670401代表1967年4月1日；
    // 15-17位为顺序号，其中17位（倒数第二位）男为单数，女为双数；
    // 18位为校验码，0-9和X。
    // 作为尾号的校验码，是由把前十七位数字带入统一的公式计算出来的，
    // 计算的结果是0-10，如果某人的尾号是0－9，都不会出现X，但如果尾号是10，那么就得用X来代替，
    // 因为如果用10做尾号，那么此人的身份证就变成了19位。X是罗马数字的10，用X来代替10
    public static void main(String[] args) {
        String randomID = IdcardUtils.INSTANCE.getRandomIdNo();
        System.out.println(randomID);
    }
}