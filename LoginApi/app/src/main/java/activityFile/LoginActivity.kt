package activityFile

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapi.*
import com.example.loginapi.databinding.ActivityLoginBinding
import common.CommonValidation
import common.KeyBordHide
import constFile.*
import modal.LoginResponse
import retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sharepref.SharePreference

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var sharePreference: SharePreference
    private lateinit var commonFun: CommonValidation
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        commonFun = CommonValidation(this)
        sharePreference = SharePreference(this)
        setContentView(binding.root)
        hideBar()
        initView()
    }

    private fun initView() {
        binding.apply {
            tvSignup.setOnClickListener {
                val i = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(i)
            }
            btnLogin.setOnClickListener { Validation() }
        }
    }

    private fun Validation() {

        val loginEmail = binding.edtLoginEmail.text.toString()
        val loginPass = binding.edtLoginPassword.text.toString()

        binding.TfLoginEmail.error = null
        binding.TfPassword.error = null

        if (loginEmail.isEmpty()) {
            binding.TfLoginEmail.error = getString(R.string.errorEmail)

        } else if (!loginEmail.matches(emailValide.toRegex())) {

            binding.TfLoginEmail.error = getString(R.string.invalidEmail)
        } else if (loginPass.isEmpty()) {
            binding.TfPassword.error = getString(R.string.passEmpty)

        } else if (loginPass.length < 6) {
            binding.TfPassword.error = getString(R.string.passError)

        } else {
            if (commonFun.isNetworkAvailable(this)) {
                userLogin(loginEmail, loginPass)
            } else {
                Toast.makeText(this, "please check internet connectivity", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun clearEditTextDataWhenSuccess() {
        binding.edtLoginEmail.text?.clear()
        binding.edtLoginPassword.text?.clear()
    }

    private fun userLogin(loginEmail: String, loginPass: String) {

        progressDialog.setMessage(getString(R.string.Loading))
        progressDialog.setCancelable(false)
        progressDialog.show()

        val hashMap:HashMap<String,String> = HashMap()
        hashMap.apply {
            put(EMAIL,loginEmail)
            put(PASSWORD,loginPass)
        }

        RetrofitInstance.apiInterface.loginUser(hashMap)
            .enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>, response: Response<LoginResponse?>
                ) {
                    progressDialog.dismiss()
                    val data: LoginResponse.Data = response.body()!!.data
                    val result = response.body()!!
                    if (result.status) {
                        clearEditTextDataWhenSuccess()
                        Log.e("TAG", "onResponse: " + response.body()!!.data)
                        val id = data.id
                        val name = data.name
                        val email = data.email

                        Log.e("TAG", "onResponse: " + response.body()!!.data.id)
                        Log.e("TAG", "onResponse: " + response.body()!!.data.email)
                        Log.e("TAG", "onResponse: " + response.body()!!.data.name)
                        sharePreference.setPrefInt(ID_KEY, id)
                        sharePreference.setPrefString(EMAIL_KEY, email)
                        sharePreference.setPrefString(USER_KEY, name)

                        Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_SHORT)
                            .show()
                        KeyBordHide().hideSoftKeyBoard(this@LoginActivity)
                    }
                    KeyBordHide().hideSoftKeyBoard(this@LoginActivity)
                    commonFun.dialog(
                        this@LoginActivity,
                        result.status,
                        result.message,
                        2,
                        getString(R.string.title)
                    )

                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "fail", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun hideBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.statusBarColor = Color.WHITE
        supportActionBar?.hide()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
}