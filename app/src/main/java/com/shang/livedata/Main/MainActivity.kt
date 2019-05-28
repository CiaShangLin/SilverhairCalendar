package com.shang.livedata.Main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.Dialog.AddDialog
import com.shang.livedata.Dialog.EditDialog
import com.shang.livedata.Dialog.SettingDialog
import com.shang.livedata.Firebase.FirebaseData
import com.shang.livedata.R
import com.shang.livedata.Repository.DataRepository
import com.shang.livedata.Room.DataEntity
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
    private lateinit var emptyDataAdapter: EmptyDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        type = intent.getIntExtra(ChoiceModeActivity.TYPE, 1)

        initView()
        when (type) {
            ChoiceModeActivity.MainActivityMode -> {
                initModel()
            }
            ChoiceModeActivity.FamilyActivityMode -> {
                if (dataViewModel.getSetting() == null) {
                    SettingDialog.getInstance(settingCallback).show(supportFragmentManager, SettingDialog.TAG)
                    Log.v(TAG, "沒有Setting")
                } else {
                    initModel()
                }
            }
        }
    }

    private fun initView() {
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
        emptyDataAdapter = EmptyDataAdapter(this)
        emptyDataAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.nothing_item, recyclerview, false))
        emptyDataAdapter.setData(mutableListOf())
        emptyDataAdapter.setOnItemClickListener(object : EmptyDataAdapter.OnItemClickListener {
            override fun onItemClick(dataEntity: DataEntity) {
                EditDialog.getInstance(type, dataEntity).show(supportFragmentManager, EditDialog.TAG)
            }
        })
        recyclerview.adapter = emptyDataAdapter


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
    }

    private fun initModel() {

        firebaseViewModel = ViewModelProviders.of(this).get(FirebaseViewModel::class.java)
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        //當點擊日曆日期 通知更新
        dataViewModel.currentDate.observe(this, object : Observer<CalendarDay> {
            override fun onChanged(t: CalendarDay?) {
                Log.v(TAG, "onclick calendarView ")
                dataViewModel.getDay(t!!).observe(this@MainActivity,
                    Observer<MutableList<DataEntity>> { data ->
                        //dataAdapter.submitList(data)
                        emptyDataAdapter.setData(data)
                    }
                )
            }
        })

        //Room
        dataViewModel.getAllDataEntity().observe(this@MainActivity, object : Observer<MutableList<DataEntity>> {
            override fun onChanged(data: MutableList<DataEntity>) {
                //清除後再增加　不然會重複蓋上去
                var myDayView = MyDayView(
                    this@MainActivity,
                    data.map { it.calendarDay }.toHashSet()
                )
                calendarView.removeDecorator(myDayView)
                calendarView.addDecorator(myDayView)

                //更新ＲｅｃｙｃｌｅｒＶｉｅｗ
                //dataAdapter.submitList(data.filter { it.calendarDay == calendarView.selectedDate })
                emptyDataAdapter.setData(data.filter { it.calendarDay == calendarView.selectedDate })
                Log.v(TAG, "submitList")
            }
        })

        //Firebase
        if (dataViewModel.getSetting() != null) {
            firebaseViewModel.getFirebaseLiveData().observe(this, object : Observer<FirebaseData> {
                override fun onChanged(firebaseData: FirebaseData) {
                    var result = dataViewModel.firebaseDao(firebaseData)
                    if (!result.equals(""))
                        toast(result)
                }
            })
        }

        //Recyclerview
        //ItemTouchHelper(dataAdapter.getSimpleCallback(dataViewModel)).attachToRecyclerView(recyclerview)
    }


    override fun onResume() {
        super.onResume()
        dataViewModel.currentDate.value = calendarView.selectedDate
    }
}

