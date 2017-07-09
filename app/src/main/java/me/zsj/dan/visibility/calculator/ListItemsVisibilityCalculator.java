package me.zsj.dan.visibility.calculator;


/**
 * This is basic interface for Visibility calculator.
 * Methods of it strongly depends on Scroll events from ListView or RecyclerView
 */
public interface ListItemsVisibilityCalculator {
    void onScrolled(int scrollState);
    void onScrollStateIdle();
}
