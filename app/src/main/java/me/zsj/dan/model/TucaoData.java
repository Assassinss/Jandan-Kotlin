package me.zsj.dan.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author zsj
 */

public class TucaoData {

    @SerializedName("tucao")
    public List<Tucao> comments;

    public static class Tucao {
        public String comment_ID;
        public String comment_post_ID;
        public String comment_author;
        public Date comment_date;
        public String comment_content;
        public String comment_parent;
        public String comment_reply_ID;
        public String vote_positive;
        public String vote_negative;
    }
}
