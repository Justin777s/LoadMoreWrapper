package com.treasure.loadmore.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;

/**
 * 用于分页的Fragment
 * Created by LiuSaibao on 9/28/2017.
 */

public abstract class BasePagingFragment extends Fragment implements IPaging{

    private PagingUtil pagingUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagingUtil = new PagingUtil();
    }

    /**
     * 加载更多数据需要设置总页数
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
     * @return
     */
    @Override
    public int getCurrentPageNum() {
        return pagingUtil.getCurrentPageNum();
    }

    @Override
    public void noMoreData() {

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
