package ru.air.common;

/**
 * Created by Admin on 23.10.2016.
 */
public class CommonUtil {
    public static String parseText(String status) {
        String textDigits = "";
        for (char ch : status.toCharArray()) {
            if (Character.isDigit(ch) || Character.isSpaceChar(ch) || ch == ':' || ch == '.' || ch == ')' || ch == '(') {
                textDigits = textDigits + ch;
            }
        }
        return textDigits.trim();
    }
}
