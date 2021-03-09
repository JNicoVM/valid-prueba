package com.example.valid_prueba.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//val Context.app: RickAndMortyPlatziApp
//    get() = applicationContext as RickAndMortyPlatziApp

fun Context.showLongToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showLongToast(@StringRes resourceId: Int){
    Toast.makeText(this, resourceId, Toast.LENGTH_LONG).show()
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(value)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this.viewModelStore, viewModelFactory)[T::class.java]
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this.viewModelStore, viewModelFactory)[T::class.java]
}
