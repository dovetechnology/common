package com.dove.imuguang.base

import android.os.Bundle
import androidx.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appbaselib.base.BaseMvcFragment
import com.appbaselib.base.BaseRecyclerViewAdapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.pangu.appbaselibrary.R
import com.safframework.ext.postDelayed

import java.util.ArrayList


/**
 * Created by tangming on 2016/12/14.
 */

abstract class BaseRefreshFragment<T> : BaseMvcFragment() {

    lateinit var mRecyclerview: RecyclerView
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var mList: MutableList<T>
    lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>
    lateinit var mLinearLayoutManager: LinearLayoutManager
    var isReReresh = false//重新刷新 清楚数据
    var pageNo = 1  //当前页
    var isFirstReresh = true
    var pageSize = 10 //每页条数
    var isLoadmore = false //是否开启加载更多
    var isLoadmoreIng = false  //是否正在加载更多

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = super.onCreateView(inflater, container, savedInstanceState)
        mRecyclerview =
            mView!!.findViewById(R.id.recyclerview)
        mSwipeRefreshLayout =
            mView.findViewById(R.id.swipe)
        return mView
    }

    @CallSuper
    override fun initView() {
        initList()
    }

    override fun getLoadingTargetView(): View? {
        return mSwipeRefreshLayout
    }

    //默认实现
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_recyclerview
    }

    fun initList() {
        mList = ArrayList()
        mRecyclerview.layoutManager = getLayoutManager()
        mSwipeRefreshLayout?.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        mSwipeRefreshLayout?.setOnRefreshListener {
            if (isLoadmoreIng) {
                return@setOnRefreshListener
            }
            postDelayed(200)
            {
                refreshData(false)
            }
        }
        initAdapter()
        if (mAdapter == null) {
            throw NullPointerException("adapter is null")
        }
        mRecyclerview.adapter = mAdapter
        val mView = activity!!.layoutInflater.inflate(R.layout.view_empty, mRecyclerview, false)
        mAdapter!!.setEmptyView(mView)
        if (mAdapter is LoadMoreModule) {
            setLoadMoreListener()
        }
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {

        return LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
    }

    fun setEnableLoadMoreFalse() {
        mAdapter.loadMoreModule?.isEnableLoadMore = false
    }

    fun setLoadMoreListener() {
        isLoadmore = true
        mAdapter.loadMoreModule?.isEnableLoadMoreIfNotFullPage = false
        mAdapter.loadMoreModule?.setOnLoadMoreListener {
            isLoadmoreIng = true
            postDelayed(200)
            {
                requestData()
            }
        }
    }

    abstract fun initAdapter()

    public abstract override fun requestData()

    //重新刷新数据
    fun refreshData(isShow: Boolean) {

        isReReresh = true
        pageNo = 1
        if (isShow) {
            mSwipeRefreshLayout?.isRefreshing = true
        }
        requestData()

    }

    fun loadComplete(mData: List<T>?) {

        loadingTargetView?.let {
            toggleShowLoading(false)
        }
        mSwipeRefreshLayout?.isRefreshing = false
        //清空
        if (isReReresh) {
            mList.clear()
        }

        if (mData != null && mData.size != 0) {
            pageNo++
            mAdapter.addData(mData)
            if (isFirstReresh || isReReresh) {
                isFirstReresh = false
                isReReresh = false
                //官方demo里面 setNewData 方法里面会 checkDisableLoadMoreIfNotFullPage 这里没有调用setnewdata 所以手动调用一次
                if (isLoadmore) {
                    if (mData.size < 10) {
                        mAdapter.loadMoreModule?.isEnableLoadMore = false
                    } else {
                        setLoadMoreListener()
                    }
                }
            }
        }

        if (isLoadmore && isLoadmoreIng) {
            isLoadmoreIng = false
            if (mData == null || mData.size == 0) {
                mAdapter.loadMoreModule?.loadMoreEnd()
            } else {
                mAdapter.loadMoreModule?.loadMoreComplete()
            }
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
                mAdapter.loadMoreModule?.loadMoreFail()
            }
        }

    }

    //===================================================我是分隔符=========================================================
}
