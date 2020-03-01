package com.admin.todoapp

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.admin.todoapp.DTO.ToDoItem
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.dashboard_dialog.view.*
import kotlinx.android.synthetic.main.rv_item_child.view.*

class ItemActivity : AppCompatActivity() {
    lateinit var dbHandler: DBHandler
    var todoId: Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(item_ToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent.getStringExtra(INTENT_TODO_NAME)
        todoId = intent.getLongExtra(INTENT_TODO_ID, -1)
        dbHandler = DBHandler(this)
        item_recycler.layoutManager = LinearLayoutManager(this)


        item_fab.setOnClickListener {
            // Add new sun ToDoItems to the clicked ToDO_id/Name
            //set Alert Dialog to get new todoItem name
            val dialog = AlertDialog.Builder(this)
            //inflate the custom dialog layout and get the View object
            val view = layoutInflater.inflate(R.layout.dashboard_dialog, null)
            val todoItem = view.editTextToDoName
            dialog.setView(view)
            dialog.setPositiveButton("Add"){ _: DialogInterface, _: Int ->
                //validate the input
                if(todoItem.text.isNotEmpty()){
                    val item = ToDoItem()
                    item.toDoId = todoId
                    item.itemName = todoItem.text.toString()
                    item.isCompleted = false

                    //add toDoItem to Database
                    val rs = dbHandler.addToDoItem(item)
                    //refresh the recycler
                    refreshList()
                }
            }
            dialog.setNegativeButton("Cancel"){ _: DialogInterface, _: Int ->

            }
            dialog.show()
        }


    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }

    private fun refreshList() {
        item_recycler.adapter = ItemAdapter(this, dbHandler, dbHandler.getToDOItem(todoId))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item?.itemId == android.R.id.home){
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    class ItemAdapter(val context: Context, val dbHandler: DBHandler, val list: MutableList<ToDoItem>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

        class ViewHolder(v:View): RecyclerView.ViewHolder(v) {
            val itemName: CheckBox = v.cb_item
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_child, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemName.text = list[position].itemName
            holder.itemName.isChecked = list[position].isCompleted

            //set onCLickListener() to toDOItems
            holder.itemName.setOnClickListener {
                list[position].isCompleted = !list[position].isCompleted
                dbHandler.updateToDoItem(list[position])
            }
        }

    }
}
