package com.shang.livedata.Dialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.ViewModel.DataViewModel
import kotlinx.android.synthetic.main.dialog_add.*
import java.util.*


class AddDialog : DialogFragment() {

    companion object {
        val TAG: String = "AddDialog"
        private var addDialog: AddDialog? = null

        fun getInstance(): AddDialog {
            if (addDialog == null) {
                addDialog = AddDialog()
            }
            return addDialog!!
        }
    }

    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //<item name="android:colorBackground">@android:color/transparent</item>
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_add, container, false)
        var eventEt = view.findViewById<EditText>(R.id.eventEt)
        var timeEt = view.findViewById<EditText>(R.id.timeEt)
        var nameEt = view.findViewById<EditText>(R.id.settingNameEt)
        var colorSp = view.findViewById<Spinner>(R.id.colorSp)
        var addBt = view.findViewById<Button>(R.id.addBt)

        colorSp.adapter= ColorSpinnerAdapter(context!!)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dataEntity = DataEntity().apply {
            this.color = 1
            this.event = "TEST"
            this.status = false
            this.name = "NAME"
        }

        timeEt.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == 0) {  //0=點下去 1=按起來 用click的話會彈出鍵盤
                TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->

                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    true
                ).show()
            }
            true
        }

        addBt.setOnClickListener {
            dataViewModel.insert(dataEntity)
        }

        colorSp.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>, p1: View?, position: Int, id: Long) {
                var colorArray=context?.resources?.getIntArray(R.array.colorArray)
                var colorName=context?.resources?.getStringArray(R.array.colorName)

            }
        }

    }

}