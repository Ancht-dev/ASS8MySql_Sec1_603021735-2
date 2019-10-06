package com.example.ass8mysqlqueryinsert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val employeeList = arrayListOf<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL))

        recycler_view.addOnItemTouchListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                Toast.makeText(applicationContext, "You click on : " +employeeList[position].emp_name,
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        callStudentData()
    }

    fun addEmployee(v: View){
        val intent = Intent( this@MainActivity, InsertActivity::class.java)
        startActivity(intent)
    }

    fun  callStudentData(){
        employeeList.clear();

        val  serv : EmployeeAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmployeeAPI ::class.java)

        serv.retrieveEmployee()
            .enqueue(object : Callback<List<Employee>> {
                override fun onResponse(
                    call: Call<List<Employee>>,
                    response: Response<List<Employee>>
                ) {
                    response.body()?.forEach {
                        employeeList.add(Employee(it.emp_name, it.emp_gender, it.emp_email, it.emp_salary))
                    }

                    recycler_view.adapter = EmployeesAdapter(employeeList, applicationContext)
                    text1.text = "Student List : "+ employeeList.size.toString()+ " Students"
                }

                override fun onFailure(call: Call<List<Employee>>, t: Throwable) = t.printStackTrace()
            })
    }
}

interface  OnItemClickListener{
    fun onItemClicked(position: Int, view: View)
}
fun RecyclerView.addOnItemTouchListener(onItemClickListener: OnItemClickListener){
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {

        override fun onChildViewDetachedFromWindow(view: View) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view?.setOnClickListener {
                val holder = getChildViewHolder(view)
                onItemClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    })
}
