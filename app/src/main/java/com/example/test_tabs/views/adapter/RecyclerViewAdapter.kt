package com.example.test_tabs.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test_tabs.interfaces.OnClickTabs
import com.example.test_tabs.R
import com.example.test_tabs.models.Tabs

class RecyclerViewAdapter(var listItem : List<Tabs>, private val onClickTabs: OnClickTabs) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    fun setList(listItem: List<Tabs>){
        this.listItem = listItem
        mDiffer.submitList(listItem.map {
            it.copy()
            }
        )
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Tabs>() {
        override fun areItemsTheSame(
            oldItem: Tabs,
            newItem: Tabs
        ): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: Tabs,
            newItem: Tabs
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val mDiffer: AsyncListDiffer<Tabs> = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listItem[position].let {
            holder.bind(it, onClickTabs)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(tab: Tabs, onClickTabs: OnClickTabs) {
            Glide.with(itemView).load(tab.imagePath).into(itemView.findViewById<ImageView>(R.id.image))

            itemView.setOnClickListener {
                onClickTabs.onClick(tab)
            }
        }
    }
}