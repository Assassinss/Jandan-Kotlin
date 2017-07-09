package me.zsj.dan.visibility.items;

import android.view.View;

/**
 * A general interface for list items.
 * This interface is used by
 *
 * @author danylo.volokh
 */
public interface ListItem {
    /**
     * When view visibility become bigger than "current active" view visibility then the new view becomes active.
     * This method is called
     */
    void setActive(View newActiveView, int newActiveViewPosition);

    /**
     * There might be a case when not only new view becomes active, but also when no view is active.
     * When view should stop being active this method is called
     */
    void deactivate(View currentView, int position);
}
