package com.shang.livedata.Dialog

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.MyBroadcastReceiver
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.DateConverter
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import com.shang.livedata.ViewModel.FirebaseViewModel
import kotlinx.android.synthetic.main.dialog_add.*
import org.jetbrains.anko.support.v4.toast
import java.util.*


class AddDialog : DialogFragment(), View.OnClickListener, View.OnTouchListener {


    companion object {
        val TAG: String = "AddDialog"
        private var addDialog: AddDialog? = null
        private val TIME: String = "TIME"
        private val TYPE: String = "TYPE"

        fun getInstance(type: Int, calendar: CalendarDay): AddDialog {
            if (addDialog == null) {
                addDialog = AddDialog()
            }
            addDialog?.arguments = Bundle().apply {
                this?.putString(TIME, DateConverter.calendarDayToString(calendar))
                this?.putInt(TYPE, type)
            }
            return addDialog as AddDialog
        }
    }

    private lateinit var dataViewModel: DataViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var settingEntity: SettingEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //<item name="android:colorBackground">@android:color/transparent</item>
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)
        firebaseViewModel = ViewModelProviders.of(activity!!).get(FirebaseViewModel::class.java)
        settingEntity = dataViewModel.getSetting()
        settingNameEt.setText(settingEntity.name)

        dataViewModel.myBroadCastReceiver.observe(this, androidx.lifecycle.Observer {

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_add, container, false)
        view.findViewById<EditText>(R.id.eventEt)
        view.findViewById<EditText>(R.id.timeEt)
        view.findViewById<EditText>(R.id.settingNameEt)
        view.findViewById<Button>(R.id.addBt)
        var colorSp = view.findViewById<Spinner>(R.id.colorSp)
        colorSp.adapter = ColorSpinnerAdapter(context!!)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeEt.setOnTouchListener(this)
        addBt.setOnClickListener(this)
    }

    private fun getTime(timeEt: String, type: Int): Int {
        val timeSp = timeEt.split(":")
        return timeSp[type].toInt()
    }

    override fun onClick(p0: View?) {
        var type = arguments?.getInt(TYPE)
        var calendarString = arguments?.getString(TIME)
        var dataEntity = DataEntity()
        with(dataEntity) {
            this.event = eventEt.text.toString()
            this.type = colorSp.selectedItemPosition
            this.hour = getTime(timeEt.text.toString(), 0)
            this.minute = getTime(timeEt.text.toString(), 1)
            this.calendarDay = DateConverter.stringToCalendarDay(calendarString!!)
            this.calendarDayString = calendarString!!
            this.firebaseCode = settingEntity.firebaseCode
            this.name = settingNameEt.text.toString()
        }

        when (type) {
            ChoiceModeActivity.MainActivityMode -> {
                toast("新增本地成功")
                dataViewModel.insert(dataEntity)
                setTimeClock(dataEntity)
            }
            ChoiceModeActivity.FamilyActivityMode -> {
                toast("新增遠端成功")
                firebaseViewModel.pushFirebase(dataEntity)
            }
        }

        dismiss()
    }

    override fun onTouch(p0: View?, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == 0) {  //0=點下去 1=按起來 用click的話會彈出鍵盤
            TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                    timeEt.setText("$hourOfDay:$minute")
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
            ).show()
        }
        return true
    }


    //設定提醒
    fun setTimeClock(dataEntity: DataEntity) {
        var alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(context, MyBroadcastReceiver::class.java).apply {
            this.action = MyBroadcastReceiver.ACTION
            this.putExtra(MyBroadcastReceiver.TITLE, dataEntity.name)
            this.putExtra(MyBroadcastReceiver.CONTENT, dataEntity.event)
        }
        var pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC, Date().time, pendingIntent)
    }

}