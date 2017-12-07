package com.jogoler.android.bakingapp;

import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.jogoler.android.bakingapp.adapter.RecipeDetailAdapter;
import com.jogoler.android.bakingapp.fragment.RecipeDetailFragment;
import com.jogoler.android.bakingapp.fragment.RecipeStepDetailFragment;
import com.jogoler.android.bakingapp.pojo.Recipe;
import com.jogoler.android.bakingapp.pojo.Step;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener,
        RecipeStepDetailFragment.ItemClickListner {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String TITLE = "title";
    public String recipeName;
    private ArrayList<Recipe> recipeArrayList;
    static String SELECTED_RECIPES = "selected_recipe";
    static String SELECTED_STEP = "selected_step";
    static String SELECTED_INDEX = "selected_index";
    static String STACK_RECIPE_DETAIL = "STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL = "STACK_RECIPE_STEP_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setTitleTextColor(Color.WHITE);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle recipeSelectedBundle = getIntent().getExtras();

            recipeArrayList = new ArrayList<>();
            recipeArrayList = recipeSelectedBundle.getParcelableArrayList(SELECTED_RECIPES);
            Log.d(TAG, recipeArrayList.size() + "");
            recipeName = recipeArrayList.get(0).getName();

            final RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(recipeSelectedBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, fragment).addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

            if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals(getString(R.string.tableLandscape))) {

                final RecipeStepDetailFragment fragment2 = new RecipeStepDetailFragment();
                fragment2.setArguments(recipeSelectedBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container_fragment2, fragment2).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();

            }


        } else {
            recipeName = savedInstanceState.getString(TITLE);
        }
        getSupportActionBar().setTitle(recipeName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (findViewById(R.id.container_fragment2) == null) {
                    if (fragmentManager.getBackStackEntryCount() > 1) {
                        fragmentManager.popBackStack(STACK_RECIPE_DETAIL, 0);
                    } else if (fragmentManager.getBackStackEntryCount() > 0) {
                        finish();
                    }
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onListItemClickListener(List<Step> step, int index, String recipeName) {
        final RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEP, (ArrayList<Step>) step);
        stepBundle.putInt(SELECTED_INDEX, index);
        stepBundle.putString(TITLE, recipeName);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals(getString(R.string.tableLandscape))) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment2, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(TITLE, recipeName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
