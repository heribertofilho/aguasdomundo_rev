package com.example.herib.guasdomundo.Adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.herib.guasdomundo.Models.Foto
import com.example.herib.guasdomundo.R


/**
 * Created by herib on 03/06/2017.
 */

class GridViewAdapter(val context: Context, val layoutResourceId: Int, data: ArrayList<Foto>) {
    private var data = ArrayList<Foto>()

    init {
        this.data = data
    }

    fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var row: View? = convertView
        var holder: ViewHolder?

        if (row == null) {
            val inflater = (context as Activity).layoutInflater
            row = inflater.inflate(R.layout.foto_layout, parent, false)
            holder = ViewHolder()
            holder.image = row!!.findViewById(R.id.fotoView) as ImageView
            row.setTag(holder)
        } else {
            holder = row.tag as ViewHolder
        }

        val item = data.get(position)
        holder.image!!.setImageBitmap(item.image)
        return row
    }

    internal class ViewHolder {
        var imageTitle: TextView? = null
        var image: ImageView? = null
    }
}