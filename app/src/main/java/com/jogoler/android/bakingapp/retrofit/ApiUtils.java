package com.jogoler.android.bakingapp.retrofit;


/**
 * Created by Gilbert on 7/3/2017.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";


    public static RecipeService getRecipeService(){
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }
}
