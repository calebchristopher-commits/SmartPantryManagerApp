package com.example.smartpantry.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String instructions;
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe(int id, String name, String instructions) {
        this.id = id; this.name = name; this.instructions = instructions;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void addIngredient(RecipeIngredient ingredient) { ingredients.add(ingredient); }
}
