package com.vikravch.recyclerviewapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vikravch.recyclerviewapp.R
import com.vikravch.recyclerviewapp.data.remote.dto.PostItem
import java.text.SimpleDateFormat
import java.util.Date


class RecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val CONTENT_ITEM = 1
    private val LOADING_ITEM = 2

    private val items = mutableListOf<PostItem>()
    private var isLoaderVisible = false

    fun setItems(items: List<PostItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyItemInserted(0)
    }

    fun addItems(items: List<PostItem>) {
        this.items.addAll(items)
        notifyItemInserted(this.items.size - items.size)
    }

    fun clear() {
        items.clear()
        this.notifyItemRemoved(0)
    }

    fun addLoading() {
        isLoaderVisible = true
        items.add(PostItem(0, ""))
        notifyItemInserted(items.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = items.size - 1
        if (position >= 0) {
            val item: PostItem = items[position]
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == items.size - 1) LOADING_ITEM else CONTENT_ITEM
        } else {
            CONTENT_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CONTENT_ITEM -> VH(
                LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            )

            LOADING_ITEM -> LoaderVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is VH) {
            holder.bind(items[position])
        }
    }
}

class LoaderVH(itemView: View) : ViewHolder(itemView)

class VH(itemView: View) : ViewHolder(itemView) {
    private val context = itemView.context
    private val textView: TextView = itemView.findViewById(R.id.tvName)
    private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
    private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

    private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
    private val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)

    fun bind(item: PostItem) {
        textView.text = item.userName
        tvMessage.text = item.message
        val newDate = SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy")
            .parse(item.date) ?: Date()
        val newDate2 = SimpleDateFormat("MMM dd','yyyy h:mm a").format(newDate)
        tvDate.text = newDate2
        if (item.userPic != "null") {
            Glide.with(context).load(item.userPic)
                .apply(RequestOptions.centerCropTransform())
                .circleCrop()
                .error(R.drawable.user_circle)
                .into(ivAvatar)
        } else {
            Glide.with(context).load(R.drawable.user_circle)
                .apply(RequestOptions.centerCropTransform())
                .circleCrop()
                .into(ivAvatar)
        }
        if (item.photo != "null") {
            Glide.with(context).load(item.photo)
                .into(ivPhoto)
        }

    }
}