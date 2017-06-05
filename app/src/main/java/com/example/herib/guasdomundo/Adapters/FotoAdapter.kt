package com.example.herib.guasdomundo.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.herib.guasdomundo.Models.Foto
import com.example.herib.guasdomundo.PerguntasActivity
import com.example.herib.guasdomundo.QuestionarioActivity
import com.example.herib.guasdomundo.R
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


/**
 * Created by herib on 03/06/2017.
 */

class FotoAdapter(val activity: QuestionarioActivity, val context: Context, val width: Int, data: ArrayList<Foto>) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {
    private var data = ArrayList<Foto>()

    init {
        this.data = data
    }

    inner class FotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.fotoView) as ImageView
    }

    override fun onBindViewHolder(holder: FotoHolder, position: Int) {
        val imageURI = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, data[position].title)
        val size = dpToPx((width / 3) - 1)
        Picasso.with(context)
                .load(imageURI)
                .placeholder(R.color.material_grey_600)
                .resize(size, size)
                .centerInside()
                .into(holder.image)

        holder.image.setOnClickListener({
            val intent = Intent(context, PerguntasActivity::class.java)
            intent.putExtra("fotoUrl", imageURI.toString())
            startActivity(context, intent, null)
        })
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics = context.getResources().getDisplayMetrics()
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoHolder {
        return FotoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.foto_layout, parent, false))
    }
}