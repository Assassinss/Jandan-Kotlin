package me.zsj.dan.model;

import android.support.annotation.NonNull;

/**
 * @author zsj
 */

public class PostComment {

    public static final String COMMENTS = "comments";
    public static final String COMMENTS_RANK = "comments_rank";
    public String category;
    public Post post;

    public PostComment(@NonNull String category, @NonNull Post post) {
        this.category = category;
        this.post = post;
    }
}
