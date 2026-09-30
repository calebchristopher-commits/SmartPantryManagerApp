package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantry.adapter.RecipeAdapter;
import com.example.smartpantry.database.DatabaseHelper;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.utils.RecipeMatcher;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.Listener {
    private DatabaseHelper db; private RecipeAdapter adapter; private TextView emptyMessage;
    @Override protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_suggested_recipes); db=new DatabaseHelper(this); emptyMessage=findViewById(R.id.emptyMessage); RecyclerView rv=findViewById(R.id.recyclerRecipes); rv.setLayoutManager(new LinearLayoutManager(this)); adapter=new RecipeAdapter(java.util.Collections.emptyList(),this); rv.setAdapter(adapter); }
    @Override protected void onResume(){ super.onResume(); List<Recipe> matches=RecipeMatcher.getStrictMatches(db.getAllPantryItems(),db.getAllRecipes()); adapter.setRecipes(matches); emptyMessage.setVisibility(matches.isEmpty()?android.view.View.VISIBLE:android.view.View.GONE); }
    @Override public void onRecipeClick(Recipe recipe){ Intent i=new Intent(this,RecipeDetailActivity.class); i.putExtra("RECIPE_ID",recipe.getId()); startActivity(i); }
}
