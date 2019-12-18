package com.appbaselib.adapter.lightAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import com.appbaselib.lightAdapter.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.pangu.appbaselibrary.R

import java.util.ArrayList
import java.util.Collections

class LightAdapter @JvmOverloads constructor(val items: MutableList<Any> = mutableListOf<Any>()) : RecyclerView.Adapter<BaseViewHolder>() {

    private var core = mutableMapOf<Int, Pair<Int, (BaseViewHolder) -> Unit>>()
    private var default_type = 0xff
    public var END = false

    fun register(@LayoutRes layout: Int, unit: (baseViewHolder: BaseViewHolder) -> Unit, type: Int = default_type): LightAdapter {

        core.put(type, Pair(layout, unit))

        return this
    }

    override fun getItemViewType(position: Int): Int {

        if (items != null && items.size != 0 && items[position] is MultiItemEntity) {
            return (items[position] as MultiItemEntity).itemType
        }
        if (END) {
            return 100
        }
        return default_type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(core.get(viewType)!!.first, parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        core.get(getItemViewType(position))!!.second(holder)
    }

    override fun getItemCount(): Int {

        return if (END) items.size + 1 else items.size
    }

    fun add(list: ArrayList<*>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun loadMore(u: () -> Unit): LightAdapter {
        END = true
        register(R.layout.quick_view_load_more, { u }, 100)
        return this
    }

    interface MultiItemEntity {
        val itemType: Int
    }
}