package com.shang.livedata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class SettingDialog : DialogFragment() {

    companion object{
        val TAG="SettingDialog"
        private var settingDialog:SettingDialog?=null
        fun getInstance():SettingDialog{
            if(settingDialog==null){
                settingDialog= SettingDialog()
            }
            return settingDialog as SettingDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.dialog_setting,container,false)

        return view
    }
}