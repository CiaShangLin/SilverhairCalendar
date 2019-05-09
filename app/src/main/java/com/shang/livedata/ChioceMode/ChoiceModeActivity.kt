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
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.shang.livedata.Main.MainActivity
import com.shang.livedata.R
import org.jetbrains.anko.toast


class ChoiceModeActivity : AppCompatActivity(), View.OnClickListener {


    private val firebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1
    val TYPE: String = "TYPE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_mode)




        familyImgBt.setOnClickListener(this)
        avatarImgBt.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.avatarImgBt -> {
                firebaseAuthRegister(1)
            }
            R.id.familyImgBt -> {
                firebaseAuthRegister(2)
            }
        }
    }

    private fun firebaseAuthRegister(type:Int) {
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
        }else{
            gotoActivity(type)
        }
    }

    fun gotoActivity(type:Int){
        when(type){
            1->{
                startActivity(Intent(this@ChoiceModeActivity, MainActivity::class.java))
            }
            2->{
                startActivity(Intent(this@ChoiceModeActivity, MainActivity::class.java))
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.d("TAG", response?.email)
                Log.d("TAG", response?.user!!.providerId)
                var user = FirebaseAuth.getInstance().currentUser
                Log.d("TAG", "${user?.uid} ${user?.displayName}")
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
