package com.cherrydev.cherrymarketbe.common.entity;

public class PageRequest {

    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;

    private int page = 1;
    private int size = DEFAULT_SIZE;

    public void setPage(int page) {
        this.page = Math.max(page, 1);
    }

    public void setSize(int size) {
        this.size = Math.min(size, MAX_SIZE);
    }
}


