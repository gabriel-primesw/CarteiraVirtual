package ufpr.br.carteiravirtual

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Deposito : AppCompatActivity() {

    private lateinit var valorEditText: EditText
    private lateinit var depositarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposito)

        // Inicializar as views
        valorEditText = findViewById(R.id.valorEditText)
        depositarButton = findViewById(R.id.depositarButton)

        // Configurar o listener do botão "Depositar"
        depositarButton.setOnClickListener {
            val valor = valorEditText.text.toString().toDoubleOrNull()
            if (valor != null && valor > 0) {
                // Retornar o valor depositado para a Activity chamadora
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_DEPOSIT_AMOUNT, valor)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            // Finaliza a Activity
            } else {
                // Exibe um aviso se o valor for inválido
                Toast.makeText(this, "Por favor, insira um valor válido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val EXTRA_DEPOSIT_AMOUNT = "EXTRA_DEPOSIT_AMOUNT"
    }
}

