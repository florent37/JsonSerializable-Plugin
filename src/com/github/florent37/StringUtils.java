package com.github.florent37;

public class StringUtils {

    enum IndexType {
        First,
        Last,
    }

    public static int findLine(String completeText, String text, IndexType indexType) {
        final int index;
        switch (indexType) {
            case Last:
                index = completeText.lastIndexOf(text);
                break;
            case First:
                index = completeText.indexOf(text);
                break;
            default:
                return -1;
        }

        final String targetStr = completeText.substring(0, index).replace("\n\n", "\n-\n");
        final String[] lines = targetStr.split("\n");
        return lines.length;
    }

}
