package com.shang.livedata

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_calendar_view.*

import kotlinx.android.synthetic.main.activity_choice_mode.*
import com.firebase.ui.auth.AuthUI
import java.util.*
import java.util.Arrays.asList
import android.R.attr.data
import android.annotation.SuppressLint
import android.util.Log
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse




class ChoiceModeActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_mode)
        setSupportActionBar(toolbar)


        firebaseAuthRegister()

    }

    private fun firebaseAuthRegister() {
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

        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK){
                Log.d("TAG",response?.email)
                Log.d("TAG",response?.user!!.providerId)
                var user=FirebaseAuth.getInstance().currentUser
                Log.d("TAG","${user?.uid} ${user?.displayName}")

            }else{
                if (response == null) {
                    // User pressed back button
                    return
                }

                if (response.getError()?.getErrorCode() == ErrorCodes.NO_NETWORK) {

                    return
                }

            }
        }

    }
}
