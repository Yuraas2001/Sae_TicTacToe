package com.example.tictactoe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("http://10.3.122.111//fetch_data.php") 
    Call<List<Player>> getPlayers(); 
}
