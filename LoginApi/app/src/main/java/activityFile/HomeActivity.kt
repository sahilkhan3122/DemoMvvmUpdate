package activityFile

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.loginapi.databinding.HomeActivityBinding

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    private lateinit var binding:HomeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = HomeActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideBar()
        binding.btnStarted.setOnClickListener {
            goToRegister()
        }
        binding.tvSignup.setOnClickListener {
            val i = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun goToRegister() {
        val i = Intent(this@HomeActivity, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun hideBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.statusBarColor = Color.WHITE
        supportActionBar?.hide()
    }
}