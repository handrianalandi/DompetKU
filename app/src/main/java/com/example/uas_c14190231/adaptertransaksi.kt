package com.example.uas_c14190231

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adaptertransaksi(private val listTransaksi: MutableList<transaksiclass>) :
    RecyclerView.Adapter<adaptertransaksi.ListViewHolder>()
{
    private lateinit var onItemClickCallback : OnItemClickCallback
    interface OnItemClickCallback{
        fun deleteTransaksi(data:transaksiclass)
    }
    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }
    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var _tvNama: TextView =itemView.findViewById(R.id.tvNama)
        var _tvKategori: TextView =itemView.findViewById(R.id.tvKategori)
        var _tvJumlah: TextView =itemView.findViewById(R.id.tvJumlah)
        var _btnDelete:ImageButton=itemView.findViewById(R.id.btnDelete)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adaptertransaksi.ListViewHolder {
        val view:View=LayoutInflater.from(parent.context)
            .inflate(R.layout.layouttransaksi,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adaptertransaksi.ListViewHolder, position: Int) {
        val transaksi=listTransaksi[position]

//        tipe 1 = pemasukan, 2=pengeluaran
        holder._tvNama.setText(transaksi.nama)
        holder._tvKategori.setText(transaksi.kategori)

        if(transaksi.tipe=="2"){
            holder._tvJumlah.setTextColor(Color.parseColor("#FF3E33"))
            holder._tvJumlah.setText("- Rp. "+transaksi.jumlah)
        }
        else{
            holder._tvJumlah.setTextColor(Color.parseColor("#63FF20"))
            holder._tvJumlah.setText("Rp. "+transaksi.jumlah)
        }

        holder._btnDelete.setOnClickListener {
            onItemClickCallback.deleteTransaksi(transaksi)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return listTransaksi.size
    }
}