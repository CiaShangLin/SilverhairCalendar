package com.shang.livedata.Dialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.DateConverter
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import com.shang.livedata.ViewModel.FirebaseViewModel
import kotlinx.android.synthetic.main.dialog_add.*
import org.jetbrains.anko.support.v4.toast
import java.util.*


class AddDialog : DialogFragment() {

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_add, container, false)
        view.findViewById<EditText>(R.id.eventEt)
        view.findViewById<EditText>(R.id.timeEt)
        view.findViewById<EditText>(R.id.settingNameEt)
        view.findViewById<Button>(R.id.addBt)
        var colorSp=view.findViewById<Spinner>(R.id.colorSp)
        colorSp.adapter = ColorSpinnerAdapter(context!!)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var type = arguments?.getInt(TYPE)
        var calendarString = arguments?.getString(TIME)
        Log.d(TAG, "type:$type calendarString:$calendarString")

        this.timeEt.setOnTouchListener { view, motionEvent ->
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
            true
        }

        addBt.setOnClickListener {
            var dataEntity = DataEntity().apply {
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
                }
                ChoiceModeActivity.FamilyActivityMode -> {
                    toast("新增遠端成功")
                    firebaseViewModel.pushFirebase(dataEntity)
                }
            }
            dismiss()
        }
    }

    private fun getTime(timeEt: String, type: Int): Int {
        val timeSp = timeEt.split(":")
        return timeSp[type].toInt()
    }

}