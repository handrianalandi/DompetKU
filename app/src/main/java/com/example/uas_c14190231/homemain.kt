package com.example.uas_c14190231

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [homemain.newInstance] factory method to
 * create an instance of this fragment.
 */
class homemain : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapterT: adaptertransaksi
    private var arTransaksi: MutableList<transaksiclass> = mutableListOf()
    private var jumlahPemasukan = 0
    private var jumlahPengeluaran = 0
    private var tanggal = ""
    private var bulan = ""
    private var tahun = ""
    private lateinit var _tvPemasukan: TextView
    private lateinit var _tvPengeluaran: TextView
    private lateinit var _tvCheer: TextView
    private lateinit var _tvTanggal: TextView
    private lateinit var _btnSetTanggal: AppCompatButton

    var day = 0
    var month = 0
    var year = 0
    var savedday = LocalDate.now().dayOfMonth
    var savedmonth = LocalDate.now().monthValue
    var savedyear = LocalDate.now().year


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homemain, container, false)
    }

    override fun onStart() {
        super.onStart()
        getTransaksi()
    }

    private fun getSpesificTransaksi(hari: String, bulan: String, tahun: String) {
        db.collection("tbTransaksi").get()
            .addOnSuccessListener { result ->
                arTransaksi.clear()
                for (document in result) {
                    if (document.data.get("tanggal")
                            .toString() == hari && document.data.get("bulan")
                            .toString() == bulan && document.data.get("tahun")
                            .toString() == tahun
                    ) {
                        var idTransaksi = document.data.get("id").toString()
                        var bulanTransaksi = document.data.get("bulan").toString()
//                        "%,d".format(document.data.get("jumlah").toString().toInt())
                        var jumlahTransaksi = "%,d".format(document.data.get("jumlah").toString().toInt())
                        var kategoriTransaksi = document.data.get("kategori").toString()
                        var namaTransaksi = document.data.get("nama").toString()
                        var tahunTransaksi = document.data.get("tahun").toString()
                        var tanggalTransaksi = document.data.get("tanggal").toString()
                        var tipeTransaksi = document.data.get("tipe").toString()

                        var newtransaksi = transaksiclass(
                            idTransaksi,
                            namaTransaksi,
                            tipeTransaksi,
                            kategoriTransaksi,
                            jumlahTransaksi,
                            tanggalTransaksi,
                            bulanTransaksi,
                            tahunTransaksi
                        )
                        arTransaksi.add(newtransaksi)
                    }
                    if (document.data.get("bulan").toString() == bulan && document.data.get("tahun")
                            .toString() == tahun
                    ) {
                        if (document.data.get("tipe").toString() == "1") {
                            jumlahPemasukan += document.data.get("jumlah").toString().toInt()
                        } else {
                            jumlahPengeluaran += document.data.get("jumlah").toString().toInt()
                        }
                    }
                }
                adapterT.notifyDataSetChanged()
                _tvTanggal.setText("Transaksi Tanggal:$savedday Bulan:$savedmonth Tahun:$savedyear")
            }
    }

    private fun getTransaksiBulanIni() {
        db.collection("tbTransaksi").get()
            .addOnSuccessListener { result ->
                arTransaksi.clear()
                for (document in result) {
                    if (document.data.get("bulan").toString() == bulan && document.data.get("tahun")
                            .toString() == tahun
                    ) {
                        var idTransaksi = document.data.get("id").toString()
                        var bulanTransaksi = document.data.get("bulan").toString()
                        //"%,d".format(document.data.get("jumlah").toString().toInt())
                        var jumlahTransaksi = "%,d".format(document.data.get("jumlah").toString().toInt())
                        var kategoriTransaksi = document.data.get("kategori").toString()
                        var namaTransaksi = document.data.get("nama").toString()
                        var tahunTransaksi = document.data.get("tahun").toString()
                        var tanggalTransaksi = document.data.get("tanggal").toString()
                        var tipeTransaksi = document.data.get("tipe").toString()

                        var newtransaksi = transaksiclass(
                            idTransaksi,
                            namaTransaksi,
                            tipeTransaksi,
                            kategoriTransaksi,
                            jumlahTransaksi,
                            tanggalTransaksi,
                            bulanTransaksi,
                            tahunTransaksi
                        )
                        arTransaksi.add(newtransaksi)
                    }
                    if (document.data.get("bulan").toString() == bulan && document.data.get("tahun")
                            .toString() == tahun
                    ) {
                        if (document.data.get("tipe").toString() == "1") {
                            jumlahPemasukan += document.data.get("jumlah").toString().toInt()
                        } else {
                            jumlahPengeluaran += document.data.get("jumlah").toString().toInt()
                        }
                    }
                }
                adapterT.notifyDataSetChanged()
                _tvTanggal.setText("Transaksi Bulan:" + bulan)
                savedday=LocalDate.now().dayOfMonth.toString().toInt()
            }
    }

    //get transaksi = hari ini
    private fun getTransaksi() {
        db.collection("tbTransaksi").get()
            .addOnSuccessListener { result ->
                arTransaksi.clear()
                jumlahPemasukan = 0
                jumlahPengeluaran = 0
                for (document in result) {
                    if (document.data.get("tanggal")
                            .toString() == tanggal && document.data.get("bulan")
                            .toString() == bulan && document.data.get("tahun").toString() == tahun
                    ) {
                        var idTransaksi = document.data.get("id").toString()
                        var bulanTransaksi = document.data.get("bulan").toString()
                        //"%,d".format(document.data.get("jumlah").toString().toInt())
                        var jumlahTransaksi = "%,d".format(document.data.get("jumlah").toString().toInt())
                        var kategoriTransaksi = document.data.get("kategori").toString()
                        var namaTransaksi = document.data.get("nama").toString()
                        var tahunTransaksi = document.data.get("tahun").toString()
                        var tanggalTransaksi = document.data.get("tanggal").toString()
                        var tipeTransaksi = document.data.get("tipe").toString()

                        var newtransaksi = transaksiclass(
                            idTransaksi,
                            namaTransaksi,
                            tipeTransaksi,
                            kategoriTransaksi,
                            jumlahTransaksi,
                            tanggalTransaksi,
                            bulanTransaksi,
                            tahunTransaksi
                        )
                        arTransaksi.add(newtransaksi)
                    }
                    if (document.data.get("bulan").toString() == bulan && document.data.get("tahun")
                            .toString() == tahun
                    ) {
                        if (document.data.get("tipe").toString() == "1") {
                            jumlahPemasukan += document.data.get("jumlah").toString().toInt()
                        } else {
                            jumlahPengeluaran += document.data.get("jumlah").toString().toInt()
                        }
                    }
                }
                //set total pengeluaran pemasukan
                _tvTanggal.setText("Transaksi Tanggal:" + tanggal + " Bulan:" + bulan + " Tahun:" + tahun)
                _tvPemasukan.setText("%,d".format(jumlahPemasukan))
                _tvPengeluaran.setText("%,d".format(jumlahPengeluaran))
                if (jumlahPemasukan >= jumlahPengeluaran) {
                    _tvCheer.setTextColor(Color.parseColor("#3cb043"))
                    _tvCheer.setText("Hebat!\nPertahankan Hematmu!")
                } else {
                    _tvCheer.setTextColor(Color.parseColor("#FF0000"))
                    _tvCheer.setText("Oh Tidak!\nKamu Harus Berhemat!")
                }
                adapterT.notifyDataSetChanged()
                savedday=LocalDate.now().dayOfMonth.toString().toInt()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //declare xmls
        _tvTanggal = view.findViewById(R.id.tvTanggal)
        var _rvTransaksi = view.findViewById<RecyclerView>(R.id.rvTransaksi)
        var _btnTambah = view.findViewById<AppCompatButton>(R.id.btnTambah)
        var _btnHariIni = view.findViewById<AppCompatButton>(R.id.btnHariIni)
        var _btnBulanIni = view.findViewById<AppCompatButton>(R.id.btnBulanIni)
        _btnSetTanggal = view.findViewById(R.id.btnSetTanggal)
        pickDate()
        _tvPemasukan = view.findViewById<TextView>(R.id.tvPemasukan)
        _tvPengeluaran = view.findViewById<TextView>(R.id.tvPengeluaran)
        _tvCheer = view.findViewById<TextView>(R.id.tvCheer)

        tanggal = LocalDate.now().dayOfMonth.toString()
        bulan = LocalDate.now().monthValue.toString()
        tahun = LocalDate.now().year.toString()

        adapterT = adaptertransaksi(arTransaksi)
        adapterT.setOnClickCallback(object : adaptertransaksi.OnItemClickCallback {
            override fun deleteTransaksi(data: transaksiclass) {
                db.collection("tbTransaksi").document(data.id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Transaksi ${data.nama} berhasil dihapus!",
                            Toast.LENGTH_SHORT
                        ).show()
                        getTransaksi()
                    }
                    .addOnFailureListener {
                        Log.d("Firebase", it.message.toString())
                    }
            }
        })
        _rvTransaksi.layoutManager = LinearLayoutManager(activity)
        _rvTransaksi.adapter = adapterT

        getTransaksi()


        //declare variables
//        _tvTanggal.setText("Transaksi Tanggal:" + tanggal + " Bulan:" + bulan + " Tahun:" + tahun)


        _btnTambah.setOnClickListener {
            var intent=Intent(activity, TambahActivity::class.java)
            intent.putExtra("tanggal",savedday.toString())
            intent.putExtra("bulan",savedmonth.toString())
            intent.putExtra("tahun",savedyear.toString())
            startActivity(intent)
        }

        _btnHariIni.setOnClickListener {
            getTransaksi()
        }
        _btnBulanIni.setOnClickListener {
            getTransaksiBulanIni()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment homemain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homemain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedday = p3
        savedmonth = p2+1
        savedyear = p1

        getDateCalendar()

        getSpesificTransaksi(savedday.toString(),savedmonth.toString(),savedyear.toString())
    }

    private fun getDateCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        if(savedday!=LocalDate.now().dayOfMonth.toString().toInt()){
            day = savedday
            month = savedmonth-1
            year = savedyear
        }
    }

    private fun pickDate() {
        _btnSetTanggal.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireActivity(), this, year, month, day).show()
        }

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}