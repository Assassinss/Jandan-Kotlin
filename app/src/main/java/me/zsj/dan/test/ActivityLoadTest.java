package me.zsj.dan.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.dan.R;
import me.zsj.dan.model.Comment;

/**
 * @author zsj
 */

public class ActivityLoadTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_test);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final Items items = new Items();
        for (int i = 0; i < 20; i++) {
            items.add(String.valueOf(i));
        }

        final MultiTypeAdapter adapter = new MultiTypeAdapter(items);
        adapter.register(String.class, new TextBinder());
        //adapter.register(Comment.class, new LoadingViewBinder());

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (layoutManager.findFirstCompletelyVisibleItemPosition() >= layoutManager.getItemCount() - 10
                        && layoutManager.findFirstVisibleItemPosition() != 0) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                        items.add(new Comment());
                        adapter.notifyItemChanged(items.size() - 1);
                        recyclerView.scrollToPosition(items.size() - 1);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                items.remove(items.size() - 1);
                                adapter.notifyItemChanged(items.size() - 1);
                                for (int i = 0; i < 20; i++) {
                                    items.add(String.valueOf(i));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }, 2000);
                    }
                }
            }
        });
    }
}
