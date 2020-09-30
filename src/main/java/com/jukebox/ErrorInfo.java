package com.jukebox;

public class ErrorInfo {
    public final String url;
    public final String ex;

    public ErrorInfo(String url, String ex) {
        this.url = url;
        this.ex = ex;
    }
}