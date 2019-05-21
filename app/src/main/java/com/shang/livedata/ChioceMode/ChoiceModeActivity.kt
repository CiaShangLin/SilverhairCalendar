package com.shang.livedata.ChioceMode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_choice_mode.*
import com.firebase.ui.auth.AuthUI
import java.util.*
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.shang.livedata.FamilyActivity
import com.shang.livedata.Main.MainActivity
import com.shang.livedata.R
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import org.jetbrains.anko.toast


class ChoiceModeActivity : AppCompatActivity(), View.OnClickListener {


    private val TAG = "ChoiceModeActivity"
    private lateinit var model: DataViewModel
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1

    companion object {
        val TYPE: String = "TYPE"
        val MainActivityMode: Int = 1
        val FamilyActivityMode: Int = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_mode)

        model = ViewModelProviders.of(this).get(DataViewModel::class.java)
        familyImgBt.setOnClickListener(this)
        avatarImgBt.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.avatarImgBt -> {
                firebaseAuthRegister(MainActivityMode)
            }
            R.id.familyImgBt -> {
                firebaseAuthRegister(FamilyActivityMode)
            }
        }
    }

    //註冊
    private fun firebaseAuthRegister(type: Int) {
        when (type) {
            MainActivityMode -> {
                if (firebaseAuth.currentUser == null) {
                    startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                Arrays.asList(
                                    AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build()
                                )
                            )
                            .build(),
                        RC_SIGN_IN
                    )
                } else {
                    startActivity(Intent(this@ChoiceModeActivity, MainActivity::class.java).apply {
                        this.putExtra(TYPE, MainActivityMode)
                    })
                }
            }
            FamilyActivityMode -> {
                startActivity(Intent(this@ChoiceModeActivity, MainActivity::class.java).apply {
                    this.putExtra(TYPE, FamilyActivityMode)
                })
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                FirebaseAuth.getInstance().currentUser.let {
                    model.insertSetting(SettingEntity().apply {
                        this.firebaseCode = it?.uid.toString()
                        this.name = it?.displayName.toString()
                        Log.d(TAG, "$firebaseCode $name")
                    })
                }
                startActivity(Intent(this@ChoiceModeActivity, MainActivity::class.java).apply {
                    this.putExtra(TYPE, MainActivityMode)
                })
            } else {
                if (response == null) {
                    toast("返回選擇畫面")
                    return
                }
                if (response.getError()?.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    toast("沒有網路Wifi")
                    return
                }
            }
        }

    }
}
