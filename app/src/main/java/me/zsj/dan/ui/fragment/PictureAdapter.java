package me.zsj.dan.ui.fragment;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.dan.visibility.items.ListItem;
import me.zsj.dan.visibility.scroll_utils.ItemsProvider;

/**
 * @author zsj
 */

public class PictureAdapter extends MultiTypeAdapter implements ItemsProvider {

    private RecyclerView recyclerView;

    public PictureAdapter(List<?> items) {
        super(items);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public ListItem getListItem(int position) {
        if (getItems() == null) {
            return null;
        }
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        if (holder instanceof ListItem) {
            return (ListItem) holder;
        }
        return null;
    }

    @Override
    public int listItemSize() {
        return getItemCount();
    }

}
