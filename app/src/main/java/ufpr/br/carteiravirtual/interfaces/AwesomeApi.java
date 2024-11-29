package ufpr.br.carteiravirtual.interfaces;

public interface AwesomeApi {
    @GET("json/last/USD-BRL,EUR-BRL,BTC-BRL,ETH-BRL")
    fun getCotacoes(): Call<Map<String, MoedaResponse>>
}
