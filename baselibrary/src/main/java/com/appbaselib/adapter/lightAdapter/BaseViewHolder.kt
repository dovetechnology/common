package com.appbaselib.lightAdapter;

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView
import com.appbaselib.adapter.lightAdapter.LightAdapter

class BaseViewHolder : RecyclerView.ViewHolder {

    private val views = SparseArray<View>()//保存view

    constructor(view: View) : super(view) {
    }

    fun getView(@IdRes viewId: Int): View? {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view
    }

    fun setText(viewId: Int, text: String): BaseViewHolder {
        var mTextView = getView(viewId) as TextView
        mTextView.text = text
        return this

    }

}
