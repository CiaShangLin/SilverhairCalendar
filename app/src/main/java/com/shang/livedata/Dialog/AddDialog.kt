package com.shang.livedata.Dialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import com.shang.livedata.ViewModel.FirebaseViewModel
import kotlinx.android.synthetic.main.dialog_add.*
import java.util.*


class AddDialog : DialogFragment() {

    companion object {
        val TAG: String = "AddDialog"
        private var addDialog: AddDialog? = null
        private val TIME: String = "TIME"
        private val TYPE: String = "TYPE"

        fun getInstance(type: Int, calendarString: String): AddDialog {
            if (addDialog == null) {
                addDialog = AddDialog()
            }
            addDialog?.arguments.apply {
                this?.putString(TIME, calendarString)
                this?.putInt(TYPE, type)
            }
            return addDialog as AddDialog
        }
    }

    private lateinit var dataViewModel: DataViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var settingEntity: SettingEntity
    private var hour: Int = 1
    private var minute: Int = 1

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
        var eventEt = view.findViewById<EditText>(R.id.eventEt)
        var timeEt = view.findViewById<EditText>(R.id.timeEt)
        var nameEt = view.findViewById<EditText>(R.id.settingNameEt)
        var colorSp = view.findViewById<Spinner>(R.id.colorSp)
        var addBt = view.findViewById<Button>(R.id.addBt)
        colorSp.adapter = ColorSpinnerAdapter(context!!)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var type = arguments?.getInt(TYPE)
        var calendarString = arguments?.getString(TIME)

        //
        timeEt.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == 0) {  //0=點下去 1=按起來 用click的話會彈出鍵盤
                TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                        this.hour = hourOfDay
                        this.minute = minute
                        timeEt.setText("$hour:$minute")
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
                this.color = getColorSp()
                this.hour = hour
                this.minute = minute
                this.calendarDay = this.stringToCalendarDay(calendarDayString)
                this.calendarDayString = calendarDayString
                this.firebaseCode = settingEntity.firebaseCode
                this.name = settingNameEt.text.toString()
            }
            when (type) {
                ChoiceModeActivity.MainActivityMode->{

                }
                ChoiceModeActivity.FamilyActivityMode->{

                }
                3->{

                }

            }
            dataViewModel.insert(dataEntity)
        }

        colorSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>, p1: View?, position: Int, id: Long) {


            }
        }

    }

    fun getColorSp(): Int {
        var colorArray = context?.resources?.getIntArray(R.array.colorArray)
        //var colorName=context?.resources?.getStringArray(R.array.colorName)
        var color = colorArray?.get(colorSp.selectedItemPosition)
        if (color != null) {
            return color
        }
        return R.color.blue
    }

}