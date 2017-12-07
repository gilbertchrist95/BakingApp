package com.jogoler.android.bakingapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jogoler.android.bakingapp.adapter.RecipeAdapter;
import com.jogoler.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.jogoler.android.bakingapp.pojo.Recipe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemListener {


    public static final String SELECTED_RECIPE = "selected_recipe";


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();
    }

    @Override
    public void onItemClickListener(Recipe clickedItemRecipe) {
        Bundle selectedBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(clickedItemRecipe);
        selectedBundle.putParcelableArrayList(SELECTED_RECIPE,selectedRecipe);

        Log.d("TAG",selectedBundle.isEmpty()+"");
        Intent intentToDetail = new Intent(this, DetailActivity.class);
        intentToDetail.putExtras(selectedBundle);
        startActivity(intentToDetail);
    }

    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
