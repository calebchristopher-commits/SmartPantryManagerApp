package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.database.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;

public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {
    private DatabaseHelper db; private PantryAdapter adapter; private TextView emptyPantryMessage;
    @Override protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_main); db = new DatabaseHelper(this); emptyPantryMessage=findViewById(R.id.emptyPantryMessage); RecyclerView rv=findViewById(R.id.recyclerPantry); rv.setLayoutManager(new LinearLayoutManager(this)); adapter=new PantryAdapter(db.getAllPantryItems(),this); rv.setAdapter(adapter); Button add=findViewById(R.id.btnAddIngredient); add.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class))); Button recipes=findViewById(R.id.btnSuggestedRecipes); recipes.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class))); Button settings=findViewById(R.id.btnSettings); settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class))); refreshEmptyState(); }
    @Override protected void onResume() { super.onResume(); if (adapter != null) { adapter.setItems(db.getAllPantryItems()); refreshEmptyState(); } }
    @Override public void onEdit(PantryItem item) { Intent i=new Intent(this,AddEditIngredientActivity.class); i.putExtra("ITEM_ID",item.getId()); startActivity(i); }
    @Override public void onDelete(PantryItem item) { new AlertDialog.Builder(this).setTitle("Delete ingredient?").setMessage("Remove " + item.getName() + " from your pantry?").setPositiveButton("Delete",(d,w)->{db.deletePantryItem(item.getId()); adapter.setItems(db.getAllPantryItems()); refreshEmptyState(); Toast.makeText(this,"Ingredient deleted",Toast.LENGTH_SHORT).show();}).setNegativeButton("Cancel",null).show(); }
    private void refreshEmptyState() { if (emptyPantryMessage != null) emptyPantryMessage.setVisibility(db.getAllPantryItems().isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE); }
}
