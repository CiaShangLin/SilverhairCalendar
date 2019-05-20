package com.shang.livedata.Main


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.Dialog.AddDialog
import com.shang.livedata.Dialog.EditDialog
import com.shang.livedata.Dialog.SettingDialog
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.DateConverter
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import com.shang.livedata.ViewModel.FirebaseViewModel

import kotlinx.android.synthetic.main.activity_main.calendarView
import kotlinx.android.synthetic.main.nest_layout.*
import java.sql.Time
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var dataViewModel: DataViewModel
    lateinit var firebaseViewModel: FirebaseViewModel
    lateinit var dataAdapter: DataAdapter
    private var settingCallback = object : SettingDialog.Callback {
        override fun callback() {
            initModel()
        }
    }
    private var type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        type = intent.getIntExtra(ChoiceModeActivity.TYPE, 1)

        //CalendarView
        calendarView.setTitleFormatter { day ->
            StringBuffer().append(day.year).append("年").append(day.month).append("月")
        }
        calendarView.setSelectedDate(CalendarDay.today())

        calendarView.setOnDateChangedListener { widget, date, selected ->
            dataViewModel.currentDate.value = date
        }
        calendarView.addDecorator(TodayView(this@MainActivity))


        //RecyclerView
        recyclerview.layoutManager = LinearLayoutManager(this)
        dataAdapter = DataAdapter(this)
        dataAdapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener {
            override fun onItemClick(dataEntity: DataEntity) {
                EditDialog.getInstance(type, dataEntity).show(supportFragmentManager, EditDialog.TAG)
            }
        })
        recyclerview.adapter = dataAdapter

        //appBarLayout
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, state: Int) {
                if (state == 0) {
                    calendarView.visibility = View.VISIBLE
                } else {
                    calendarView.visibility = View.INVISIBLE
                }
            }
        })

        //Toolbar
        mainTb.inflateMenu(R.menu.menu_main)
        mainTb.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_seting -> {
                    SettingDialog.getInstance(settingCallback).show(supportFragmentManager, SettingDialog.TAG)
                }
                R.id.menu_add -> {
                    AddDialog.getInstance(type, calendarView.selectedDate!!).show(supportFragmentManager, AddDialog.TAG)
                }
            }
            true
        }

        floatingActionButton.setOnClickListener {
            AddDialog.getInstance(type, calendarView.selectedDate!!).show(supportFragmentManager, AddDialog.TAG)
        }

        when (type) {
            ChoiceModeActivity.MainActivityMode -> {
                initModel()
            }
            ChoiceModeActivity.FamilyActivityMode -> {
                if (dataViewModel.getSetting() == null) {
                    SettingDialog.getInstance(settingCallback).show(supportFragmentManager, SettingDialog.TAG)
                    Log.d("TAG", "沒有Setting")
                } else {
                    initModel()
                }
            }
        }
    }

    private fun initModel() {

        firebaseViewModel = ViewModelProviders.of(this).get(FirebaseViewModel::class.java)
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        //當點擊日曆日期 通知更新
        dataViewModel.currentDate.observe(this, object : Observer<CalendarDay> {
            override fun onChanged(t: CalendarDay?) {
                dataViewModel.getDay(t!!).observe(this@MainActivity,
                    Observer<MutableList<DataEntity>> { data ->
                        Log.d(TAG, "onclick calendarView ")
                        dataAdapter.submitList(data)
                    }
                )
            }
        })
        dataViewModel.currentDate.value = calendarView.selectedDate

        //Room
        dataViewModel.getAllDataEntity().observe(this, object : Observer<MutableList<DataEntity>> {
            override fun onChanged(data: MutableList<DataEntity>) {
                //清除後再增加　不然會重複蓋上去
                var myDayView = MyDayView(
                    this@MainActivity,
                    data.map { it.calendarDay }.toHashSet(),
                    resources.getColor(R.color.blue),
                    resources.getDrawable(R.drawable.ic_ellipsis)
                )
                calendarView.removeDecorator(myDayView)
                calendarView.addDecorator(myDayView)

                //更新ＲｅｃｙｃｌｅｒＶｉｅｗ
                dataAdapter.submitList(data.filter { it.calendarDay == calendarView.selectedDate })
                Log.d(TAG,"submitList")
            }
        })

        //Firebase
        if (dataViewModel.getSetting() != null) {
            firebaseViewModel.getFirebaseLiveData().observe(this, object : Observer<String> {
                override fun onChanged(reslut: String?) {
                    toast(reslut.toString())
                    //dataAdapter.notifyDataSetChanged()
                }
            })
        }

        //Recyclerview
        //ItemTouchHelper(dataAdapter.getSimpleCallback(dataViewModel)).attachToRecyclerView(recyclerview)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun alarm() {
        var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent()
        var pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC, Date().time, null)
    }

}

