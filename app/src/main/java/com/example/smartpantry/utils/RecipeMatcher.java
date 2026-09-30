package com.example.smartpantry.utils;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeMatcher {
    public static List<Recipe> getStrictMatches(List<PantryItem> pantry, List<Recipe> recipes) {
        java.util.ArrayList<Recipe> matches = new java.util.ArrayList<>();
        Map<String, Double> pantryTotals = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = normalize(item.getName());
            Double amount = convertToBase(item.getQuantity(), item.getUnit());
            if (amount != null) pantryTotals.put(key, pantryTotals.getOrDefault(key, 0.0) + amount);
        }
        for (Recipe recipe : recipes) {
            boolean canMake = true;
            for (RecipeIngredient needed : recipe.getIngredients()) {
                String key = normalize(needed.getName());
                Double available = pantryTotals.get(key);
                Double required = convertToBase(needed.getQuantity(), needed.getUnit());
                if (available == null || required == null || available + 0.0001 < required) { canMake = false; break; }
            }
            if (canMake) matches.add(recipe);
        }
        return matches;
    }

    public static String normalize(String name) {
        String s = name.toLowerCase(Locale.ROOT).trim();
        if (s.endsWith("ies") && s.length() > 3) s = s.substring(0, s.length()-3) + "y";
        else if (s.endsWith("oes") && s.length() > 3) s = s.substring(0, s.length()-2);
        else if (s.endsWith("s") && !s.endsWith("ss") && s.length() > 2) s = s.substring(0, s.length()-1);
        return s;
    }

    private static Double convertToBase(double quantity, String unit) {
        String u = unit.toLowerCase(Locale.ROOT).trim();
        if (u.equals("kg") || u.equals("kilogram") || u.equals("kilograms")) return quantity * 1000;
        if (u.equals("g") || u.equals("gram") || u.equals("grams")) return quantity;
        if (u.equals("l") || u.equals("litre") || u.equals("litres") || u.equals("liter") || u.equals("liters")) return quantity * 1000;
        if (u.equals("ml") || u.equals("millilitre") || u.equals("millilitres") || u.equals("milliliter") || u.equals("milliliters")) return quantity;
        if (u.equals("count") || u.equals("piece") || u.equals("pieces") || u.equals("slice") || u.equals("slices")) return quantity;
        return quantity;
    }
}
