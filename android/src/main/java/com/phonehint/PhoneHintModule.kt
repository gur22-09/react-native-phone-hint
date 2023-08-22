package com.phonehint

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import android.util.Log
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.Promise;


class PhoneHintModule(val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  private var promise: Promise? = null
  private val PHONE_SELECTOR_ERROR = "PHONE_SELECTOR_ERROR"
  override fun getName(): String {
    return NAME
  }

  private val activityEventListener =
    object : BaseActivityEventListener() {
      override fun onActivityResult(
        activity: Activity?,
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
      ) {
        when(requestCode) {
          1000 -> {
            if(resultCode == RESULT_OK) {
              try {
                if(activity != null) {
                  val phoneNumber = Identity.getSignInClient(activity).getPhoneNumberFromIntent(intent)
                  Log.d("found the phone number", phoneNumber);
                  promise!!.resolve(phoneNumber);
                }
              }catch (err: Exception) {
                  promise!!.reject(PHONE_SELECTOR_ERROR);
              }
            }
          }
        }
      }
    }
  init {
    reactContext.addActivityEventListener(activityEventListener)
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun initPhoneNumberHint(promise: Promise) {
    this.promise = promise
    val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build();

    Identity.getSignInClient(reactContext.getApplicationContext()).getPhoneNumberHintIntent(request).addOnSuccessListener {
        result: PendingIntent ->
      try {
        val intentSender = result.intentSender
        val activity = currentActivity
        activity?.startIntentSenderForResult(intentSender, 1000, null, 0, 0, 0)
      } catch (e: Exception) {
        promise.reject(PHONE_SELECTOR_ERROR)
      }
    }
  }


  companion object {
    const val NAME = "PhoneHint"
  }
}
