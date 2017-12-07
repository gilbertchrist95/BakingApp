package com.jogoler.android.bakingapp.adapter;

import android.content.Context;
import android.support.test.filters.LargeTest;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jogoler.android.bakingapp.R;
import com.jogoler.android.bakingapp.pojo.Recipe;
import com.jogoler.android.bakingapp.pojo.Step;

import java.util.List;

/**
 * Created by Gilbert on 9/17/2017.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeViewHolder> {

    List<Step> stepList;
    private String recipeName;
    final private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener{
        void onListItemClickListener(List<Step> step, int index, String recipeName);

    }

    public RecipeDetailAdapter(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setStepRecipeData(List<Recipe> recipes, Context context){
        stepList = recipes.get(0).getSteps();
        recipeName = recipes.get(0).getName();
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recipe_detail_items,parent,false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.stepDescriptionTextView.setText(stepList.get(position).getId()+". "+
            stepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepList!=null?stepList.size():0;
    }

    public class RecipeViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView stepDescriptionTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            stepDescriptionTextView = (TextView)itemView.findViewById(R.id.step_description_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemClickListener.onListItemClickListener(stepList,position,recipeName);
        }
    }
}
