package com.appbaselib.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.pangu.appbaselibrary.R

/**
 * Created by CLOUD on 2016/10/14.   两个项目通用的东西
 */

object ImageLoader {
    fun load(context: Context, path: String, imageView: ImageView) {
        if (path.isNullOrEmpty())
            return
        Glide.with(context).load(path)
                //  .centerCrop()  //
                .placeholder(R.drawable.defalut_img_bg)
                //                .error(R.drawable.image_failure)
                .skipMemoryCache(false)
                .transition(withCrossFade())
                .into(imageView)

    }

    //防止 CircleImageView不生效
    fun loadNoHolder(context: Context, path: String, imageView: ImageView) {
        Glide.with(context).load(path)
                //  .centerCrop()  //
                //                .error(R.drawable.image_failure)
                .into(imageView)

    }

    fun loadHead(context: Context, path: String?, imageView: ImageView) {

        if (path.isNullOrEmpty() || "https://f-bd.imuguang.com/".equals(path)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.moren_icon))

        } else {
            Glide.with(context).load(path)
                    //  .centerCrop()  //
                    //                .error(R.drawable.image_failure)
                    .into(imageView)
        }

    }


    fun load(context: Context, path: String, imageView: ImageView, drawable: Int = 0) {
        Glide.with(context).load(path)
                //  .centerCrop()  //
                .placeholder(if (drawable == 0) R.drawable.defalut_img_bg else drawable)
                //                .error(R.drawable.image_failure)
                .transition(withCrossFade())
                .into(imageView)

    }

    fun load(context: Context, path: String, drawable: Drawable, imageView: ImageView) {
        Glide.with(context).load(path)
                //  .centerCrop()  //
                .placeholder(drawable)
                //                .error(R.drawable.image_failure)
                .transition(withCrossFade())
                .into(imageView)

    }

    fun load(context: Context, path: String, imageView: ImageView, corner: Float) {
        Glide.with(context).load(path)
                //  .centerCrop()  //
                .placeholder(R.drawable.defalut_img_bg)
                .apply(RequestOptions().transforms(CenterCrop(), RoundCorner(context, corner)))
                //   .transition(withCrossFade())
                .into(imageView)

    }


    fun load(context: Context, path: Uri, imageView: ImageView) {
        Glide.with(context).load(path)
                //  .centerCrop()  //
                .transition(withCrossFade())
                .placeholder(R.drawable.moren_icon)
                //                .error(R.drawable.image_failure)
                .into(imageView)

    }

    fun load(context: Context, path: String, imageView: ImageView, mRequestListener: RequestListener<Drawable>) {
        Glide.with(context).load(path)
                .transition(withCrossFade())
                .listener(mRequestListener)
                .into(imageView)

    }

    fun load(context: Context, path: Uri, imageView: ImageView, mRequestListener: RequestListener<Drawable>) {
        Glide.with(context).load(path)
                .transition(withCrossFade())
                .listener(mRequestListener)
                .into(imageView)

    }
}
