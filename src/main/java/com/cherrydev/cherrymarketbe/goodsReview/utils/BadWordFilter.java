package com.cherrydev.cherrymarketbe.goodsReview.utils;

import java.util.List;

public class BadWordFilter {

    public static int[] prefixTable(String pattern) {
        int n = pattern.length();
        int[] pi = new int[n];

        int i = 1;
        int j = 0;
        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                pi[i] = j + 1;
                i++;
                j++;
            } else {
                if (j > 0) {
                    j = pi[j - 1];
                } else {
                    pi[i] = 0;
                    i++;
                }
            }
        }
        return pi;
    }

    public static int kmpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int[] pi = prefixTable(pattern);

        int i = 0;
        int j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    return i - m; // 찾은 금칙어의 시작 인덱스를 반환
                }
            } else {
                if (j > 0) {
                    j = pi[j - 1];
                } else {
                    i++;
                }
            }
        }
        return -1;
    }

    public static String replace(String text, String pattern, String replacement, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(text.substring(0, index));
        sb.append(replacement);
        sb.append(text.substring(index + pattern.length()));
        return sb.toString();
    }

    public static String filterAndReplace(String text) {
        // 금칙어 목록 로드
        List<String> forbiddenWords = FilterWordsLoader.loadFilterWords();

        // 금칙어 검색 및 *로 변경 (KMP 알고리즘 사용)
        for (String forbiddenWord : forbiddenWords) {
            int index = kmpSearch(text, forbiddenWord);
            while (index != -1) {
                // 금칙어를 찾았을 경우, *로 변경
                String replacement = "*".repeat(forbiddenWord.length());
                System.out.println("Found forbidden word: " + forbiddenWord + " at index: " + index);
                System.out.println("Original text: " + text);
                System.out.println("Replacement: " + replacement);
                text = replace(text, forbiddenWord, replacement, index);

                // 변경된 부분 이후의 텍스트에서 금칙어 검색
                index = kmpSearch(text, forbiddenWord);
            }
        }
        return text;
    }

    public static String CheckForForbiddenWords(String content) {
        System.out.println("CheckForForbiddenWords start");
        return BadWordFilter.filterAndReplace(content);
    }
}


