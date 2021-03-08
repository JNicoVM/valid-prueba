package com.example.valid_prueba.base

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.example.valid_prueba.R

open class BaseActivity : AppCompatActivity() {

    private var contentLoadingProgressBar: ContentLoadingProgressBar?=null

    //funcion para mostrar progressbar
    fun mostrarProgressBar() {
        contentLoadingProgressBar?.visibility = View.VISIBLE
        contentLoadingProgressBar?.show()
    }

    //funcion para ocultar progressbar
    fun ocultarProgressBar() {
        contentLoadingProgressBar?.hide()
    }

//    funcion para inicializar progressbar
    fun inicializarProgressBar() {
        contentLoadingProgressBar = findViewById(R.id.contentLoadingProgressBar)
    }

//    funcion para inicializar 1 o varios spinners (ideal para formularios)
    fun inicializarSpinner(data: Array<String>, spinnerView: Int){
        val adapter = ArrayAdapter(this, R.layout.spinner_selected, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        val spinner = findViewById<Spinner>(spinnerView)
        spinner.adapter = adapter
        Log.d("D_spinner_filtro", "inicializado con exito")
    }
}