package activityFile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import constFile.ID_KEY
import com.example.loginapi.R
import com.example.loginapi.databinding.ActivityMainBinding
import common.CommonValidation
import modal.ProfileModal
import retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sharepref.SharePreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharePreference: SharePreference
    private lateinit var commonFun: CommonValidation

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharePreference = SharePreference(this)
        commonFun = CommonValidation(this)
        setContentView(binding.root)
        val id = sharePreference.getPrefInteger(ID_KEY, "")
        checkProfile(id)
    }

    private fun checkProfile(id: Int?) {
        Log.e("id", "$id")
        RetrofitInstance.apiInterface.profileUser(id!!).enqueue(object : Callback<ProfileModal?> {
            override fun onResponse(call: Call<ProfileModal?>, response: Response<ProfileModal?>) {
                val profileResult: ProfileModal.ProfileData = response.body()!!.data
                Log.e("ss", " ${profileResult.id}")
                Log.e("ss", " ${profileResult.name}")
                Log.e("ss", " ${profileResult.email}")
                binding.tvId.text = profileResult.id
                binding.tvEmail.text = profileResult.name
                binding.tvPassword.text = profileResult.email
            }

            override fun onFailure(call: Call<ProfileModal?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "error" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.LogOut -> {
                commonFun.dialogWithTwoButton(
                    this@MainActivity,
                    getString(R.string.dataClear),
                    getString(R.string.areyouwanttologout),
                    1
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}