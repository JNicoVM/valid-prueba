package com.example.valid_prueba.activities.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.valid_prueba.models.request.TopArtistsRequest
import com.example.valid_prueba.models.response.TopArtistsResponse
import com.example.valid_prueba.models.response.TopArtistsSearchResponse
import com.example.valid_prueba.retrofit.APIConstants.API_KEY
import com.example.valid_prueba.useCases.GetAllTopArtistsUseCase
import com.example.valid_prueba.utils.APPConstants.COUNTRY_CO
import com.example.valid_prueba.utils.APPConstants.PAGE_LIMIT
import com.example.valid_prueba.utils.APPConstants.TOP_ARTISTS
import com.example.valid_prueba.utils.APPConstants.TOP_TRACKS
import com.example.valid_prueba.utils.notifyObserver
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable

class SearchViewmodel(
    private val getAllTopArtistsUseCase: GetAllTopArtistsUseCase
): ViewModel() {

    private val disposable = CompositeDisposable()

    private var service = MutableLiveData<String>()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    fun getService() = service as LiveData<String>

    fun setService(countryPosition: String){
        this.service.value = countryPosition
    }

    // Se crean la variable livedata y mutablelivedata para que la información persista con el ciclo de vida de la aplicación
    private val _searchValues: MutableLiveData<TopArtistsSearchResponse> = MutableLiveData()
//    val searchValues: LiveData<TopArtistsSearchResponse> get() = _searchValues

    // Se crean la variable livedata y mutablelivedata para que la información persista con el ciclo de vida de la aplicación
    private val _listArtists: MutableLiveData<MutableList<TopArtistsResponse>> = MutableLiveData()
    val listArtists: LiveData<MutableList<TopArtistsResponse>> get() = _listArtists

    init {
        _listArtists.value = ArrayList()
    }

    fun performSearch(){
        when(getService().value){
            TOP_ARTISTS -> searchTopArtists()
            TOP_TRACKS -> searchTopTracks()
        }
    }

    private fun searchTopArtists(){
        disposable.add(
            getAllTopArtistsUseCase
                .invoke(TopArtistsRequest(country = COUNTRY_CO, apiKey = API_KEY, limit = PAGE_LIMIT.toString(), page = currentPage))
                .doOnSubscribe { showLoading() }
                .subscribe{
                    try {
                        // parseando json de respuesta a SearchRespone
                        _searchValues.postValue(Gson().fromJson(it["topartists"], TopArtistsSearchResponse::class.java))
                        val parsinArtists = Gson().fromJson((it["topartists"] as JsonObject).get("artist"), (Array<TopArtistsResponse>::class.java)).toList()
                        _listArtists.value?.addAll(parsinArtists)
                        _listArtists.notifyObserver()
                        if (parsinArtists.size <  PAGE_LIMIT) {
                            isLastPage = true
                        }
                        hideLoading()
                    }catch (error: Exception){
                        hideLoading()
                        Log.e("EParcing", error.toString())
                    }
                }
        )
    }

    private fun searchTopTracks(){

    }

    private fun showLoading() {
        isLoading = true
    }

    private fun hideLoading() {
        isLoading = false
    }

    fun onLoadMoreItems(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (isLoading ||isLastPage || !isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }

        currentPage += 1
        searchTopArtists()
    }

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_LIMIT
    }

    // limpiando disposable
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}


