package me.zsj.dan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author zsj
 */

public class FreshNew {

    public String status;
    public int count;
    public int count_total;
    public int pages;
    public List<Post> posts;

    public static class Tag implements Parcelable {
        public String id;
        public String slug;
        public String description;
        @SerializedName("post_Count")
        public int postCount;

        protected Tag(Parcel in) {
            id = in.readString();
            slug = in.readString();
            description = in.readString();
            postCount = in.readInt();
        }

        public static final Creator<Tag> CREATOR = new Creator<Tag>() {
            @Override
            public Tag createFromParcel(Parcel in) {
                return new Tag(in);
            }

            @Override
            public Tag[] newArray(int size) {
                return new Tag[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(slug);
            dest.writeString(description);
            dest.writeInt(postCount);
        }
    }

    public static class Author implements Parcelable {
        public String id;
        public String name;
        public String url;
        public String description;

        protected Author(Parcel in) {
            id = in.readString();
            name = in.readString();
            url = in.readString();
            description = in.readString();
        }

        public static final Creator<Author> CREATOR = new Creator<Author>() {
            @Override
            public Author createFromParcel(Parcel in) {
                return new Author(in);
            }

            @Override
            public Author[] newArray(int size) {
                return new Author[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(url);
            dest.writeString(description);
        }
    }
}
