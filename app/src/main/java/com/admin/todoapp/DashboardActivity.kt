package com.admin.todoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.admin.todoapp.DTO.ToDo
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.dashboard_dialog.view.*
import kotlinx.android.synthetic.main.rv_child.view.*

class DashboardActivity : AppCompatActivity() {
    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        //set actionbar
        setSupportActionBar(actionToolBar)
        title = "ToDo List"
        //initialize dbHandler
        dbHandler = DBHandler(this)
        //set layout fro recyclerView
        recycler.layoutManager = LinearLayoutManager(this)


        fab.setOnClickListener {
            Log.i("Message", "fab clicked")
            //Build alertDialog
            val dialog = AlertDialog.Builder(this)
            //inflate the custom dialog layout and get the View object
            val view = layoutInflater.inflate(R.layout.dashboard_dialog, null)
            //get the editetxtView from view
            val toDoName = view.editTextToDoName
            //set dialog view
            dialog.setView(view)

            //set butons for dialog
            dialog.setPositiveButton("Add"){ _, _ ->
                if(toDoName.text.isNotEmpty()){
                    val toDo = ToDo()
                    toDo.name = toDoName.text.toString()
                    //call the addToDo() to insert into DB
                    dbHandler.addToDo(toDo)
                    //call refresh() to refresh recyclerView
                    refreshList()
                }
            }
            dialog.setNegativeButton("Cancel") { _, _ ->

            }

            dialog.show()
        }
    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }

    private fun refreshList(){
        recycler.adapter = DashboardAdapter(this,dbHandler.getToDo())
    }
}

class DashboardAdapter(val context: Context, val list: MutableList<ToDo>) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_child, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoName.text = list[position].name
        holder.todoName.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(INTENT_TODO_NAME, list[position].name)
            intent.putExtra(INTENT_TODO_ID, list[position].id)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val todoName = view.tv_todoName
    }

}
