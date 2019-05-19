package com.shang.livedata.Dialog

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.shang.livedata.ChioceMode.ChoiceModeActivity
import com.shang.livedata.R
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import com.shang.livedata.ViewModel.FirebaseViewModel
import kotlinx.android.synthetic.main.dialog_add.*
import kotlinx.android.synthetic.main.dialog_edit.*
import org.jetbrains.anko.support.v4.toast

class EditDialog : DialogFragment(), androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {


    companion object {
        val TAG: String = "EditDialog"
        private val TYPE = "TYPE"
        private val DATA = "DATA"
        private var editDialog: EditDialog? = null
        fun getInstance(type: Int, dataEntity: DataEntity): EditDialog {
            if (editDialog == null) {
                editDialog = EditDialog()
            }
            editDialog?.arguments = getBundle(type, dataEntity)
            return editDialog as EditDialog
        }


        private fun getBundle(type: Int, dataEntity: DataEntity): Bundle {
            var bundle = Bundle()
            bundle.putParcelable(DATA, dataEntity)
            bundle.putInt(TYPE, type)
            return bundle
        }
    }

    private lateinit var dataViewModel: DataViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)
        firebaseViewModel = ViewModelProviders.of(activity!!).get(FirebaseViewModel::class.java)

        var data = arguments?.getParcelable(DATA) as DataEntity

        editNameEt.setText(data.name)
        editEventEt.setText(data.event)
        editTimeEt.setText("${data.hour}:${data.minute}")
        editColorSp.setSelection(data.type)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.dialog_edit, container, false)
        view.findViewById<EditText>(R.id.editEventEt)
        view.findViewById<EditText>(R.id.editTimeEt)
        view.findViewById<EditText>(R.id.editNameEt)
        view.findViewById<Toolbar>(R.id.editTb)
        var colorSp = view.findViewById<Spinner>(R.id.editColorSp)
        colorSp.adapter = ColorSpinnerAdapter(context!!)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTb.inflateMenu(R.menu.menu_dialog_edit)
        editTb.setNavigationOnClickListener { dismiss() }
        editTb.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        var type = arguments?.getInt(TYPE)
        when (item.itemId) {
            R.id.menu_save -> {
                save(type, getDataEntity())
            }
            R.id.menu_delete -> {
                delete(type, getDataEntity())
            }
        }
        dismiss()
        return true

    }

    private fun getDataEntity(): DataEntity { //取得更新後的Data
        var dataEntity = arguments?.getParcelable("DATA") as DataEntity
        dataEntity = dataEntity.apply {
            this.name = editNameEt.text.toString()
            this.event = editEventEt.text.toString()
            this.hour = getTime(editTimeEt.text.toString(), 0)
            this.minute = getTime(editTimeEt.text.toString(), 1)
            this.type = editColorSp.selectedItemPosition;
        }
        return dataEntity
    }

    private fun getTime(timeEt: String, type: Int): Int {
        val timeSp = timeEt.split(":")
        return timeSp[type].toInt()
    }

    //更新Updae
    private fun save(type: Int?, dataEntity: DataEntity) {
        when (type) {
            ChoiceModeActivity.MainActivityMode -> {
                dataViewModel.update(dataEntity)
            }
            ChoiceModeActivity.FamilyActivityMode -> {
                firebaseViewModel.updateFirebase(dataEntity)
            }
        }
        toast("更新成功")
    }

    //刪除
    private fun delete(type: Int?, dataEntity: DataEntity) {
        when (type) {
            ChoiceModeActivity.MainActivityMode -> {
                dataViewModel.delete(dataEntity)
            }
            ChoiceModeActivity.FamilyActivityMode -> {
                firebaseViewModel.deleteFirebase(dataEntity)
            }
        }
        toast("刪除成功")
    }

}