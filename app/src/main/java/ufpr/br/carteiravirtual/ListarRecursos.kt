package ufpr.br.carteiravirtual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ViewHolder para exibir o nome e valor da moeda
class ItemMoedaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nomeMoedaTextView: TextView = view.findViewById(R.id.nomeMoedaTextView)
    val valorMoedaTextView: TextView = view.findViewById(R.id.valorMoedaTextView)

    fun bind(nome: String, valor: String) {
        nomeMoedaTextView.text = nome
        valorMoedaTextView.text = valor
    }
}

// Adapter para RecyclerView (supondo que você tenha uma lista de moedas)
class MoedasAdapter(private val moedas: List<Pair<String, String>>) : RecyclerView.Adapter<ItemMoedaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMoedaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return ItemMoedaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemMoedaViewHolder, position: Int) {
        val moeda = moedas[position]
        holder.bind(moeda.first, moeda.second)
    }

    override fun getItemCount(): Int = moedas.size
}
