package com.jogoler.android.bakingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jogoler.android.bakingapp.MainActivity;
import com.jogoler.android.bakingapp.R;
import com.jogoler.android.bakingapp.adapter.RecipeAdapter;
import com.jogoler.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.jogoler.android.bakingapp.pojo.Recipe;
import com.jogoler.android.bakingapp.retrofit.ApiUtils;
import com.jogoler.android.bakingapp.retrofit.RecipeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    private static final String TAG = RecipeFragment.class.getName();

    public static final String ALL_RECIPES = "all_recipes";
    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_recipe, container, false);
        RecyclerView recipeItemRecyclerView;
        final RecipeAdapter recipeAdapter;

        recipeItemRecyclerView = (RecyclerView)rootView.findViewById(R.id.recipe_recycler_view);
        recipeAdapter = new RecipeAdapter((MainActivity)getActivity());
        recipeItemRecyclerView.setAdapter(recipeAdapter);

        if(rootView.getTag()!=null && rootView.getTag().equals(getString(R.string.landscape))){
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
            recipeItemRecyclerView.setLayoutManager(mLayoutManager);
        }else{
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recipeItemRecyclerView.setLayoutManager(mLayoutManager);
        }

        RecipeService recipeService = ApiUtils.getRecipeService();
        final Call<ArrayList<Recipe>> recipe = recipeService.getRecipe();
        final SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipes = response.body();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES,recipes);

                recipeAdapter.setRecipesList(recipes,getContext());
                if(idlingResource!=null){
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e("TAG","Failed to get data. " + t.getMessage());
            }
        });


        return rootView;
    }

}
