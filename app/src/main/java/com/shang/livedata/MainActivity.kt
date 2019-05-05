package com.shang.livedata


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.DateConverter
import com.shang.livedata.ViewModel.DataViewModel
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.activity_main.cal
import kotlinx.android.synthetic.main.nest_layout.*

import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var model: DataViewModel
    lateinit var dataAdapter: DataAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview.layoutManager = LinearLayoutManager(this)
        dataAdapter = DataAdapter()
        dataAdapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener {
            override fun onItemClick(dataEntity: DataEntity) {
                //model.update(dataEntity)
                toast(dataEntity.calendarDay.toString())
            }
        })
        recyclerview.adapter = dataAdapter

        var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.delete(dataAdapter.getDataAt(viewHolder.position))
                toast("delete : ${viewHolder.position}")
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerview)

        model = ViewModelProviders.of(this).get(DataViewModel::class.java)
        model.getAllDataEntity().observe(this, object : Observer<MutableList<DataEntity>> {
            override fun onChanged(t: MutableList<DataEntity>) {
                dataAdapter.submitList(t)
            }
        })



        //button.setOnClickListener {
            //AddDialog.getInstance().show(supportFragmentManager,AddDialog.TAG)
            //startActivity(Intent(this,CalendarViewActivity::class.java))
            //startActivity(Intent(this,ChoiceModeActivity::class.java))
        //}

        model.getFirebaseLiveData().observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                toast(t.toString())
            }
        })

        cal.setTitleFormatter { day ->
            val buffer = StringBuffer()
            val yearOne = day.year
            val monthOne = day.month
            buffer.append(yearOne).append("年").append(monthOne).append("月")
            buffer
        }

    }
}

