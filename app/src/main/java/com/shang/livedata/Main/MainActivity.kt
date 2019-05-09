package com.shang.livedata.Main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

import kotlinx.android.synthetic.main.activity_main.cal
import kotlinx.android.synthetic.main.nest_layout.*


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



        button.setOnClickListener {
            //AddDialog.getInstance().show(supportFragmentManager,AddDialog.TAG)
            //SettingDialog.getInstance().show(supportFragmentManager, SettingDialog.TAG)

            startActivity(Intent(this, ChoiceModeActivity::class.java))
        }

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

        appBarLayout.addOnOffsetChangedListener(object :AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, state: Int) {
                if(state==0){
                    cal.visibility= View.VISIBLE
                }else{
                    cal.visibility=View.INVISIBLE
                }
            }
        })


        var set = hashSetOf<CalendarDay>().apply {
            this.add(CalendarDay.from(2019, 4, 19))
            this.add(CalendarDay.from(2019, 4, 10))
            this.add(CalendarDay.from(2019, 4, 5))
        }
        cal.addDecorator(
            MyDayView(
                this,
                set,
                resources.getColor(R.color.primary_dark_material_dark),
                resources.getDrawable(R.drawable.ic_ellipsis)
            )
        )

        mainTb.inflateMenu(R.menu.menu_main)
        mainTb.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_seting ->{
                    SettingDialog.getInstance().show(supportFragmentManager,
                        SettingDialog.TAG)
                }
            }
            true
        }
        //insertSetting()
    }

    fun insertSetting(){
        var settingEntity=SettingEntity()
        settingEntity.apply {
            this.name="ShangLin"
            this.firebaseCode="TO0GbWYZ51d4RW95HZd3boY0mv62"
        }
        model.insertSetting(settingEntity)
    }
}

