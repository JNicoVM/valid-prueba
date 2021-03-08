package com.example.valid_prueba.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.valid_prueba.R
import com.example.valid_prueba.activities.viewmodel.SearchViewmodel
import com.example.valid_prueba.adapters.ArtistAdapter
import com.example.valid_prueba.base.BaseActivity
import com.example.valid_prueba.data.RemoteTopArtistsDataSource
import com.example.valid_prueba.databinding.SearchActivityBinding
import com.example.valid_prueba.models.response.TopArtistsResponse
import com.example.valid_prueba.repository.TopArtistsRepository
import com.example.valid_prueba.retrofit.APIConstants.BASE_API_URL
import com.example.valid_prueba.retrofit.RequestTopArtists
import com.example.valid_prueba.retrofit.SearchRetrofitDataSource
import com.example.valid_prueba.useCases.GetAllTopArtistsUseCase
import com.example.valid_prueba.utils.APPConstants.TOP_ARTISTS
import com.example.valid_prueba.utils.showLongToast

class SearchActivity : BaseActivity() {

    // Llamado generico de la actividad
    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(
                context,
                SearchActivity::class.java
            )
            context.startActivity(intent)
        }
    }

    // late init del binding
    private lateinit var binding: SearchActivityBinding

    //     se carga lista de elementos filtros
    private val servicesList: Array<String> by lazy {
        resources.getStringArray(R.array.servicios)
    }

    private val remote: RemoteTopArtistsDataSource by lazy {
        SearchRetrofitDataSource(RequestTopArtists(BASE_API_URL))
    }

    private val repo: TopArtistsRepository by lazy {
        TopArtistsRepository(remote)
    }

    private val userCases: GetAllTopArtistsUseCase by lazy {
        GetAllTopArtistsUseCase(repo)
    }

    private val searchViewmodel: SearchViewmodel by lazy {
        //construyendo viewmodelprovider con funcion generica
        SearchViewmodel(userCases)
    }

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                searchViewmodel.onLoadMoreItems(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
    }

    // adapatador del recyclerView
    private var artistAdapter= ArtistAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeUi()
        initListeners()
    }


    // Se inicializan los elemento de usuario que va a estar trabajando a lo largo de la vida de la actividad
    private fun initializeUi() {
        // Se iniciliza el progressbar indeterminado segun su R.id
        inicializarProgressBar()
        // Se iniciliza el spinner segun su lista de parametros y R.id
        inicializarSpinner(servicesList, R.id.spinnerServicios)
        // Se asigna el correspondiente layoutManager al recyclerview y el respectivo adapter
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = artistAdapter
        }
    }

    // Se inicializan los diferentes tipos de listeners que va a estar trabajando a lo largo de la vida de la actividad
    private fun initListeners() {
        //El escuchador de la variable searchValues declarada como livedata
        searchViewmodel.searchValues.observe(this, {
            ocultarProgressBar()
            //Se carga la lista de elementos en el adaptador
            loadAdapter(it)
        })

        binding.btnBuscar.setOnClickListener {
            searchViewmodel.performSearch()
        }

        // Listener de la posicion del spinner
        binding.spinnerServicios.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // segun la posicion del spinner se va a hacer un query con un filtro diferente
                    when (position) {
                        0 -> {
                            searchViewmodel.setService("")
                            binding.btnBuscar.isEnabled = false
                            showLongToast("Select one option")
                        }
                        1 -> {
                            searchViewmodel.setService(TOP_ARTISTS)
                            binding.btnBuscar.isEnabled = true
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

//        binding.rvItems.run {
//            addOnScrollListener(onScrollListener)
//            adapter = artistAdapter
//        }
    }

    //Se carga la lista que llega del servicio en el adaptador
    private fun loadAdapter(listTopArtistsResponse: List<TopArtistsResponse>) {
        listTopArtistsResponse.let {
            artistAdapter.updateItem(ArrayList(it))
            if(it.isEmpty())
                binding.noData.visibility = View.VISIBLE
            else
                binding.noData.visibility = View.GONE
        }
    }

}