package com.my.map.constant;

public  class Information {
    public static  String id;//用户id
    public  static String username;//用户名
    public static String passworrd;//密码；
    public static String startTime;
    public static String endTime;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Information.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Information.username = username;
    }

    public static String getPassworrd() {
        return passworrd;
    }

    public static void setPassworrd(String passworrd) {
        Information.passworrd = passworrd;
    }

    public static String getStartTime() {
        return startTime;
    }

    public static void setStartTime(String startTime) {
        Information.startTime = startTime;
    }

    public static String getEndTime() {
        return endTime;
    }

    public static void setEndTime(String endTime) {
        Information.endTime = endTime;
    }
}
