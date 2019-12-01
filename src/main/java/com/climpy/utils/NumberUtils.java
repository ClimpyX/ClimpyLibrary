package com.climpy.utils;

public class NumberUtils {

    public static boolean isInteger(String value) {

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static Integer parseInt(String string) {

        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static int parseInt(String intString, int fallBack) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException ex) {
            return fallBack;
        }
    }

    public static boolean isLong(String value) {

        try {
            Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    public static Long parseLong(String string) {

        try {
            return Long.parseLong(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static long parseLong(String longString, int fallBack) {
        try {
            return Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            return fallBack;
        }
    }
}
