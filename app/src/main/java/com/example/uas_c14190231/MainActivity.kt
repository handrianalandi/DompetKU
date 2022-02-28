package com.example.uas_c14190231

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

var db:FirebaseFirestore = FirebaseFirestore.getInstance()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mFragmentManager=supportFragmentManager
        val mMain=homemain()

        mFragmentManager.findFragmentByTag(homemain::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer,mMain,homemain::class.java.simpleName)
            .commit()
    }


}