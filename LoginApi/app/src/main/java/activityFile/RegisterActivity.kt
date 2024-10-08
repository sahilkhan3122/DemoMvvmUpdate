package activityFile

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.loginapi.R
import com.example.loginapi.databinding.ActivityRegisterBinding
import constFile.emailValide
import common.CommonValidation
import common.KeyBordHide
import constFile.EMAIL
import constFile.NAME
import constFile.PASSWORD
import modal.DefaultResponse
import retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("CAST_NEVER_SUCCEEDS", "DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var commonFun: CommonValidation
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideBar()
        commonFun = CommonValidation(this)
        progressDialog = ProgressDialog(this)
        binding.TvLogin.setOnClickListener { goToLogin() }
        binding.btnSignUp.setOnClickListener {
            validation()
        }
    }

    private fun hideBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.statusBarColor = Color.WHITE
        supportActionBar?.hide()
    }

    private fun validation() {
        val fullName = binding.edtFullName.text.toString()
        val email = binding.edtLoginEmail.text.toString()
        val password = binding.edtLoginPassword.text.toString()

        // on below line we are initializing our shared preferences
        binding.TfFullName.error = null
        binding.Tfemail.error = null
        binding.TfPassword.error = null

        if (fullName.isEmpty()) {
            binding.TfFullName.error = getString(R.string.emptyName)

        } else if (email.isEmpty()) {
            binding.Tfemail.error = getString(R.string.errorEmail)

        } else if (!email.matches(emailValide.toRegex())) {

            binding.Tfemail.error = getString(R.string.invalidEmail)
        } else if (password.isEmpty()) {
            binding.TfPassword.error = getString(R.string.passEmpty)

        } else if (password.length < 6) {
            binding.TfPassword.error = getString(R.string.passError)

        } else {
            if (commonFun.isNetworkAvailable(this)) {
                checkApiData(fullName, email, password)
            } else {
                Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkApiData(fullName: String, email: String, password: String) {
        progressDialog.setMessage(getString(R.string.LoadingData))
        progressDialog.setCancelable(false)
        progressDialog.show()

        val hashMap:HashMap<String,String> = HashMap()
        hashMap.apply {
            put(EMAIL,email)
            put(PASSWORD,password)
            put(NAME,fullName)
        }
        RetrofitInstance.apiInterface.createUser(hashMap)
            .enqueue(object : Callback<DefaultResponse?> {
                override fun onResponse(
                    call: Call<DefaultResponse?>, response: Response<DefaultResponse?>
                ) {
                    progressDialog.dismiss()
                    val myData = response.body()!!

                    if (myData.status) {
                        clearEditTextWhenRegister()
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.status) + myData.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        KeyBordHide().hideSoftKeyBoard(this@RegisterActivity)
                        commonFun.dialog(
                            this@RegisterActivity,
                            myData.status,
                            myData.message,
                            1,
                            getString(R.string.registerDialog)
                        )
                    } else {
                        commonFun.dialog(
                            this@RegisterActivity,
                            myData.status,
                            myData.message,
                            5,
                            getString(R.string.error)
                        )
                    }

                }

                override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@RegisterActivity, "error" + t.message, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun clearEditTextWhenRegister() {
        binding.edtFullName.text?.clear()
        binding.edtLoginEmail.text?.clear()
        binding.edtLoginPassword.text?.clear()
    }

    private fun goToLogin() {
        val i = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(i)
    }
}