package ufpr.br.carteiravirtual.interfaces;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ufpr.br.carteiravirtual.models.MoedaResponse;

public interface AwesomeApiService {
    @GET("json/{moedas}")
    Call<List<MoedaResponse>> getCotacao(@Path("moedas") String moedas);
}