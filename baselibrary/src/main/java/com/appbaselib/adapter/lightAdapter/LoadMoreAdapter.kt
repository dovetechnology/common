package com.appbaselib.adapter.lightAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import com.appbaselib.lightAdapter.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.pangu.appbaselibrary.R
import java.net.CookieHandler

class LoadMoreAdapter<T> @JvmOverloads constructor(
    val items: MutableList<T> = mutableListOf<T>()
    , var itemClickListener: ((adapter: LoadMoreAdapter<T>, view: View, position: Int) -> Unit)? = null
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var core =
        mutableMapOf<Int, Pair<Int, (BaseViewHolder) -> Unit>>() //layout. type  ,viewholder
    private var default_type = 0
    private var MORE_TYPE = 100
    var isLoadMore = false


//    var onItemClickListener: OnItemClickListener? = null
//    fun setOnClickItemListener(itemClickListener: (adapter: LightAdapter, view: View, position: Int) -> Unit) {
//        this.onItemClickListener = object :OnItemClickListener{
//            override fun onItemClick(adapter: LightAdapter, view: View, position: Int) {
//                itemClickListener(adapter,view,position)
//            }
//
//        }
//    }

    fun register(
        @LayoutRes layout: Int, type: Int = default_type,
        unit: (baseViewHolder: BaseViewHolder) -> Unit
    ): LoadMoreAdapter<T> {

        core.put(type, Pair(layout, unit))

        return this
    }

    override fun getItemViewType(position: Int): Int {


        if (items != null && items.size != 0 && position < items.size) {
            if (items[position] is MultiItemEntity) //多种type的时候
                return (items[position] as MultiItemEntity).itemType()
        }
        if (position >= items.size) {
            return MORE_TYPE
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(core.get(viewType)!!.first, parent, false)
        var holder = BaseViewHolder(itemView)
        itemClickListener?.let {
            //防止footer点击事件的问题
            if (holder.adapterPosition < items.size) {
                itemView.setOnClickListener {
                    itemClickListener?.invoke(this, itemView, holder.adapterPosition)
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        core.get(getItemViewType(position))!!.second(holder)
    }

    override fun getItemCount(): Int {

        return if (isLoadMore) items.size + 1 else items.size
    }

    fun add(list: MutableList<T>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    //==================loadmore=-============
    var state = 1; //1 正在加载，2，加载失败,3结束

    fun loadMore(u: () -> Unit): LoadMoreAdapter<T> {
        isLoadMore = true
        register(R.layout.quick_view_load_more, MORE_TYPE, {
            var more = it.getView<LinearLayout>(R.id.load_more_loading_view)
            var end = it.getView<FrameLayout>(R.id.load_more_load_end_view)
            var fail = it.getView<FrameLayout>(R.id.load_more_load_fail_view)

            if (state == 2) {
                more.visibility = View.GONE
                end.visibility = View.GONE
                fail.visibility = View.VISIBLE
                it.itemView.setOnClickListener {
                    more.visibility = View.VISIBLE
                    end.visibility = View.GONE
                    fail.visibility = View.GONE
                    u()
                }
            } else if (state == 3) {
                more.visibility = View.GONE
                end.visibility = View.VISIBLE
                fail.visibility = View.GONE
            } else if (state == 1) {
                more.visibility = View.VISIBLE
                end.visibility = View.GONE
                fail.visibility = View.GONE
                u()
            } else {

            }
        })
        return this
    }

    fun loadMoreEnd() {
        state = 3
        notifyItemChanged(itemCount - 1)
    }


    fun loadMoreComplete() {
        //this.rec
        state = 1  //加载完毕恢复 正在加载状态
    }

    fun loadMoreFail() {
        state = 2
        notifyItemChanged(itemCount - 1)
    }
    //===================================我是分界线========================================//

    interface MultiItemEntity {
        fun itemType(): Int
    }
}