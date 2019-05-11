package com.shang.livedata.Main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.ViewModel.DataViewModel
import org.jetbrains.anko.toast

class DataAdapter : ListAdapter<DataEntity, DataAdapter.ViewHolder>(DIFF_CALLBACK) {

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
        holder.idTv.text = dataEntity.id.toString()
        holder.nameTv.text=dataEntity.firebaseCode+"\n"+dataEntity.event
            holder.itemView.setOnClickListener {
            if (listener != null)
                listener.onItemClick(getDataAt(position))
        }
    }

    fun getDataAt(position: Int): DataEntity {
        return getItem(position)
    }

    public interface OnItemClickListener {
        fun onItemClick(dataEntity: DataEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idTv = itemView.findViewById<TextView>(R.id.idTv)
        var nameTv = itemView.findViewById<TextView>(R.id.nameTv)
    }

    fun getSimpleCallback(model:DataViewModel):ItemTouchHelper.SimpleCallback{
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

}


