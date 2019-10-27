package com.example.leftovers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mCtx;
    private List<Recipe> recipeList;
    private OnItemCLickListener mListener;

    public interface OnItemCLickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemCLickListener listener){
        mListener = listener;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView name, rating;

        public RecipeViewHolder(@NonNull View itemView, final OnItemCLickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.recipe_name);
            rating = itemView.findViewById(R.id.recipe_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public RecipeAdapter(Context mCtx, List<Recipe> recipeList) {
        this.mCtx = mCtx;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recipe_layout, null);
        return new RecipeViewHolder(view, mListener);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder RecipeViewHolder, int i) {
        Recipe recipe = recipeList.get(i);

        RecipeViewHolder.name.setText(recipe.getName());
        RecipeViewHolder.rating.setText(String.valueOf(recipe.getRating()));
    }
}
