package com.example.smartpantry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 3;
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        createTables(db);
        seedRecipes(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry_date TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, instructions TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL, ingredient_name TEXT NOT NULL, required_quantity REAL NOT NULL, unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Preserve existing pantry data when upgrading.
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, instructions TEXT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_INGREDIENTS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL, ingredient_name TEXT NOT NULL, required_quantity REAL NOT NULL, unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
            seedRecipes(db);
        }
    }

    public long insertPantryItem(String name, double quantity, String unit, String expiryDate) {
        ContentValues v = new ContentValues(); v.put("name", name); v.put("quantity", quantity); v.put("unit", unit); v.put("expiry_date", expiryDate); return getWritableDatabase().insert(TABLE_PANTRY, null, v);
    }
    public int updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        ContentValues v = new ContentValues(); v.put("name", name); v.put("quantity", quantity); v.put("unit", unit); v.put("expiry_date", expiryDate); return getWritableDatabase().update(TABLE_PANTRY, v, "id=?", new String[]{String.valueOf(id)});
    }
    public int deletePantryItem(int id) { return getWritableDatabase().delete(TABLE_PANTRY, "id=?", new String[]{String.valueOf(id)}); }
    public int clearPantry() { return getWritableDatabase().delete(TABLE_PANTRY, null, null); }
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_PANTRY, null, null, null, null, null, "name COLLATE NOCASE ASC");
        try { while (c.moveToNext()) items.add(new PantryItem(c.getInt(c.getColumnIndexOrThrow("id")), c.getString(c.getColumnIndexOrThrow("name")), c.getDouble(c.getColumnIndexOrThrow("quantity")), c.getString(c.getColumnIndexOrThrow("unit")), c.getString(c.getColumnIndexOrThrow("expiry_date")))); }
        finally { c.close(); } return items;
    }
    public PantryItem getPantryItem(int id) {
        Cursor c = getReadableDatabase().query(TABLE_PANTRY, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        try { if (c.moveToFirst()) return new PantryItem(c.getInt(c.getColumnIndexOrThrow("id")), c.getString(c.getColumnIndexOrThrow("name")), c.getDouble(c.getColumnIndexOrThrow("quantity")), c.getString(c.getColumnIndexOrThrow("unit")), c.getString(c.getColumnIndexOrThrow("expiry_date"))); return null; }
        finally { c.close(); }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_RECIPES, null, null, null, null, null, "name COLLATE NOCASE ASC");
        try {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                Recipe recipe = new Recipe(id, c.getString(c.getColumnIndexOrThrow("name")), c.getString(c.getColumnIndexOrThrow("instructions")));
                recipe.getIngredients().addAll(getRecipeIngredients(id));
                recipes.add(recipe);
            }
        } finally { c.close(); }
        return recipes;
    }

    public Recipe getRecipe(int recipeId) {
        Cursor c = getReadableDatabase().query(TABLE_RECIPES, null, "id=?", new String[]{String.valueOf(recipeId)}, null, null, null);
        try {
            if (!c.moveToFirst()) return null;
            Recipe recipe = new Recipe(recipeId, c.getString(c.getColumnIndexOrThrow("name")), c.getString(c.getColumnIndexOrThrow("instructions")));
            recipe.getIngredients().addAll(getRecipeIngredients(recipeId));
            return recipe;
        } finally { c.close(); }
    }

    private List<RecipeIngredient> getRecipeIngredients(int recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_RECIPE_INGREDIENTS, null, "recipe_id=?", new String[]{String.valueOf(recipeId)}, null, null, "id ASC");
        try { while (c.moveToNext()) list.add(new RecipeIngredient(c.getString(c.getColumnIndexOrThrow("ingredient_name")), c.getDouble(c.getColumnIndexOrThrow("required_quantity")), c.getString(c.getColumnIndexOrThrow("unit")))); }
        finally { c.close(); } return list;
    }

    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Tomato Omelette", "Beat the eggs, add chopped tomato and cheese, then cook in a lightly oiled pan until set.", new Object[][]{{"egg",2.0,"count"},{"tomato",1.0,"count"},{"cheese",50.0,"g"},{"oil",10.0,"ml"}});
        addRecipe(db, "Cheese Pasta", "Cook the pasta. Stir in cheese and milk over low heat until creamy. Season and serve.", new Object[][]{{"pasta",200.0,"g"},{"cheese",100.0,"g"},{"milk",100.0,"ml"},{"salt",2.0,"g"}});
        addRecipe(db, "Chicken Pasta", "Cook pasta and chicken separately. Combine with tomato and cheese, then heat through.", new Object[][]{{"chicken",200.0,"g"},{"pasta",200.0,"g"},{"tomato",2.0,"count"},{"cheese",50.0,"g"}});
        addRecipe(db, "Chicken Rice", "Cook rice. Pan-cook chicken with onion and carrot, then combine with the rice.", new Object[][]{{"chicken",200.0,"g"},{"rice",200.0,"g"},{"onion",1.0,"count"},{"carrot",1.0,"count"},{"oil",10.0,"ml"}});
        addRecipe(db, "Fried Rice", "Cook rice, then stir-fry it with egg, peas, carrot and onion.", new Object[][]{{"rice",250.0,"g"},{"egg",2.0,"count"},{"peas",80.0,"g"},{"carrot",1.0,"count"},{"onion",1.0,"count"},{"oil",10.0,"ml"}});
        addRecipe(db, "Tuna Sandwich", "Mix tuna with mayonnaise. Fill bread with the tuna mixture and sliced tomato.", new Object[][]{{"bread",2.0,"slice"},{"tuna",100.0,"g"},{"mayonnaise",30.0,"g"},{"tomato",1.0,"count"}});
        addRecipe(db, "Grilled Cheese Sandwich", "Place cheese between bread slices and toast in a pan until golden and melted.", new Object[][]{{"bread",2.0,"slice"},{"cheese",80.0,"g"},{"butter",10.0,"g"}});
        addRecipe(db, "Vegetable Stir Fry", "Stir-fry onion, carrot, peppers and peas in oil until tender but crisp.", new Object[][]{{"carrot",1.0,"count"},{"onion",1.0,"count"},{"pepper",1.0,"count"},{"peas",80.0,"g"},{"oil",10.0,"ml"}});
        addRecipe(db, "Chicken Wrap", "Cook seasoned chicken. Fill a wrap with chicken, lettuce and tomato.", new Object[][]{{"chicken",150.0,"g"},{"wrap",1.0,"count"},{"lettuce",50.0,"g"},{"tomato",1.0,"count"}});
        addRecipe(db, "Pancakes", "Whisk flour, milk and egg into a batter. Cook spoonfuls in a lightly oiled pan.", new Object[][]{{"flour",150.0,"g"},{"milk",250.0,"ml"},{"egg",1.0,"count"},{"sugar",20.0,"g"},{"oil",10.0,"ml"}});
        addRecipe(db, "French Toast", "Dip bread in beaten egg and milk, then fry in butter until golden.", new Object[][]{{"bread",2.0,"slice"},{"egg",2.0,"count"},{"milk",60.0,"ml"},{"butter",10.0,"g"}});
        addRecipe(db, "Mashed Potatoes", "Boil potatoes until tender, then mash with butter and milk. Season to taste.", new Object[][]{{"potato",300.0,"g"},{"butter",20.0,"g"},{"milk",80.0,"ml"},{"salt",2.0,"g"}});
        addRecipe(db, "Egg Fried Noodles", "Cook noodles. Stir-fry with egg, carrot and onion until combined.", new Object[][]{{"noodles",200.0,"g"},{"egg",2.0,"count"},{"carrot",1.0,"count"},{"onion",1.0,"count"},{"oil",10.0,"ml"}});
        addRecipe(db, "Tomato Rice", "Cook rice with chopped tomato, onion and oil until tender and fragrant.", new Object[][]{{"rice",200.0,"g"},{"tomato",2.0,"count"},{"onion",1.0,"count"},{"oil",10.0,"ml"}});
        addRecipe(db, "Chicken Salad", "Cook and slice chicken. Toss with lettuce, tomato and cucumber.", new Object[][]{{"chicken",150.0,"g"},{"lettuce",80.0,"g"},{"tomato",1.0,"count"},{"cucumber",1.0,"count"}});
        addRecipe(db, "Creamy Chicken", "Pan-cook chicken, add milk and cheese, and simmer until the sauce thickens.", new Object[][]{{"chicken",200.0,"g"},{"milk",150.0,"ml"},{"cheese",80.0,"g"},{"butter",10.0,"g"}});
        addRecipe(db, "Potato Omelette", "Cook diced potato and onion, add beaten eggs, and cook until firm.", new Object[][]{{"potato",200.0,"g"},{"egg",3.0,"count"},{"onion",1.0,"count"},{"oil",10.0,"ml"}});
        addRecipe(db, "Simple Tomato Soup", "Cook tomato and onion in water until soft, blend until smooth, and season.", new Object[][]{{"tomato",4.0,"count"},{"onion",1.0,"count"},{"water",400.0,"ml"},{"salt",2.0,"g"}});
        addRecipe(db, "Peanut Butter Toast", "Toast the bread and spread with peanut butter.", new Object[][]{{"bread",2.0,"slice"},{"peanut butter",30.0,"g"}});
        addRecipe(db, "Banana Pancakes", "Mash banana, mix with egg and flour, then cook small pancakes in an oiled pan.", new Object[][]{{"banana",1.0,"count"},{"egg",1.0,"count"},{"flour",100.0,"g"},{"oil",10.0,"ml"}});
    }

    private void addRecipe(SQLiteDatabase db, String name, String instructions, Object[][] ingredients) {
        ContentValues r = new ContentValues(); r.put("name", name); r.put("instructions", instructions);
        long recipeId = db.insert(TABLE_RECIPES, null, r);
        for (Object[] i : ingredients) {
            ContentValues v = new ContentValues(); v.put("recipe_id", recipeId); v.put("ingredient_name", (String)i[0]); v.put("required_quantity", (Double)i[1]); v.put("unit", (String)i[2]); db.insert(TABLE_RECIPE_INGREDIENTS, null, v);
        }
    }
}
