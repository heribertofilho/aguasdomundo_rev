package com.example.herib.guasdomundo.Adapters

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.herib.guasdomundo.Models.Foto
import com.example.herib.guasdomundo.R
import android.view.LayoutInflater
import android.widget.ImageButton


/**
 * Created by herib on 03/06/2017.
 */

class FotoAdapter(val context: Context, val layoutResourceId: Int, data: ArrayList<Foto>) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {
    private var data = ArrayList<Foto>()

    init {
        this.data = data
    }

    inner class FotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.fotoView) as ImageView
    }

    override fun onBindViewHolder(holder: FotoHolder, position: Int) {
        holder.image.setImageBitmap(data[position].image)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoHolder {
        return FotoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.foto_layout, parent, false))
    }
}