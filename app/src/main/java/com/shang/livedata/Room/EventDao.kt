package com.shang.livedata.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prolificinteractive.materialcalendarview.CalendarDay

@Dao
interface EventDao {

    @Query("SELECT * FROM " + DataEntity.TABLE_NAME)
    fun getMyDataEntity(): LiveData<MutableList<DataEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(myDataEntity: DataEntity)

    @Delete
    fun delete(myDataEntity: DataEntity)

    @Update
    fun update(myDataEntity: DataEntity)

    @Query("SELECT * FROM " + DataEntity.TABLE_NAME + " where calendarDay=:calendarDay")
    fun getDay(calendarDay: CalendarDay): LiveData<MutableList<DataEntity>>

    @Query("SELECT * FROM " + DataEntity.TABLE_NAME + " where firebaseCode=:firebaseCode")
    fun checkFirebaseCode(firebaseCode: String): Boolean

    @Query("SELECT id FROM " + DataEntity.TABLE_NAME + " where firebaseCode=:firebaseCode")
    fun getFirebaseCodeToId(firebaseCode: String): Int


    //SettingTable
    @Query("SELECT * FROM " + SettingEntity.TABLE_NAME)
    fun getSetting():SettingEntity

    @Insert
    fun insertSetting(settingEntity: SettingEntity)

}