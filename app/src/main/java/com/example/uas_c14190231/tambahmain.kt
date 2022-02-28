package com.example.uas_c14190231

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import java.time.LocalDate
import android.content.DialogInterface
import android.text.InputType

import android.widget.EditText
import androidx.core.view.marginLeft
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [tambahmain.newInstance] factory method to
 * create an instance of this fragment.
 */
//fun generateRandomID(): String {
//    return UUID.randomUUID().toString()
//}

class tambahmain : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var kategori: ArrayList<String> = arrayListOf()
    lateinit var spinneradapter : ArrayAdapter<String>


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
        return inflater.inflate(R.layout.fragment_tambahmain, container, false)
    }

    fun getKategoriDB() {
        db.collection("tbKategori").get()
            .addOnSuccessListener { result ->
                kategori.clear()
                for (document in result) {
                    kategori.add(document.data.get("nama").toString())
                }
                spinneradapter.notifyDataSetChanged()
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //declare xmls
        var _spinnerKategori = view.findViewById<Spinner>(R.id.spinnerKategori)
        var _tvTanggal = view.findViewById<TextView>(R.id.tvTanggal)
        var _etNama = view.findViewById<EditText>(R.id.etNama)
        var _etJumlah = view.findViewById<EditText>(R.id.etJumlah)
        var _rbPemasukan = view.findViewById<RadioButton>(R.id.rbPemasukan)
        var _rbPengeluaran = view.findViewById<RadioButton>(R.id.rbPengeluaran)
        var _btnTambahKategori = view.findViewById<AppCompatButton>(R.id.btnTambahKategori)
        var _btnTambah = view.findViewById<AppCompatButton>(R.id.btnTambah)
        var _btnBack=view.findViewById<ImageButton>(R.id.btnBack)


        //vars

        var selectedKategori = ""
        var tanggal = LocalDate.now().dayOfMonth.toString()
        var bulan = LocalDate.now().monthValue.toString()
        var tahun = LocalDate.now().year.toString()

        if (arguments != null) {
            tanggal = arguments?.getString("tanggal").toString()
            bulan = arguments?.getString("bulan").toString()
            tahun = arguments?.getString("tahun").toString()
        }

        _tvTanggal.setText(tanggal + " - " + bulan + " - " + tahun)

        spinneradapter=ArrayAdapter(requireActivity(), R.layout.support_simple_spinner_dropdown_item, kategori)
        _spinnerKategori.adapter =spinneradapter
        _spinnerKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedKategori = kategori[p2]
                _spinnerKategori.setSelection(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        getKategoriDB()
        if (kategori.size > 0) {
            selectedKategori = kategori[0]
            Log.d("testingkategori", selectedKategori)
        }

        _btnBack.setOnClickListener {
            activity?.finish()
        }

        _btnTambahKategori.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setTitle("Kategori Baru")
            val input = EditText(activity)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, which ->
                    if(input.text.toString()!=""){
                        var newkat = kategoriclass(input.text.toString())
                        db.collection("tbKategori").document(newkat.nama)
                            .set(newkat)
                            .addOnSuccessListener {
                                getKategoriDB()
                            }
                            .addOnFailureListener {
                                Log.d("Firebase", it.message.toString())
                            }
                    }
                    else{
                        Toast.makeText(activity,"Nama Kategori Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
                    }
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }

        _btnTambah.setOnClickListener {
            var lanjut=true
            //tipe 1 = pemasukan, 2=pengeluaran
            var tipe="1"

            var kategori=_spinnerKategori.selectedItem.toString()
            if(_etNama.text.isEmpty()){
                _etNama.error="Nama Tidak Boleh Kosong"
                lanjut=false
            }
            if(_etJumlah.text.isEmpty()){
                _etJumlah.error="Jumlah Tidak Boleh Kosong"
                lanjut=false
            }
            if(_rbPengeluaran.isChecked){
                tipe="2"
            }
            if(lanjut){
//                val id=generateRandomID()
                val newtransaksi=transaksiclass(UUID.randomUUID().toString(),_etNama.text.toString(),tipe,kategori,_etJumlah.text.toString(),tanggal,bulan,tahun)
                db.collection("tbTransaksi").document(newtransaksi.id)
                    .set(newtransaksi)
                    .addOnSuccessListener {
                        activity?.finish()
                    }
                    .addOnFailureListener{
                        Log.d("Firebase",it.message.toString())
                    }
            }
        }

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment tambahmain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            tambahmain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}