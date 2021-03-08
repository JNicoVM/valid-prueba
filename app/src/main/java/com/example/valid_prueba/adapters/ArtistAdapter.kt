package com.example.valid_prueba.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valid_prueba.R
import com.example.valid_prueba.databinding.HolderArtistBinding
import com.example.valid_prueba.models.response.TopArtistsResponse
import com.example.valid_prueba.utils.CallbackT
import com.squareup.picasso.Picasso

class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.ItemsViewHolder>() {

    //Lista de items que va a usar el adaptador
    private var itemsList = listOf<TopArtistsResponse>()

    //Callback para ejecutar el evento en la actividad que instancio al adaptador
    private lateinit var callbackClickItem : CallbackT<TopArtistsResponse> // callback generico <T>

    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: HolderArtistBinding =
            HolderArtistBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemsViewHolder (
        LayoutInflater.from(parent.context).inflate(
            R.layout.holder_artist, parent, false
        )
    )

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = itemsList[position]
        initUi(holder, item)
        initListeners(holder, item)
    }

    override fun getItemCount(): Int = itemsList.size

    //Funcion para actualizar la informacion del adaptador y instaciarlo solo una vez
    fun updateItem(items: ArrayList<TopArtistsResponse>){
        this.itemsList = items
        notifyDataSetChanged()
    }

    // inicializacion del callback
    fun onUserClicked(
        callbackClickItem:  CallbackT<TopArtistsResponse>
    ) {
        this.callbackClickItem = callbackClickItem
    }

    //LLenar la informacion de la vista segun el item
    @SuppressLint("SetTextI18n")
    private fun initUi(holder: ItemsViewHolder, item: TopArtistsResponse){
        holder.binding.tvNombreItem.text = item.name
        holder.binding.tvPrecioItem.text = item.listeners
        Picasso.get()
            .load(item.image[0].imageUrl)
            .resize(100, 100)
            .into(holder.binding.ivItem)
    }

    //Inicializar los listeners
    private fun initListeners(holder: ItemsViewHolder, item: TopArtistsResponse){
        holder.binding.llElegido.setOnClickListener {
            callbackClickItem(item)
        }
    }

}