package ufpr.br.carteiravirtual

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListarRecursosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_recursos)

        recyclerView = findViewById(R.id.recursosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val recursos = carregarRecursos()
        adapter = RecursosAdapter(recursos)
        recyclerView.adapter = adapter
    }

    private fun carregarRecursos(): List<Recurso> {
        // Simulação de carregamento usando SharedPreferences
        val prefs = getSharedPreferences("wallet", Context.MODE_PRIVATE)

        val recursos = listOf(
            Recurso("Real (R$)", prefs.getFloat("BRL", 0f).toDouble()),
            Recurso("Dólar ($)", prefs.getFloat("USD", 0f).toDouble()),
            Recurso("Euro (€)", prefs.getFloat("EUR", 0f).toDouble()),
            Recurso("Bitcoin (BTC)", prefs.getFloat("BTC", 0f).toDouble()),
            Recurso("Ethereum (ETH)", prefs.getFloat("ETH", 0f).toDouble())
        )

        return recursos
    }
}

// ViewHolder para exibir o nome e valor da moeda
class ItemMoedaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nomeMoedaTextView: TextView = view.findViewById(R.id.nomeMoedaTextView)
    val valorMoedaTextView: TextView = view.findViewById(R.id.valorMoedaTextView)

    fun bind(nome: String, valor: String) {
        nomeMoedaTextView.text = nome
        valorMoedaTextView.text = valor
    }
}

class RecursosAdapter(private val recursos: List<Recurso>) :
    RecyclerView.Adapter<RecursosAdapter.RecursoViewHolder>() {

    class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeMoedaTextView: TextView = itemView.findViewById(R.id.nomeMoedaTextView)
        val valorMoedaTextView: TextView = itemView.findViewById(R.id.valorMoedaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.nomeMoedaTextView.text = recurso.moeda
        holder.valorMoedaTextView.text = String.format("%.2f", recurso.saldo)
    }

    override fun getItemCount(): Int = recursos.size
}

data class Recurso(val moeda: String, val saldo: Double)
