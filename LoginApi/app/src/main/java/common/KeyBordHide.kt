package common

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class KeyBordHide {
    fun hideSoftKeyBoard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(activity.currentFocus!!.getWindowToken(), 0)
}
}