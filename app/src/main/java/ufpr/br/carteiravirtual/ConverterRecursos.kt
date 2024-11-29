package ufpr.br.carteiravirtual

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConverterRecursosActivity : AppCompatActivity() {

    private lateinit var origemSpinner: Spinner
    private lateinit var destinoSpinner: Spinner
    private lateinit var valorEditText: EditText
    private lateinit var converterButton: Button
    private lateinit var progressBar: ProgressBar

    private val moedas = listOf("BRL", "USD", "EUR", "BTC", "ETH") // Moedas disponíveis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter_recursos)

        origemSpinner = findViewById(R.id.origemSpinner)
        destinoSpinner = findViewById(R.id.destinoSpinner)
        valorEditText = findViewById(R.id.valorEditText)
        converterButton = findViewById(R.id.converterButton)
        progressBar = findViewById(R.id.progressBar)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moedas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origemSpinner.adapter = adapter
        destinoSpinner.adapter = adapter

        converterButton.setOnClickListener {
            val moedaOrigem = origemSpinner.selectedItem.toString()
            val moedaDestino = destinoSpinner.selectedItem.toString()
            val valor = valorEditText.text.toString().toDoubleOrNull()

            if (valor == null || valor <= 0) {
                Toast.makeText(this, "Insira um valor válido para conversão.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (moedaOrigem == moedaDestino) {
                Toast.makeText(this, "Selecione moedas diferentes para a conversão.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Iniciar a conversão
            realizarConversao(moedaOrigem, moedaDestino, valor)
        }
    }

    private fun realizarConversao(moedaOrigem: String, moedaDestino: String, valor: Double) {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val taxa = obterTaxaDeConversao(moedaOrigem, moedaDestino)
                val valorConvertido = valor * taxa

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ConverterRecursosActivity,
                        "Valor convertido: %.2f %s".format(valorConvertido, moedaDestino),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ConverterRecursosActivity,
                        "Erro ao realizar a conversão. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun obterTaxaDeConversao(moedaOrigem: String, moedaDestino: String): Double {
        return when {
            moedaOrigem == "BRL" && moedaDestino == "USD" -> 0.20
            moedaOrigem == "USD" && moedaDestino == "BRL" -> 5.0
            else -> 1.0
        }
    }
}
