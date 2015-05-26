package com.jixianxueyuan.util.filedownload;


public class Tool {

    public static String regenerate_filename(String filename) {
        int size = filename.length();
        if (size <= 16) {
            return filename;
        }

        String short_filename = filename.substring(0, 8) + "..." +
                filename.substring(size - 5);
        return short_filename;
    }


    public static String get_prefix_filename(String filename) {
        int size = filename.length();
        if (size <= 16) {
            return filename;
        }

        String short_filename = filename.substring(0, 8) + "...";
        return short_filename;
    }
}
