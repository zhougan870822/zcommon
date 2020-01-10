package com.zhoug.common.utils;

import android.telephony.PhoneNumberUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 字符串工具包
 */
public class StringUtils {
    /**
     * 全角空格，长度等于一个中文字符
     */
    public static final String BLANK_SPACE_2 = "&#8195;";
    /**
     * 全角空格，长度等于半个中文字符
     */
    public static final String BLANK_SPACE_1 = "&#8194;";
    /**
     * 不会被合并的空格，长度与常规空格相同
     */
    public static final String BLANK_SPACE = "&#160;";
    /**
     * email的正则表达式
     */
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");


    /**
     * 断给定字符串是否空白串
     * null、"null"、" "
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()) || "null".equals(str))
            return true;
        return false;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {

        return !isEmpty(email) && emailer.matcher(email).matches();
    }

    /**
     * 返回字符串值 null返回空""
     *
     * @param str
     * @return
     */
    public static String getText(String str) {
        return isEmpty(str) ? "" : str;
    }

    /**
     * 是否是手机电话
     *
     * @param phone 手机号码 11位
     * @return
     */
    public static boolean isMobilePhoneCode(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        //匹配手机号码
        Pattern pattern = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        return pattern.matcher(phone).matches();
    }

    /**
     * 判断是否为座机号码
     *
     * @param phone
     * @return
     */
    public static boolean isFixedPhoneCode(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        boolean isPhone;
        // 验证带区号的
        if (phone.length() > 9) {
            Pattern p1 = Pattern.compile("^[0][0-9]{2,3}[0-9]{4,8}$");
            isPhone = p1.matcher(phone).matches();
        } else {
            // 验证没有区号的
            Pattern p2 = Pattern.compile("\\d{4,8}");
            isPhone = p2.matcher(phone).matches();
        }

        return isPhone;

    }

    /**
     * 验证是否为电话号码包括手机和座机
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneCode(String phone) {

        return isMobilePhoneCode(phone) || isFixedPhoneCode(phone);
    }

    /**
     * 判断是否为电话号码 原生方法
     *
     * @param phone
     * @return
     */
    public static boolean isGlobalPhoneNumber(String phone) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }

    /**
     * 判断是否为身份证
     *
     * @param idCard 身份证号
     * @return
     */
    public static boolean isIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\d{17}[0-9Xx]");
        if (!pattern.matcher(idCard).matches()) {
            return false;
        }
        //验证出生日期
        if (idCard.trim().length() == 18) {
            String date = idCard.trim().substring(6, 14);//7-14为表示出生日期
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            try {
                Date parse = format.parse(date);//把给定的日期字符串转化为日期格式
                String newDate = format.format(parse);//再把日期转化为字符串
                //原字符串和转化的字符串相等则日期合法
                //例：date=19860532 那么newDate=19860601
                if (date.equals(newDate)) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("日期不合法");
            }
        }
        return false;
    }

    /**
     * 根据合法的身份证获取性别
     *
     * @param idCard
     * @return 性别
     */
    public static String getSexByIdCard(String idCard) {
        if (isIdCard(idCard)) {
            int gd = Integer.parseInt(idCard.charAt(16) + "");
            if (gd % 2 == 0) {//偶数
                return "女";
            } else {
                return "男";
            }
        } else {
            throw new IllegalArgumentException("身份证号码不合法");
        }
    }

    /**
     * 把字符串用URLEncoder编码 返回可以用url请求的字符串
     * 空格转化为"%20"
     *
     * @param s       带中文的字符串
     * @param charset 编码 默认utf-8
     * @return
     */
    public static String urlEncodeCH(String s, String charset) {
        if (isEmpty(s)) {
            return null;
        }
        if (charset == null) {
            charset = "utf-8";
        }
        String result = "";
        String[] array = s.split("");
        for (int i = 0; i < array.length; i++) {
            try {
                int length = array[i].getBytes(charset).length;
                if (length >= 2) {
                    String encode = URLEncoder.encode(array[i], charset);
                    result += encode;
                } else {
                    result += array[i];
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.replaceAll(" ", "%20");
    }

    /**
     * 把给定的时间(单位秒)转化成 00:00的格式
     *
     * @param seconds
     * @return
     */
    public static String getStringTime(long seconds) {
        if (seconds <= 0) {
            return "00:00";
        } else if (seconds < 10) {
            return "00:0" + seconds;
        } else if (seconds < 60) {
            return "00:" + seconds;
        } else {
            String s = "";
            long mm = seconds / 60;//分钟
            long ss = seconds % 60;//秒
            if (mm < 10) {
                s += "0" + mm + ":";
            } else {
                s += mm + ":";
            }
            if (ss < 10) {
                s += "0" + ss;
            } else {
                s += ss;
            }
            return s;
        }

    }

}
