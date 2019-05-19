package com.shang.livedata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_family.*

class FamilyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family)
        setSupportActionBar(addTb)

    }

}
