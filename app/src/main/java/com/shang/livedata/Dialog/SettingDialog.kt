package com.shang.livedata.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.shang.livedata.R
import com.shang.livedata.ViewModel.DataViewModel
import kotlinx.android.synthetic.main.dialog_setting.*

class SettingDialog : DialogFragment() {

    companion object {
        val TAG = "SettingDialog"
        private var settingDialog: SettingDialog? = null
        fun getInstance(): SettingDialog {
            if (settingDialog == null) {
                settingDialog =
                    SettingDialog()
            }
            return settingDialog as SettingDialog
        }
    }

    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)

        var settingEntity=dataViewModel.getSetting()
        settingNameEt.setText(settingEntity.name)
        settingFirebaseCodeEt.setText(settingEntity.firebaseCode)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_setting, container, false)
        var settingNameEt=view.findViewById<EditText>(R.id.settingNameEt)
        var settingFirebaseCodeEt=view.findViewById<EditText>(R.id.settingFirebaseCodeEt)



        return view
    }
}