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
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.shang.livedata.R
import com.shang.livedata.Room.SettingEntity
import com.shang.livedata.ViewModel.DataViewModel
import org.jetbrains.anko.toast


class ChoiceModeActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var model: DataViewModel
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1

    companion object {
        val TYPE: String = "TYPE"
        val MainActivityType: Int = 1
        val FamilyActivityType: Int = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_mode)

        familyImgBt.setOnClickListener(this)
        avatarImgBt.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.avatarImgBt -> {
                firebaseAuthRegister(MainActivityType)
            }
            R.id.familyImgBt -> {
                firebaseAuthRegister(FamilyActivityType)
            }
        }
    }

    private fun firebaseAuthRegister(type: Int) {
        when (type) {
            MainActivityType -> {
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
                    startActivity(Intent(this@ChoiceModeActivity, MainActivityType::class.java).apply {
                        this.putExtra(TYPE, MainActivityType)
                    })
                }
            }
            FamilyActivityType -> {
                startActivity(Intent(this@ChoiceModeActivity, FamilyActivityType::class.java).apply {
                    this.putExtra(TYPE, FamilyActivityType)
                })
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                var user = FirebaseAuth.getInstance().currentUser
                user.let {
                    model.insertSetting(SettingEntity().apply {
                        this.firebaseCode = it?.uid.toString()
                        this.name = it?.displayName.toString()
                    })
                }
                startActivity(Intent(this@ChoiceModeActivity, MainActivityType::class.java).apply {
                    this.putExtra(TYPE, MainActivityType)
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
