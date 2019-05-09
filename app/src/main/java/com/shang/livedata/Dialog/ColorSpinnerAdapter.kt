package com.shang.livedata.Dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.shang.livedata.R
import org.jetbrains.anko.layoutInflater

class ColorSpinnerAdapter : BaseAdapter {
    private var colorList = intArrayOf()
    private var nameList = arrayOf<String>()
    private lateinit var inflater: LayoutInflater

    constructor(context: Context) : super() {
        colorList = context.resources.getIntArray(R.array.colorArray)
        nameList = context.resources.getStringArray(R.array.colorName)
        inflater=context.layoutInflater
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view=inflater.inflate(R.layout.spinner_item,parent,false)
        var colorNameTv=view.findViewById<TextView>(R.id.colorNameTv)
        var colorImg=view.findViewById<ImageView>(R.id.colorImg)

        colorNameTv.setText(nameList[position])
        colorImg.setColorFilter(colorList[position])
        return view

    }

    override fun getItem(p0: Int): Any? {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return colorList.size
    }


}