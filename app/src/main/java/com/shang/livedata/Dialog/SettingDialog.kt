package com.shang.livedata.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.shang.livedata.R
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import kotlinx.android.synthetic.main.dialog_setting.*
import org.jetbrains.anko.support.v4.toast

class SettingDialog : DialogFragment() {

    companion object {
        val TAG = "SettingDialog"
        private var settingDialog: SettingDialog? = null
        private var settingCallback: SettingDialog.Callback? = null
        fun getInstance(callback: SettingDialog.Callback): SettingDialog {
            if (settingDialog == null) {
                settingDialog = SettingDialog()
            }
            this.settingCallback = callback
            return settingDialog as SettingDialog
        }
    }

    interface Callback {
        fun callback()
    }

    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)

        var settingEntity = dataViewModel.getSetting()
        if (settingEntity != null) {
            settingNameEt.setText(settingEntity.name)
            settingFirebaseCodeEt.setText(settingEntity.firebaseCode)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_setting, container, false)
        var settingNameEt = view.findViewById<EditText>(R.id.settingNameEt)
        var settingFirebaseCodeEt = view.findViewById<EditText>(R.id.settingFirebaseCodeEt)
        var saveBt = view.findViewById<Button>(R.id.saveBt)
        saveBt.setOnClickListener {
            var settingEntity = SettingEntity().apply {
                this.name = settingNameEt.text.toString()
                this.firebaseCode = settingFirebaseCodeEt.text.toString()
            }
            if (dataViewModel.getSetting() == null) {
                dataViewModel.insertSetting(settingEntity)
                toast("新增成功")
            } else {
                dataViewModel.updateSetting(settingEntity)
                toast("更新成功")
            }
            settingCallback?.callback()
        }
        return view
    }
}