package me.zsj.dan.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;
import me.zsj.dan.R;

/**
 * @author zsj
 */

public class TextBinder extends ItemViewBinder<String, TextBinder.TextHolder> {

    @NonNull
    @Override
    protected TextHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_text_test, parent, false);
        return new TextHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TextHolder holder, @NonNull String item) {

    }

    class TextHolder extends RecyclerView.ViewHolder {

        public TextHolder(View itemView) {
            super(itemView);
        }
    }
}
