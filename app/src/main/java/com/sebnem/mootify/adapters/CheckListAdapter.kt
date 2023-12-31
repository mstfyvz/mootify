package com.sebnem.mootify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sebnem.mootify.databinding.ItemCheckBinding
import com.sebnem.mootify.db.CheckList

internal class CheckListAdapter : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {
    private var itemCheckList: List<CheckList> = ArrayList()

    internal inner class ViewHolder(val binding: ItemCheckBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(holder.binding) {
            val item = itemCheckList[position]
            textViewTitle.text = item.title
            checkbox.isChecked = item.isCompleted

            checkbox.setOnClickListener {
                item.isCompleted = !item.isCompleted
                item.update()
                checkbox.isChecked = item.isCompleted
            }

            imageViewDelete.setOnClickListener {
                item.delete()
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int {
        return itemCheckList.size
    }

    fun setData(list: List<CheckList>) {
        itemCheckList = ArrayList(list)
        notifyDataSetChanged()
    }
}

