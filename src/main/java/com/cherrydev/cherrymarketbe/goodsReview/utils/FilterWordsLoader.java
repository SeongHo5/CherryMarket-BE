package com.cherrydev.cherrymarketbe.goodsReview.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FilterWordsLoader {

    private static final String FILTERING_URL = "https://kr.object.ncloudstorage.com/guest-syh/null/filtering_UTF-8.txt";

    public static List<String> loadFilterWords() {
        List<String> filterWords = new ArrayList<>();
        try {
            System.out.println("Start loading filter words...");
            URL fileUrl = new URL(FILTERING_URL);
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmedLine = line.trim();
                    filterWords.add(trimmedLine);
                    //디버깅용
//                    System.out.println("Loaded word: " + trimmedLine);
                }
            }
            System.out.println("Filter words loaded successfully.");
            connection.disconnect();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("return");
        return filterWords;
    }

}

