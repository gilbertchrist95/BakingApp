package com.jogoler.android.bakingapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jogoler.android.bakingapp.R;
import com.jogoler.android.bakingapp.pojo.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gilbert on 9/16/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{
    ArrayList<Recipe> mRecipeList;
    Context mContext;
    private RecipeItemListener mRecipeItemListener;

    public interface RecipeItemListener{
        void onItemClickListener(Recipe clickedItemRecipe);
    }

    public RecipeAdapter(RecipeItemListener mRecipeItemListener) {
        this.mRecipeItemListener = mRecipeItemListener;
    }

    public void setRecipesList(ArrayList<Recipe> recipeList,Context context){
        this.mRecipeList = recipeList;
        this.mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_items,parent,false);
        RecipeAdapterViewHolder viewHolder  = new RecipeAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.titleTextView.setText(mRecipeList.get(position).getName());
        String urlImage = mRecipeList.get(position).getImage();

        if(!urlImage.equals("")){
            Picasso.with(mContext).load(urlImage).into(holder.recipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList!=null? mRecipeList.size():0;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleTextView;
        ImageView recipeImageView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView)itemView.findViewById(R.id.title_text_view);
            recipeImageView = (ImageView)itemView.findViewById(R.id.recipe_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mRecipeItemListener.onItemClickListener(mRecipeList.get(position));
        }
    }
}
