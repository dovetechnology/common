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

class LightAdapter<T> @JvmOverloads constructor(
    val items: MutableList<T> = mutableListOf<T>()
    , var itemClickListener: ((adapter: LightAdapter<T>, view: View, position: Int) -> Unit)? = null
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var core =
        mutableMapOf<Int, Pair<Int, (BaseViewHolder) -> Unit>>() //layout. type  ,viewholder
    private var default_type = 0

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
    ): LightAdapter<T> {
        core.put(type, Pair(layout, unit))
        return this
    }

    override fun getItemViewType(position: Int): Int {


        if (items != null && items.size != 0 && position < items.size) {
            if (items[position] is MultiItemEntity) //多种type的时候
                return (items[position] as MultiItemEntity).itemType()
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(core.get(viewType)!!.first, parent, false)
        var holder = BaseViewHolder(itemView)
        itemClickListener?.let {
            itemView.setOnClickListener {
                itemClickListener?.invoke(this, itemView, holder.adapterPosition)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        core.get(getItemViewType(position))!!.second(holder)
    }

    override fun getItemCount(): Int {

        return items.size
    }

    fun add(list: MutableList<T>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    //===================================我是分界线========================================//

    interface MultiItemEntity {
        fun itemType(): Int
    }
}