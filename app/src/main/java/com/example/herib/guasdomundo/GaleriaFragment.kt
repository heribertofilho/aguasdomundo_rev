package com.example.herib.guasdomundo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.galeria_layout.view.*


/**
 * Created by herib on 03/06/2017.
 */

class GaleriaFragment : Fragment() {
    var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.galeria_layout, container, false)
        view.imgClose.setOnClickListener({
            activity.finish()
        })
//        view.recyclerView.setHasFixedSize(true)
//        layoutManager = GridLayoutManager(activity.applicationContext, 3)
//        view.recyclerView.layoutManager = layoutManager
        return view
    }
}