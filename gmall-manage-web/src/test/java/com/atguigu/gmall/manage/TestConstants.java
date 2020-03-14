package com.atguigu.gmall.manage;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@SuppressWarnings("unused")
public class TestConstants {
    private static String ip_home = "192.168.1.106";
    private static String ip_work = "192.168.1.106";
    private static String ip_work_store = "192.168.1.106";
    public final static int PORT = 22122;//22122
    public final static int STORE_PORT = 23000; //23000
    public static InetSocketAddress address = new InetSocketAddress(ip_home, PORT);
    public static InetSocketAddress store_address = new InetSocketAddress(ip_home, STORE_PORT);
    public static final int soTimeout = 550;
    public static final int connectTimeout = 500;
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final String DEFAULT_GROUP = "group1";
    public static final String DEFAULT_STORAGE_IP = ip_home;

    public static final String PERFORM_FILE_PATH = "/images/gs.jpg";
    public static final String CAT_IMAGE_FILE = "/images/cat.jpg";
    public static final String FLY_IMAGE_FILE = "/images/fly.png";
}
