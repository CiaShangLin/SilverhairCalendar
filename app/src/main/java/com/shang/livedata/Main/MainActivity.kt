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
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.Dialog.SettingDialog
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel

import kotlinx.android.synthetic.main.activity_main.calendarView
import kotlinx.android.synthetic.main.nest_layout.*


class MainActivity : AppCompatActivity() {

    lateinit var model: DataViewModel
    lateinit var dataAdapter: DataAdapter
    lateinit var currentDate:MutableLiveData<CalendarDay>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //CalendarView
        calendarView.setTitleFormatter { day ->
            StringBuffer().append(day.year).append("年").append(day.month).append("月")
        }
        calendarView.setSelectedDate(CalendarDay.today())
        Log.d("TAG",calendarView.selectedDate.toString())

        calendarView.setOnDateChangedListener { widget, date, selected ->
            currentDate.value=date
            Log.d("TAG",date.toString())
        }


        //Room
        model = ViewModelProviders.of(this).get(DataViewModel::class.java)
        model.getAllDataEntity().observe(this, object : Observer<MutableList<DataEntity>> {
            override fun onChanged(data: MutableList<DataEntity>) {
                /*dataAdapter.submitList(data.filter {
                    it.calendarDay==calendarView.selectedDate
                })*/

                calendarView.addDecorator(
                    MyDayView(
                        this@MainActivity,
                        data.map {
                            it.calendarDay
                        },
                        resources.getColor(R.color.primary_dark_material_dark),
                        resources.getDrawable(R.drawable.ic_ellipsis)
                    )
                )
            }
        })


        currentDate.observe(this,object :Observer<CalendarDay>{
            override fun onChanged(t: CalendarDay?) {
                model.getDay(t!!).observe(this@MainActivity,
                    object :Observer<MutableList<DataEntity>>{
                        override fun onChanged(t: MutableList<DataEntity>?) {
                            dataAdapter.submitList(t!!)
                        }
                    }
                )
            }
        })



        //Firebase
        model.getFirebaseLiveData().observe(this, object : Observer<String> {
            override fun onChanged(reslut: String?) {
                toast(reslut.toString())
            }
        })

        //RecyclerView
        recyclerview.layoutManager = LinearLayoutManager(this)
        dataAdapter = DataAdapter()
        dataAdapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener {
            override fun onItemClick(dataEntity: DataEntity) {
                toast(calendarView.selectedDate.toString())
            }
        })
        recyclerview.adapter = dataAdapter
        ItemTouchHelper(dataAdapter.getSimpleCallback(model)).attachToRecyclerView(recyclerview)

        button.setOnClickListener {
            //AddDialog.getInstance().show(supportFragmentManager,AddDialog.TAG)
            //SettingDialog.getInstance().show(supportFragmentManager, SettingDialog.TAG)

            startActivity(Intent(this, ChoiceModeActivity::class.java))
        }





        //appBarLayout
        appBarLayout.addOnOffsetChangedListener(object :AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, state: Int) {
                if(state==0){
                    calendarView.visibility= View.VISIBLE
                }else{
                    calendarView.visibility=View.INVISIBLE
                }
            }
        })

        //Toolbar
        mainTb.inflateMenu(R.menu.menu_main)
        mainTb.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_seting ->{
                    SettingDialog.getInstance().show(supportFragmentManager, SettingDialog.TAG)
                }
            }
            true
        }
        //insertSetting()
        insertDataEntity()
    }

    fun insertSetting(){
        var settingEntity=SettingEntity()
        settingEntity.apply {
            this.name="ShangLin"
            this.firebaseCode="TO0GbWYZ51d4RW95HZd3boY0mv62"
        }
        model.insertSetting(settingEntity)
    }

    fun insertDataEntity(){
        var setting=model.getSetting()
        for(i in 1..5){
            var dataEntity=DataEntity().apply {
                this.event="TEST $i"
                this.calendarDay=CalendarDay.from(2019,5,11)
                this.calendarDayString=calendarDayToString(calendarDay)
                this.hour=i
                this.minute=i
                this.color=R.color.blue
                this.firebaseCode=setting.firebaseCode
                this.name=setting.name
            }
            model.insert(dataEntity)
        }
    }
}

