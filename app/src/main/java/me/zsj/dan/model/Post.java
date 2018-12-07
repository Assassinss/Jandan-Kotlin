package me.zsj.dan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zsj
 */

public class Post implements Parcelable {

    public String id;
    public String url;
    @SerializedName("title_plain")
    public String titlePlain;
    public String title;
    public String content;
    public String excerpt;
    public Date date;
    public List<FreshNew.Tag> tags;
    public FreshNew.Author author;
    @SerializedName("comment_count")
    public int commentCount;
    @SerializedName("comment_status")
    public String commentStatus;
    @SerializedName("custom_fields")
    public CustomFields customFields;
    public ArrayList<Comment> comments;
    @SerializedName("comments_rank")
    public ArrayList<Comment> commentsRank;

    protected Post(Parcel in) {
        id = in.readString();
        url = in.readString();
        titlePlain = in.readString();
        title = in.readString();
        content = in.readString();
        excerpt = in.readString();
        date = (Date) in.readSerializable();
        tags = in.createTypedArrayList(FreshNew.Tag.CREATOR);
        author = in.readParcelable(FreshNew.Author.class.getClassLoader());
        commentCount = in.readInt();
        commentStatus = in.readString();
        customFields = in.readParcelable(CustomFields.class.getClassLoader());
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(titlePlain);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(excerpt);
        dest.writeSerializable(date);
        dest.writeTypedList(tags);
        dest.writeParcelable(author, flags);
        dest.writeInt(commentCount);
        dest.writeString(commentStatus);
        dest.writeParcelable(customFields, flags);
    }

    public static class CustomFields implements Parcelable {
        @SerializedName("thumb_c")
        public List<String> thumbC;

        protected CustomFields(Parcel in) {
            thumbC = in.createStringArrayList();
        }

        public static final Creator<CustomFields> CREATOR = new Creator<CustomFields>() {
            @Override
            public CustomFields createFromParcel(Parcel in) {
                return new CustomFields(in);
            }

            @Override
            public CustomFields[] newArray(int size) {
                return new CustomFields[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(thumbC);
        }
    }
}
