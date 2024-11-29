package ufpr.br.carteiravirtual

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ufpr.br.carteiravirtual.Deposito.Companion.EXTRA_DEPOSIT_AMOUNT
import ufpr.br.carteiravirtual.MainActivity.Companion.EXTRA_CONVERTED_AMOUNT
import ufpr.br.carteiravirtual.models.MoedaResponse
import ufpr.br.carteiravirtual.objetos.RetrofitClient

class ConverterRecursosActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var origemSpinner: Spinner
    private lateinit var destinoSpinner: Spinner
    private lateinit var valorEditText: EditText
    private val api = RetrofitClient.instance
    private val moedas = listOf("BRL", "USD", "EUR", "BTC", "ETH")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter_recursos)

        origemSpinner = findViewById(R.id.origemSpinner)
        destinoSpinner = findViewById(R.id.destinoSpinner)
        valorEditText = findViewById(R.id.valorEditText)
        progressBar = findViewById(R.id.progressBar)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Converter Recursos"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moedas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origemSpinner.adapter = adapter
        destinoSpinner.adapter = adapter
        destinoSpinner.setSelection(1)

        findViewById<Button>(R.id.converterButton).setOnClickListener {
            val origem = origemSpinner.selectedItem.toString()

            val destino = destinoSpinner.selectedItem.toString()
            val valor = valorEditText.text.toString().toDoubleOrNull()

            if (valor == null || valor <= 0 || origem == destino) {
                Toast.makeText(this, "Entrada inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val origemValor = getSharedPreferences("wallet", Context.MODE_PRIVATE)
                .getFloat(origem, 0f).toDouble()

            if (valor > origemValor) {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            realizarConversao(origem, destino, valor)
        }
    }

    private fun realizarConversao(origem: String, destino: String, valor: Double) {
        progressBar.visibility = View.VISIBLE
        val moedas = "$destino-$origem"

        api.getCotacao(moedas).enqueue(object : Callback<List<MoedaResponse>> {
            override fun onResponse(
                call: Call<List<MoedaResponse>>,
                response: Response<List<MoedaResponse>>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val cotacoes = response.body()
                    if (cotacoes != null && cotacoes.isNotEmpty()) {
                        val cotacao = cotacoes[0]
                        val taxaConversao = cotacao.ask.toDoubleOrNull()
                        if (taxaConversao != null) {
                            val resultado = valor / taxaConversao
                            atualizarInterface(resultado, destino)
                            atualizarSaldos(origem, destino, valor, resultado)
                        } else {
                            Toast.makeText(
                                this@ConverterRecursosActivity,
                                "Erro ao obter taxa de conversão.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ConverterRecursosActivity,
                            "Erro na resposta da API.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ConverterRecursosActivity,
                        "Erro ao consultar API.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            override fun onFailure(call: Call<List<MoedaResponse>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@ConverterRecursosActivity,
                    "Erro de conexão: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun atualizarInterface(resultado: Double, destino: String) {
        Toast.makeText(
            this,
            "Valor convertido: %.2f %s".format(resultado, destino),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun atualizarSaldos(
        origem: String,
        destino: String,
        valorOrigem: Double,
        valorDestino: Double
    ) {
        val prefs = getSharedPreferences("wallet", Context.MODE_PRIVATE)
        val saldoOrigem = prefs.getFloat(origem, 0f).toDouble()
        val saldoDestino = prefs.getFloat(destino, 0f).toDouble()
        prefs.edit().apply {
            putFloat(origem, (saldoOrigem - valorOrigem).toFloat())
            putFloat(destino, (saldoDestino + valorDestino).toFloat())
            apply()
        }
        finish()
    }
}
