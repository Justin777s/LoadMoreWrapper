package com.treasure.loadmore.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;

/**
 * 用于分页的Activity
 * Created by LiuSaibao on 9/28/2017.
 */

public abstract class BasePagingActivity extends AppCompatActivity implements IPaging {

    private PagingUtil pagingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagingUtil = new PagingUtil();
    }

    /**
     * 加载更多数据需要设置总页数
     *
     * @param pages
     */
    @Override
    public void setPages(int pages) {
        pagingUtil.setPages(pages);
    }

    /**
     * 获取最新数据需要重置
     */
    @Override
    public void resetPageNum() {
        pagingUtil.resetPageNum();
    }

    /**
     * 设置下一页
     */
    @Override
    public void setNextPageNum() {
        pagingUtil.setNextPageNum();
    }

    /**
     * 获取当前页
     *
     * @return
     */
    @Override
    public int getCurrentPageNum() {
        return pagingUtil.getCurrentPageNum();
    }

    @Override
    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
        if (pagingUtil.getPages() >= getCurrentPageNum()) {
            onLoadMore();
        } else {
            noMoreData();
        }
    }
}
