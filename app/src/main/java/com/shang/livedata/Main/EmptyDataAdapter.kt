package com.shang.livedata.Main

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity

class EmptyDataAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var dataList: MutableList<DataEntity> = mutableListOf()
    private var emptyView: View? = null
    private val ITEM_TYPE_EMPTY: Int = 0
    private val ITEM_TYPE_MORMAL: Int = 1
    private lateinit var listener: OnItemClickListener
    private lateinit var colorArray: IntArray
    private lateinit var typeArray: Array<String>

    interface OnItemClickListener {
        fun onItemClick(dataEntity: DataEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(dataList: List<DataEntity>) {
        //這個會引發抱錯 前後資料不一致
        /*this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyItemRangeInserted(0, this.dataList.size)*/

        this.dataList=dataList.toMutableList()
        notifyDataSetChanged()


    }

    fun setEmptyView(view: View) {
        emptyView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (emptyView != null && viewType == ITEM_TYPE_EMPTY)
            return ViewHolder2(emptyView!!)

        var view = LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_TYPE_EMPTY)
            return
        else {
            var dataHolder = holder as ViewHolder
            var dataEntity = dataList.get(position)
            dataHolder.eventTv.text = dataEntity.event
            dataHolder.timeTv.text = getTime(dataEntity.hour, dataEntity.minute)
            dataHolder.nameTv.text = dataEntity.name
            dataHolder.typeTv.text = getTypeText(dataEntity.type)
            dataHolder.typeTv.background = getTypeBackground(dataEntity.type)
            dataHolder.itemView.setOnClickListener {
                if (listener != null)
                    listener.onItemClick(dataEntity)
            }
        }
    }

    override fun getItemCount(): Int {
        if (emptyView != null && dataList.size == 0)
            return 1
        return dataList.size
    }


    override fun getItemViewType(position: Int): Int {
        if (emptyView != null && dataList.size == 0)
            return ITEM_TYPE_EMPTY
        return ITEM_TYPE_MORMAL
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eventTv = itemView.findViewById<TextView>(R.id.eventTv)
        var timeTv = itemView.findViewById<TextView>(R.id.timeTv)
        var nameTv = itemView.findViewById<TextView>(R.id.nameTv)
        var typeTv = itemView.findViewById<TextView>(R.id.typeTv)
    }

    class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    private fun getTypeBackground(type: Int): Drawable {
        var shape = ContextCompat.getDrawable(context, R.drawable.shape)
        shape?.setColorFilter(getTypeColor(type), PorterDuff.Mode.SRC)
        return shape!!
    }

    private fun getTypeText(type: Int): String {
        return context.resources.getStringArray(R.array.typeName)[type]
    }

    private fun getTypeColor(type: Int): Int {
        return context.resources.getIntArray(R.array.colorArray)[type]
    }

    private fun getTime(hour: Int, minute: Int): String {
        var stringBuffer = StringBuffer("")
        stringBuffer.append(if (hour >= 10) hour else "0$hour")
            .append(":")
            .append(if (minute >= 10) minute else "0$minute")

        return stringBuffer.toString()
    }

}