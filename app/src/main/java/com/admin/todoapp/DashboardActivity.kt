package com.admin.todoapp

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
            //Build alertDialog
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Add ToDo")
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

     fun refreshList(){
        recycler.adapter = DashboardAdapter(this,dbHandler.getToDo())
    }

    fun editToDo(toDo: ToDo) {
        //Build alertDialog
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Edit ToDo name")
        //inflate the custom dialog layout and get the View object
        val view = layoutInflater.inflate(R.layout.dashboard_dialog, null)
        //get the editetxtView from view
        val toDoName = view.editTextToDoName
        toDoName.setText(toDo.name)
        //set dialog view
        dialog.setView(view)

        //set butons for dialog
        dialog.setPositiveButton("Update"){ _, _ ->
            if(toDoName.text.isNotEmpty()){
                toDo.name = toDoName.text.toString()
                //call the addToDo() to insert into DB
                dbHandler.editToDo(toDo)
                //call refresh() to refresh recyclerView
                refreshList()
            }
        }
        dialog.setNegativeButton("Cancel") { _, _ ->

        }

        dialog.show()
    }
}

class DashboardAdapter(val activity: DashboardActivity, val list: MutableList<ToDo>) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoName.text = list[position].name
        isCompleted(list[position].id, holder)

        holder.todoName.setOnClickListener {
            val intent = Intent(activity, ItemActivity::class.java)
            intent.putExtra(INTENT_TODO_NAME, list[position].name)
            intent.putExtra(INTENT_TODO_ID, list[position].id)
            activity.startActivity(intent)
        }

        holder.menu.setOnClickListener {
            val popUp = PopupMenu(activity, holder.menu)
            popUp.inflate(R.menu.todo_menu)
            //set clickListener on popUpmenuItems
            popUp.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.todo_edit ->{
                        activity.editToDo(list[position])
                    }
                    R.id.todo_completed ->{
                        activity.dbHandler.updateToDoItemCompletedStatus(list[position].id, true)
                        isCompleted(list[position].id, holder)
                    }
                    R.id.todo_delete ->{
                        val dialog = AlertDialog.Builder(activity)
                        dialog.setTitle("Warning")
                        dialog.setMessage("Are you sure ?")
                        dialog.setPositiveButton("Delete"){ _, _ ->

                            activity.dbHandler.deleteToDo(list[position].id)
                            activity.refreshList()
                        }
                        dialog.setNegativeButton("Cancel"){ _, _ ->

                        }
                        dialog.show()
                    }
                    R.id.todo_reset ->{
                        activity.dbHandler.updateToDoItemCompletedStatus(list[position].id, false)
                        isCompleted(list[position].id, holder)
                    }
                }
                true
            }
            popUp.show()
        }
    }

    fun isCompleted(id: Long, holder: ViewHolder ){
        if(activity.dbHandler.isToDoCompleted(id)){
            holder.todoName.paintFlags = holder.todoName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.todoName.paintFlags = holder.todoName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val todoName = view.tv_todoName
        val menu = view.iv_menu
    }

}
