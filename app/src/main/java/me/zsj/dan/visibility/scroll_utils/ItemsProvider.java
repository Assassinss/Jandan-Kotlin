package me.zsj.dan.visibility.scroll_utils;

import me.zsj.dan.visibility.items.ListItem;

/**
 * @author zsj
 */

public interface ItemsProvider {

    ListItem getListItem(int position);

    int listItemSize();
}
