package ru.red.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class HTTPHeaders {

    private static String HOST = "";
    private static String URL = "";
    public static HashMap<String, String> DEFAULT_HEADERS = new HashMap<>();
    public static HashMap<String, String> LOGIN_HEADERS = new HashMap<>();
    public static HashMap<String, String> REPORT_HEADERS = new HashMap<>();

    public HTTPHeaders() {
        DEFAULT_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
        DEFAULT_HEADERS.put("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        DEFAULT_HEADERS.put("Accept-Encoding", "gzip, deflate");
        DEFAULT_HEADERS.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        DEFAULT_HEADERS.put("Connection", "keep-alive");
    }
}
