package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Listener { void onRecipeClick(Recipe recipe); }
    private List<Recipe> recipes; private final Listener listener;
    public RecipeAdapter(List<Recipe> recipes, Listener listener) { this.recipes = recipes; this.listener = listener; }
    public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; notifyDataSetChanged(); }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe,parent,false)); }
    @Override public void onBindViewHolder(@NonNull Holder h, int position) { Recipe r=recipes.get(position); h.name.setText(r.getName()); h.ingredients.setText(r.getIngredients().size()+" ingredients available"); h.itemView.setOnClickListener(v->listener.onRecipeClick(r)); }
    @Override public int getItemCount() { return recipes.size(); }
    static class Holder extends RecyclerView.ViewHolder { TextView name,ingredients; Holder(View v){super(v); name=v.findViewById(R.id.recipeName); ingredients=v.findViewById(R.id.recipeIngredients);} }
}
