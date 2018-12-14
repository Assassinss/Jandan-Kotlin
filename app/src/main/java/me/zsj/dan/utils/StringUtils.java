package me.zsj.dan.utils;

import android.text.TextUtils;

import org.jsoup.Jsoup;

/**
 * @author zsj
 */

public class StringUtils {

    private static final String FILTER_TAG = "\\r";
    public static final String FILTER_1 = "\r\n";
    public static final String FILTER_2 = "\r\n\r\n";
    private static final String N_TAG = "\\n";
    private static final String EMPTY_STR = "";

    public static String filter(String filterStr) {
        return Jsoup.parse(filterStr).text();
    }

    public static String filterHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            return html.replaceAll("<p>", EMPTY_STR).replaceAll("</p>", EMPTY_STR)
                    .replaceAll(N_TAG, EMPTY_STR);
        } else {
            return html;
        }
    }

    public static String makeFileName(String picUrl) {
        int indexOf = picUrl.lastIndexOf('/');
        return picUrl.substring(indexOf, picUrl.length());
    }
}
