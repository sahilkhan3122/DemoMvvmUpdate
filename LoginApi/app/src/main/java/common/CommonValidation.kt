package common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import activityFile.LoginActivity
import activityFile.MainActivity
import android.net.ConnectivityManager
import com.example.loginapi.R
import com.google.android.material.dialog.*
import constFile.USER_LOGIN
import sharepref.SharePreference

class CommonValidation(private val activity: Activity) {
    private lateinit var sharePreference: SharePreference

    fun dialog(
        context: Context, tittle: Boolean, message: String, check: Int, dialogTittle: String
    ) {
        sharePreference = SharePreference(activity)
        val materialDialogs = MaterialAlertDialogBuilder(context)

        materialDialogs.setTitle(dialogTittle)
        materialDialogs.setMessage(message)

        materialDialogs.setPositiveButton(R.string.okk) { _, _ ->
            if (tittle) {
                if (check == 1) {

                    activity.startActivity(Intent(context, LoginActivity::class.java))
                    activity.finishAffinity()
                } else {
                    sharePreference.setPrefBoolean(USER_LOGIN, true)
                    activity.startActivity(Intent(context, MainActivity::class.java))
                    activity.finishAffinity()
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
        materialDialogs.setCancelable(false)
        materialDialogs.show()
    }

    fun dialogWithTwoButton(context: Context, dialogTittle: String, message: String, check: Int) {
        sharePreference = SharePreference(activity)
        val materialDialogs = MaterialAlertDialogBuilder(context)

        materialDialogs.setTitle(dialogTittle)
        materialDialogs.setMessage(message)

        materialDialogs.setPositiveButton(R.string.okk) { _, _ ->

            if (check == 1) {
                sharePreference.setPrefBoolean(USER_LOGIN, false)
                activity.startActivity(Intent(context, LoginActivity::class.java))
                activity.finish()
            }
        }
        materialDialogs.setNegativeButton(context.getString(R.string.dismiss)) { closeDialog, _ ->
            closeDialog.dismiss()
        }
        materialDialogs.setCancelable(false)
        materialDialogs.show()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}