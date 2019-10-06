package com.example.ass8mysqlqueryinsert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_insert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
    }

    fun  AddEmployee(v: View){

        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
        val api : EmployeeAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmployeeAPI ::class.java)

        api.insertEmp(
            edit_name.text.toString(),
            radio.text.toString(),
            edit_email.text.toString(),
            edit_salary.text.toString().toInt()).enqueue(object : Callback<Employee> {

            override fun onResponse(call: Call<Employee>, response: retrofit2.Response<Employee>) {
                if(response.isSuccessful()) {
                    Toast.makeText(applicationContext, "Successfully Inserted", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Error ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Employee>, t: Throwable) {
                Toast.makeText(applicationContext, "Error onFailure " + t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    fun Cancel(v:View){
        finish()
    }
}
