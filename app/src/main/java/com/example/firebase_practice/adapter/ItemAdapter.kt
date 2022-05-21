package com.example.firebase_practice.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_practice.databinding.ItemsLayoutBinding
import com.example.firebase_practice.model.ItemData

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    var itemsList = emptyList<ItemData>()

    class MyViewHolder(val binding: ItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //View Holder of the Adapter....
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.MyViewHolder {
        return MyViewHolder(
            ItemsLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false )
        )

    }

    override fun onBindViewHolder(holder: ItemAdapter.MyViewHolder, position: Int) {
       holder.binding.apply {
           dataTimeEt.setText(itemsList[position].dataTime.toString())
           personName.setText(itemsList[position].personName.toString())
           location.setText(itemsList[position].location.toString())
           details.setText(itemsList[position].details.toString())
       }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getDataChanges(items: List<ItemData>) {
        this.itemsList = items
        this.notifyDataSetChanged()
    }
}