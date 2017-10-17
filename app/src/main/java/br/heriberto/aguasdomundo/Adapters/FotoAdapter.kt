package br.heriberto.aguasdomundo.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import br.heriberto.aguasdomundo.Models.Foto
import br.heriberto.aguasdomundo.PerguntasActivity
import br.heriberto.aguasdomundo.QuestionarioActivity
import br.heriberto.aguasdomundo.R
import com.squareup.picasso.Picasso


/**
 * Created by herib on 03/06/2017.
 */

class FotoAdapter(private val activity: QuestionarioActivity, val context: Context, val width: Int, data: ArrayList<Foto>) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {
    private var data = ArrayList<Foto>()

    init {
        this.data = data
    }

    inner class FotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.fotoView)
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
            val requestCode = 0
            val intent = Intent(context, PerguntasActivity::class.java)
            intent.putExtra("fotoUrl", imageURI.toString())
            startActivityForResult(activity, intent, requestCode, null)
        })
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
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