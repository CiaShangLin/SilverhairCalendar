package com.shang.livedata.Main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.Dialog.AddDialog
import com.shang.livedata.Dialog.SettingDialog
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import com.shang.livedata.ViewModel.FirebaseViewModel

import kotlinx.android.synthetic.main.activity_main.calendarView
import kotlinx.android.synthetic.main.nest_layout.*


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
        recyclerview.layoutManager = GridLayoutManager(this, 2)
        dataAdapter = DataAdapter()
        dataAdapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener {
            override fun onItemClick(dataEntity: DataEntity) {
                toast(calendarView.selectedDate.toString())
            }
        })
        recyclerview.adapter = dataAdapter


        saveBt.setOnClickListener {
            //AddDialog.getInstance(1,"").show(supportFragmentManager, AddDialog.TAG)
            //SettingDialog.getInstance().show(supportFragmentManager, SettingDialog.TAG)
            insertDataEntity()
            //startActivity(Intent(this, ChoiceModeActivity::class.java))
        }

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
            }
            true
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

        //當天顏色要改
    }

    private fun initModel() {

        firebaseViewModel = ViewModelProviders.of(this).get(FirebaseViewModel::class.java)
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        //當點擊日曆日期 通知更新
        dataViewModel.currentDate.observe(this, object : Observer<CalendarDay> {
            override fun onChanged(t: CalendarDay?) {
                dataViewModel.getDay(t!!).observe(this@MainActivity,
                    Observer<MutableList<DataEntity>> { data ->
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
            }
        })

        //Firebase
        if (dataViewModel.getSetting() != null) {
            firebaseViewModel.getFirebaseLiveData().observe(this, object : Observer<String> {
                override fun onChanged(reslut: String?) {
                    toast(reslut.toString())
                }
            })
        }

        //Recyclerview
        ItemTouchHelper(dataAdapter.getSimpleCallback(dataViewModel)).attachToRecyclerView(recyclerview)
    }

    fun insertFirebase() {
        var setting = dataViewModel.getSetting()
        for (i in 1..5) {
            var dataEntity = DataEntity().apply {
                this.event = "今天要吃藥"
                this.calendarDay = CalendarDay.from(2019, 5, 12)
                this.calendarDayString = calendarDayToString(calendarDay)
                this.hour = i
                this.minute = i
                this.color = R.color.blue
                this.firebaseCode = setting.firebaseCode
                this.name = setting.name
            }
            firebaseViewModel.pushFirebase(dataEntity)
        }

    }

    fun insertSetting() {
        var settingEntity = SettingEntity()
        settingEntity.apply {
            this.name = "ShangLin"
            this.firebaseCode = "TO0GbWYZ51d4RW95HZd3boY0mv62"
        }
        dataViewModel.insertSetting(settingEntity)
    }

    fun insertDataEntity() {
        var setting = dataViewModel.getSetting()
        for (i in 1..3) {
            var dataEntity = DataEntity().apply {
                this.event = "小翠出來玩"
                this.calendarDay = CalendarDay.from(2019, 5, 13)
                this.calendarDayString = calendarDayToString(calendarDay)
                this.hour = i
                this.minute = i
                this.color = R.color.blue
                this.firebaseCode = setting.firebaseCode
                this.name = setting.name
            }
            dataViewModel.insert(dataEntity)
        }
    }

}

