package activityFile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.loginapi.R
import constFile.USER_LOGIN
import sharepref.SharePreference

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var sharePreference: SharePreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePreference = SharePreference(this)
        setContentView(R.layout.activity_splash)
        fullScreen()
        timeForScreen()
    }

    private fun timeForScreen() {
        Handler(Looper.getMainLooper()).postDelayed({

            val hasLoggedIn = sharePreference.getPrefBoolean(USER_LOGIN)
            if (!hasLoggedIn) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }

    private fun fullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.statusBarColor = Color.WHITE
        supportActionBar?.hide()
    }
}