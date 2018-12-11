package me.zsj.dan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author zsj
 */

public class Comment {

    public String id;
    @SerializedName("comment_ID")
    public String commentId;
    @SerializedName("comment_post_ID")
    public String postId;
    @SerializedName("comment_author")
    public String author;
    public String name;
    @SerializedName("comment_date")
    public Date commentDate;
    public Date date;
    @SerializedName("comment_content")
    public String commentContent;
    public String content;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("comment_reply_ID")
    public String replyId;
    @SerializedName("vote_positive")
    public String votePositive;
    @SerializedName("vote_negative")
    public String voteNegative;
    @SerializedName("sub_comment_count")
    public String commentCount;
    @SerializedName("text_content")
    public String textContent;
    public ArrayList<String> pics;
    public boolean voted = false;
    public boolean negative = false;

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", commentDate=" + commentDate +
                ", date=" + date +
                ", commentContent='" + commentContent + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", replyId='" + replyId + '\'' +
                ", votePositive='" + votePositive + '\'' +
                ", voteNegative='" + voteNegative + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", textContent='" + textContent + '\'' +
                ", pics=" + pics +
                ", voted=" + voted +
                ", negative=" + negative +
                '}';
    }
}
