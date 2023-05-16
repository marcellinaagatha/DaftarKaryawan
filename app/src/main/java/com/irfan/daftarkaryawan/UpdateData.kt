package com.irfan.daftarkaryawan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_update_data.*
import kotlinx.android.synthetic.main.activity_update_data.rbPria

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? =  null
    private var cekNama: String? = null
    private var cekNip: String? = null
    private var cekJabatan: String? = null
    private var cekJkel: String? = null

    private fun getJkel(): String{
        var jenisKelamin = ""
        jenisKelamin = if (rbPria.isChecked){
            "Laki-laki"
        } else {
            "Perempuan"
        }
        return jenisKelamin
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)
        supportActionBar!!.title = "Update Data"
        get()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data
        setDataSpinnerJB()
        update.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                cekNama = new_nama.text.toString()
                cekNip = new_nip.text.toString()
                cekJabatan = new_spinnerJB.selectedItem.toString()
                val getJkel : String = getJkel()

                if (isEmpty(cekNama) || isEmpty(cekNip) || isEmpty(cekJabatan) || isEmpty(getJkel) ){
                    Toast.makeText(this@UpdateData, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }else{
                    val setdata_karyawan = data_karyawan()
                    setdata_karyawan.nama = new_nama.text.toString()
                    setdata_karyawan.nip = new_nip.text.toString()
                    setdata_karyawan.jabatan = new_spinnerJB.selectedItem.toString()
                    setdata_karyawan.jkel = getJkel()
                    updateKaryawan(setdata_karyawan)
                }
            }
        })



    }
    private fun get() {
        val getNama = intent.extras!!.getString("dataNama")
        val getNip = intent.extras!!.getString("dataNip")
        val getJabatan = intent.extras!!.getString("datajabatan")
        val getJkel = intent.extras!!.getString("dataJkel")

        new_nama!!.setText(getNama)
        new_nip!!.setText(getNip)
        new_jabatan!!.tag = getJabatan
        if (getJkel == "Laki-laki") {
            rbPria.isChecked = true
        } else {
            (getJkel == "Perempuan")
        }
    }

    private fun setDataSpinnerJB() {
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.Jabatan, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        new_spinnerJB.adapter = adapter

    }

    private val data: Unit
        private get() {
            val getNama = intent.extras!!.getString("dataNama")
            val getNip = intent.extras!!.getString("dataNip")
            val getJabatan = intent.extras!!.getString("dataJabatan")
            val getJkel = intent.extras!!.getString("dataJkel")
            new_nama!!.setText(getNama)
            new_nip!!.setText(getNip)
            new_spinnerJB!!.setTag(getJabatan)
            if (getJkel == "Laki-laki"){
                rbPria.isChecked = true
            } else  {
                (getJkel == "Perempuan")
                true
            }

        }

    private fun updateKaryawan(setdatadata_karyawan: data_karyawan) {
        val userID = auth!!.uid
        val getkey =intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("DataKaryawan")
            .child(getkey!!)
            .setValue(setdatadata_karyawan)
            .addOnSuccessListener {
                new_nama!!.setText("")
                new_nip!!.setText("")
                new_spinnerJB!!.setTag("")
                if (getJkel() == "Laki-laki"){
                    rbPria.isChecked = true
                } else  {
                    (getJkel() == "Perempuan")
                    true
                }

                Toast.makeText(this@UpdateData,"Data Berhasil Diubah",
                    Toast.LENGTH_SHORT).show()
                finish()
            }


    }
}