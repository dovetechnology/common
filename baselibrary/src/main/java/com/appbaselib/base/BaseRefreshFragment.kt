package com.dove.imuguang.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appbaselib.base.BaseMvcFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pangu.appbaselibrary.R
import com.safframework.ext.postDelayed

import java.util.ArrayList


/**
 * Created by tangming on 2016/12/14.
 */

abstract class BaseRefreshFragment<T> : BaseMvcFragment() {

    lateinit var mRecyclerview: androidx.recyclerview.widget.RecyclerView
    var mSwipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null

    lateinit var mList: MutableList<T>
    lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>
    lateinit var mLinearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
    var isReReresh = false//重新刷新 清楚数据
    var pageNo = 1  //当前页
    var isFirstReresh = true
    var pageSize = 10 //每页条数
    var isLoadmore = false //是否开启加载更多
    var isLoadmoreIng = false  //是否正在加载更多

    //butternife  暂时无法解决
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = super.onCreateView(inflater, container, savedInstanceState)
        mRecyclerview = mView!!.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerview)
        mSwipeRefreshLayout = mView.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe)
        return mView
    }

    @CallSuper
    override fun initView() {
        initList()
        //设置只在viewpager的情况下 开启懒加载

        //        if (isLazyLoad()) {
        //            if (!(this.getView().getParent() instanceof ViewPager)) {   //懒加载 如果 父控件不是  viewpager （adapter必须是fragmentadapter） 不会调用  setvisiblehint
        //                initData();
        //            }
        //        } else {
        //            initData();
        //        }

    }

    override fun getLoadingTargetView(): View? {
        return mSwipeRefreshLayout
    }

    //默认实现
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_recyclerview
    }

    // tips:要获取 getintent 的数据 ，必须重写 getIntentData方法，在里面去获取
    fun initList() {

        val mView = activity!!.layoutInflater.inflate(R.layout.view_empty, mRecyclerview.parent as ViewGroup, false)
        mList = ArrayList()
        mLinearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        mLinearLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        mRecyclerview.layoutManager = mLinearLayoutManager
        mSwipeRefreshLayout?.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        mSwipeRefreshLayout?.setOnRefreshListener {
            if (!isLoadmoreIng)
                postDelayed(300)
                {
                    refreshData(false)
                }
            else
                mSwipeRefreshLayout?.isRefreshing = false
        }
        initAdapter()
        if (mAdapter == null)
            throw NullPointerException("adapter is null")
        mAdapter!!.emptyView = mView

    }

    fun setLoadMoreListener() {
        isLoadmore = true
       // mAdapter!!.setEnableLoadMore(false)
        mAdapter!!.setOnLoadMoreListener({
            if (!(mSwipeRefreshLayout?.isRefreshing ?: false)) {
                isLoadmoreIng = true
                postDelayed(300)
                {
                    requestData()

                }
            } else {

            }
        }, mRecyclerview)

    }


    abstract fun initAdapter()


    public abstract override fun requestData()

    //重新刷新数据
    fun refreshData(isShow: Boolean) {

        //   mRecyclerview.scrollToPosition(0);
        isReReresh = true
        pageNo = 1
        if (isShow)
            mSwipeRefreshLayout?.isRefreshing = true
        requestData()
//        if (isLoadmore) {
//            mAdapter!!.setEnableLoadMore(false)
//            //重新刷新,重新设置加载更多的逻辑
//        }

    }

    fun loadComplete(mData: List<T>?) {
        loadingTargetView?.let {
            toggleShowLoading(false)
        }
        mSwipeRefreshLayout?.isRefreshing = false

        if (isFirstReresh) {
            mRecyclerview.adapter = mAdapter  //如果一开始就设置，会导致 先出现  空数据 再加载数据
        }
        if (isReReresh) {
            mList.clear()
        }
        if (mData != null && mData.size != 0) {

            pageNo++
            mAdapter.addData(mData)


            if (isFirstReresh || isReReresh) {

                isFirstReresh = false
                isReReresh = false
                //当数据不满一页的时候，取消加载更多
                if (isLoadmore) {

                    //延时操作，避免 lastitem为 -1
                    //                mRecyclerview.postDelayed(new Runnable() {
                    //                    @Override
                    //                    public void run() {
                    //                        int lastItem = ((LinearLayoutManager) mRecyclerview.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    //                        int all_item = mAdapter.requestData().size() + mAdapter.getHeaderLayoutCount() + mAdapter.getFooterLayoutCount();
                    //
                    //                        if (lastItem == all_item - 1)   //表示数据不满一页
                    //                            mAdapter.setEnableLoadMore(false);
                    //                        else {
                    //                            mAdapter.notifyDataSetChanged();
                    //                            mAdapter.setEnableLoadMore(true);
                    //                            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    //                                @Override
                    //                                public void onLoadMoreRequested() {
                    //
                    //                                    if (!mSwipeRefreshLayout.isRefreshing()) {
                    //                                        isLoadmoreIng = true;
                    //                                        requestData();
                    //                                    } else {
                    //
                    //                                    }
                    //
                    //                                }
                    //                            }, mRecyclerview);
                    //
                    //                        }
                    //
                    //                    }
                    //                }, 300);

                    mAdapter!!.disableLoadMoreIfNotFullPage(mRecyclerview)
                }
            }


        } else {
            mAdapter!!.notifyDataSetChanged()//清空视图
        }

        if (isLoadmore && isLoadmoreIng) {
            isLoadmoreIng = false
            if (mData == null || mData.size == 0)
                mAdapter!!.loadMoreEnd()
            else
                mAdapter!!.loadMoreComplete()
        }
    }


    fun loadError(mes: String?) {

        if (isFirstReresh) {

            toggleShowError(true, "加载失败,点击重新加载") {
                toggleShowLoading(true, "加载中……")
                requestData()
            }
        } else {
            toggleShowLoading(false)
            showToast(mes)
            if (mSwipeRefreshLayout != null)
                mSwipeRefreshLayout?.isRefreshing = false

            if (isLoadmoreIng) {
                isLoadmoreIng = false
                mAdapter!!.loadMoreFail()
            }
        }

    }

    //===================================================我是分隔符=========================================================
}
