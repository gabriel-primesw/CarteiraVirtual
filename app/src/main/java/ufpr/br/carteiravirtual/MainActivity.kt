package ufpr.br.carteiravirtual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var saldoTextView: TextView
    private lateinit var depositButton: Button
    private lateinit var listResourcesButton: Button
    private lateinit var convertResourcesButton: Button

    private var saldo: Double = 0.0 // Saldo inicial em R$

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar as views
        saldoTextView = findViewById(R.id.saldoTextView)
        depositButton = findViewById(R.id.depositButton)
        listResourcesButton = findViewById(R.id.listResourcesButton)
        convertResourcesButton = findViewById(R.id.convertResourcesButton)

        // Atualizar o saldo inicial na tela
        atualizarSaldo()

        // Listener para o botão de depósito
        depositButton.setOnClickListener {
            val intent = Intent(this, Deposito::class.java)
            startActivityForResult(intent, REQUEST_DEPOSIT)
        }

        // Listener para o botão de listar recursos
        listResourcesButton.setOnClickListener {
            val intent = Intent(this, ListarRecursosActivity::class.java)
            startActivity(intent)
        }

        // Listener para o botão de converter recursos
        convertResourcesButton.setOnClickListener {
            val intent = Intent(this, ConverterRecursosActivity::class.java)
            startActivity(intent)
        }
    }

    // Atualiza o TextView do saldo
    private fun atualizarSaldo() {
        val prefs = getSharedPreferences("wallet", Context.MODE_PRIVATE)
        saldo = prefs.getFloat("BRL", 0f).toDouble()
        saldoTextView.text = "Saldo: R$ %.2f".format(saldo)
    }

    // Gerenciar o resultado de Activities, como depósito
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DEPOSIT && resultCode == RESULT_OK) {
            val valorDeposito = data?.getDoubleExtra(EXTRA_DEPOSIT_AMOUNT, 0.0) ?: 0.0
            saldo += valorDeposito
            val prefs = getSharedPreferences("wallet", Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putFloat("BRL", saldo.toFloat())
                apply()
            }
            atualizarSaldo()
        }
    }

    companion object {
        const val REQUEST_DEPOSIT = 1
        const val EXTRA_DEPOSIT_AMOUNT = "EXTRA_DEPOSIT_AMOUNT"
    }
}

