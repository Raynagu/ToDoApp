package com.admin.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(item_ToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent.getStringExtra(INTENT_TODO_NAME)
        val todoId = intent.getLongExtra(INTENT_TODO_ID, -1)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item?.itemId == android.R.id.home){
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
