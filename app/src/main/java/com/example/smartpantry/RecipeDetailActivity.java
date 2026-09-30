package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantry.database.DatabaseHelper;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState){ super.onCreate(savedInstanceState); setContentView(R.layout.activity_recipe_detail); int id=getIntent().getIntExtra("RECIPE_ID",-1); Recipe r=new DatabaseHelper(this).getRecipe(id); if(r==null){finish();return;} ((TextView)findViewById(R.id.detailName)).setText(r.getName()); StringBuilder b=new StringBuilder("Ingredients\n\n"); for(RecipeIngredient i:r.getIngredients()) b.append("• ").append(i.getName()).append(" — ").append(i.getQuantity()).append(" ").append(i.getUnit()).append("\n"); ((TextView)findViewById(R.id.detailIngredients)).setText(b.toString()); ((TextView)findViewById(R.id.detailMethod)).setText("Preparation\n\n"+r.getInstructions()); }
}
