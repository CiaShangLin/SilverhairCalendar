package com.shang.livedata.Main

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.ViewModel.DataViewModel
import java.time.LocalTime

class DataAdapter(var context: Context) : ListAdapter<DataEntity, DataAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private var DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataEntity>() {
            override fun areItemsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
                return false
            }

            override fun getChangePayload(oldItem: DataEntity, newItem: DataEntity): Any? {
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }

    private lateinit var listener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dataEntity = getItem(position)
        holder.eventTv.text = dataEntity.event
        holder.timeTv.text = getTime(dataEntity.hour, dataEntity.minute)
        holder.nameTv.text = dataEntity.name
        holder.typeTv.text = getTypeText(dataEntity.type)
        holder.typeTv.background = getTypeBackground(dataEntity.type)
        holder.itemView.setOnClickListener {
            if (listener != null)
                listener.onItemClick(getDataAt(position))
        }

    }

    fun getDataAt(position: Int): DataEntity {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(dataEntity: DataEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eventTv = itemView.findViewById<TextView>(R.id.eventTv)
        var timeTv = itemView.findViewById<TextView>(R.id.timeTv)
        var nameTv = itemView.findViewById<TextView>(R.id.nameTv)
        var typeTv = itemView.findViewById<TextView>(R.id.typeTv)
    }

    fun getSimpleCallback(model: DataViewModel): ItemTouchHelper.SimpleCallback {
        var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.delete(getDataAt(viewHolder.position))
            }
        }
        return simpleCallback
    }

    private fun getTypeBackground(type:Int): Drawable {
        var shape = ContextCompat.getDrawable(context, R.drawable.shape)
        shape?.setColorFilter(getTypeColor(type), PorterDuff.Mode.ADD)
        return shape!!
    }

    private fun getTypeText(type:Int):String{
        return context.resources.getStringArray(R.array.typeName)[type]
    }
    private fun getTypeColor(type:Int):Int{
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


