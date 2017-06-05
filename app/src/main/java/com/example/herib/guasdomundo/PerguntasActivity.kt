package com.example.herib.guasdomundo

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perguntas.*

class PerguntasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perguntas)

        val url = intent.getStringExtra("fotoUrl")
        val fotoUri = Uri.parse(url)

        Picasso.with(this)
                .load(fotoUri)
                .fit()
                .centerCrop()
                .into(fotoView)

        imgClose.setOnClickListener({
            finish()
        })
    }
}
