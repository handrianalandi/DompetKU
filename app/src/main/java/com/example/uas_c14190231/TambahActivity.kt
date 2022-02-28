package com.example.uas_c14190231

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TambahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        val mFragmentManager=supportFragmentManager
        val mMain=tambahmain()
        val tanggal=intent.getStringExtra("tanggal")
        val bulan=intent.getStringExtra("bulan")
        val tahun=intent.getStringExtra("tahun")

        val mBundle=Bundle()
        mBundle.putString("tanggal",tanggal)
        mBundle.putString("bulan",bulan)
        mBundle.putString("tahun",tahun)

        mMain.arguments=mBundle

        mFragmentManager.findFragmentByTag(tambahmain::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainerTambah,mMain,tambahmain::class.java.simpleName)
            .commit()
    }
}