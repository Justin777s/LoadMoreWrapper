package com.treasure.loadmore.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.github.nukc.sample.R;
import com.treasure.loadmore.base.BasePagingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * vlayout 分页示例
 * Created by liusaibao on 17/11/2017.
 */

public class VLayoutRefreshAndLoadMoreActivity extends BasePagingActivity implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreAdapter mLoadMoreAdapter;
    private VLayoutAdapter mVLayoutAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlayout_refresh_loadmore);
        setTitle(R.string.vlayout_usage);

        mHandler = new Handler(getMainLooper());

        mSwipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        List<DelegateAdapter.Adapter> adapters = new ArrayList<>();
        // vlayout的使用 https://github.com/alibaba/vlayout/blob/master/README.md
        mVLayoutAdapter = new VLayoutAdapter();
        adapters.add(mVLayoutAdapter);
        DelegateAdapter mDelegateAdapter = new DelegateAdapter(layoutManager);
        mDelegateAdapter.setAdapters(adapters);
        mLoadMoreAdapter = LoadMoreWrapper.with(mDelegateAdapter)
                .setLoadMoreEnabled(false)
                .setListener(this)
                .into(mRecyclerView);
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        loadData();
    }

    @Override
    public void onRefresh() {
        resetPageNum();
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    private void loadData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setPages(3);
                if (getCurrentPageNum() == 1) {
                    mVLayoutAdapter.clear();
                } else {
                    mVLayoutAdapter.addItem();
                }
                setNextPageNum();
                if (resetRefreshing()) {
                    if (!mLoadMoreAdapter.getLoadMoreEnabled()) {
                        mLoadMoreAdapter.setLoadMoreEnabled(true);
                    }
                }
            }
        }, 1000);
    }

    private boolean resetRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            return true;
        }
        return false;
    }

    @Override
    public LoadMoreAdapter getLoadMoreAdapter() {
        return mLoadMoreAdapter;
    }

    private static class VLayoutAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

        private int mCount;

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return new LinearLayoutHelper();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sample, parent, false);
            return new VLayoutHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((VLayoutHolder) holder).mTextView.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }

        public void clear() {
            mCount = PAGE_SIZE;
            notifyDataSetChanged();
        }

        public void addItem() {
            final int positionStart = mCount;
            mCount += PAGE_SIZE;
            notifyItemRangeInserted(positionStart, PAGE_SIZE);
        }

        static class VLayoutHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            public VLayoutHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
