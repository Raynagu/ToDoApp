package com.admin.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageViewFlash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.flash_in))
        Handler().postDelayed({
            imageViewFlash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.flash_out))
            Handler().postDelayed({
                imageViewFlash.visibility = View.GONE
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }, 500)
        }, 1500)
    }
}
