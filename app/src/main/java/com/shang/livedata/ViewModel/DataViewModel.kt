package com.shang.livedata.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Firebase.FirebaseLiveData
import com.shang.livedata.Repository.DataRepository
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.SettingEntity

class DataViewModel: AndroidViewModel{
    private lateinit var repository: DataRepository
    private lateinit var allDataEntity: LiveData<MutableList<DataEntity>>

    constructor(application: Application) : super(application) {
        repository= DataRepository(application)
        allDataEntity=repository.getAllData()
    }

    //EventDao
    fun getAllDataEntity():LiveData<MutableList<DataEntity>>{
        return repository.getAllData()
    }

    fun insert(myDataEntity: DataEntity) {
       repository.insert(myDataEntity)
    }

    fun delete(myDataEntity: DataEntity) {
        repository.delete(myDataEntity)
    }

    fun update(myDataEntity: DataEntity) {
        repository.update(myDataEntity)
    }

    fun getDay(calendarDay: CalendarDay): LiveData<MutableList<DataEntity>>{
        return repository.getDay(calendarDay)
    }



    //FirebaseDao
    fun getFirebaseLiveData(): FirebaseLiveData {
        return repository.getFirebaseLiveData()
    }

    fun pushFirebase(dataEntity: DataEntity){
        repository.pushFirebase(dataEntity)
    }

    fun updateFirebase(dataEntity: DataEntity){
        repository.updateFirebase(dataEntity)
    }


    //Setting
    fun insertSetting(settingEntity: SettingEntity){
        repository.insertSetting(settingEntity)
    }

    fun getSetting():SettingEntity{
        return repository.getSetting()
    }




}