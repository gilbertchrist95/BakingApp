package com.jogoler.android.bakingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jogoler.android.bakingapp.DetailActivity;
import com.jogoler.android.bakingapp.R;
import com.jogoler.android.bakingapp.adapter.RecipeDetailAdapter;
import com.jogoler.android.bakingapp.pojo.Ingredient;
import com.jogoler.android.bakingapp.pojo.Recipe;
import com.jogoler.android.bakingapp.widget.BakingAppUpdateService;

import java.util.ArrayList;
import java.util.List;

import static com.jogoler.android.bakingapp.MainActivity.SELECTED_RECIPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String TITLE = "title";
    ArrayList<Recipe> recipeArrayList;
    String recipeName;


    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        RecyclerView detailRecyclerView;
        RecipeDetailAdapter recipeDetailAdapter;
        TextView ingredientTextView;
        recipeArrayList = new ArrayList<>();
        ArrayList<String> ingredientsWidget = new ArrayList<>();
        recipeDetailAdapter = new RecipeDetailAdapter((DetailActivity)getActivity());

        ingredientTextView = (TextView)rootView.findViewById(R.id.ingredient_text_view);
        detailRecyclerView = (RecyclerView)rootView.findViewById(R.id.recipe_detail_recycler_view);



        if(savedInstanceState!=null){
            recipeArrayList = savedInstanceState.getParcelableArrayList(SELECTED_RECIPE);
        }else{
            recipeArrayList  =getArguments().getParcelableArrayList(SELECTED_RECIPE);
        }

        List<Ingredient> ingredientList = recipeArrayList.get(0).getIngredients();
        recipeName = recipeArrayList.get(0).getName();

        for(Ingredient ingredient: ingredientList){
            ingredientTextView.append(""+ingredient.getIngredient()+"\n");
            ingredientTextView.append("\t\t\t Quantity: " + ingredient.getQuantity() + "\n");
            ingredientTextView.append("\t\t\t Measure: " + ingredient.getMeasure() + "\n\n");

            ingredientsWidget.add(ingredient.getIngredient()+" \n"+
                "\t Quantity: "+ingredient.getQuantity()+ " \n"+
                "\t Measure: "+ingredient.getMeasure()+ "\n");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        detailRecyclerView.setLayoutManager(layoutManager);
        detailRecyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.setStepRecipeData(recipeArrayList,getContext());

        BakingAppUpdateService.startBakingService(getContext(),ingredientsWidget);


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SELECTED_RECIPE, recipeArrayList);
        outState.putString(TITLE, recipeName);
    }
}
