package com.shang.livedata.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Firebase.FirebaseData
import com.shang.livedata.Firebase.FirebaseLiveData
import com.shang.livedata.Repository.DataRepository
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.SettingEntity

class DataViewModel : AndroidViewModel {
    private lateinit var repository: DataRepository
    var currentDate: MutableLiveData<CalendarDay> = MutableLiveData()

    constructor(application: Application) : super(application) {
        repository = DataRepository(application)
    }

    //EventDao
    fun getAllDataEntity(): LiveData<MutableList<DataEntity>> {
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

    fun getDay(calendarDay: CalendarDay): LiveData<MutableList<DataEntity>> {
        return repository.getDay(calendarDay)
    }

    //Setting
    fun insertSetting(settingEntity: SettingEntity) {
        repository.insertSetting(settingEntity)
    }

    fun getSetting(): SettingEntity {
        return repository.getSetting()
    }

    fun updateSetting(settingEntity: SettingEntity) {
        repository.updateSetting(settingEntity)
    }

    //Firebase
    fun firebaseDao(firebaseData:FirebaseData):String{
        return repository.firebaseDao(firebaseData)
    }



}