package com.appbaselib.base


import android.util.SparseBooleanArray
import android.view.View

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import java.util.ArrayList

/**
 * Created by tangming on 2016/11/28.   加入单选 多选 / 2017 5/7 xiugai  默认实现加载更多
 */

abstract class BaseRecyclerViewAdapter<T>(layoutResId: Int, data: MutableList<T>) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data), LoadMoreModule {


    //原点击事件不为空的话，先保存原点击事件；
    //原点击事件不为空的话，先保存原点击事件；
    var isShowMutilChoose = false
        set(mShowMutilChoose) {
            field = mShowMutilChoose
            if (mShowMutilChoose) {
                if (getOnItemClickListener() != null) {
                    mOrigionOnItemClickListener = getOnItemClickListener()

                }
                setOnItemClickListener(mSelectOnItemClickListener)

            } else {

                if (getOnItemClickListener() != null) {
                    setOnItemClickListener(mOrigionOnItemClickListener)
                } else {
                    setOnItemClickListener(null)
                }
            }
            notifyDataSetChanged()
        }  //，默认是否打开多选开关
    private val selectedItems = SparseBooleanArray()//记录选中的position

    var mSinglePosition = -1

    /**
     * 获取item数目
     */
    val selectedItemCount: Int
        get() = selectedItems.size()

    val selectedItemsKey: List<Int>
        get() {
            val items = ArrayList<Int>(selectedItems.size())
            for (i in 0 until selectedItems.size()) {
                items.add(selectedItems.keyAt(i))
            }
            return items
        }


    /**
     * 单选获取选中的items
     */
    val singleSelectedItems: T?
        get() = if (mSinglePosition >= 0) {
            data[mSinglePosition]

        } else
            null


    internal var mOrigionOnItemClickListener: OnItemClickListener? = null//多选模式下，原来的点击事件 （为了多选点击事件能覆盖整个item）

    internal var mSelectOnItemClickListener: OnItemClickListener = OnItemClickListener { adapter, view, position ->
        switchSelectedState(position)
    }

    /**
     * 多选判读是否选中
     */
    fun isSelected(position: Int): Boolean {
        return selectedItemsKey.contains(position)
    }

    /**
     * 多选切换选中或取消选中
     */
    fun switchSelectedState(position: Int) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    /**
     * 清除所有选中item的标记
     */
    fun clearSelectedState() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    /**
     * 全选
     */
    fun selectAllItems() {
        clearSelectedState()
        for (i in 0 until data.size) {
            selectedItems.put(i, true)
        }
        notifyDataSetChanged()
    }

    /**
     * 多选获取选中的items
     */
    fun getSelected(): List<T> {
        val items = ArrayList<T>(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(data[selectedItems.keyAt(i)])
        }
        return items
    }


    fun setSingleChoosed(positon: Int) {
        mSinglePosition = positon
        notifyDataSetChanged()
    }


    override fun addData(newData: T) {
        this.data.add(newData)
        this.notifyItemInserted(this.data.size)
    }


}
