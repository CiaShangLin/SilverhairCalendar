package com.shang.livedata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.shang.livedata.Room.DataEntity

class DataAdapter2 : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var dataList: MutableList<DataEntity> = mutableListOf()
    private var emptyView:View?=null
    private val ITEM_TYPE_EMPTY:Int=0
    private val ITEM_TYPE_MORMAL:Int=1

    fun setData(dataList: MutableList<DataEntity>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(emptyView!=null && viewType==ITEM_TYPE_EMPTY)
            return ViewHolder(emptyView!!)

        var view = LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(emptyView!=null && dataList.size==0)
            return 1
        return dataList.size
    }


    override fun getItemViewType(position: Int): Int {
        if(emptyView!=null && position==0)
            return ITEM_TYPE_EMPTY
        return ITEM_TYPE_MORMAL
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eventTv = itemView.findViewById<TextView>(R.id.eventTv)
        var timeTv = itemView.findViewById<TextView>(R.id.timeTv)
        var nameTv = itemView.findViewById<TextView>(R.id.nameTv)
        var typeTv = itemView.findViewById<TextView>(R.id.typeTv)
    }

}