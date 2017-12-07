package com.jogoler.android.bakingapp.retrofit;

import com.jogoler.android.bakingapp.pojo.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gilbert on 7/3/2017.
 */

public interface RecipeService {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
