# Project Handoff

## Main Java classes
- `MainActivity` — pantry screen, navigation and delete confirmation.
- `AddEditIngredientActivity` — create/update pantry items and input validation.
- `SuggestedRecipesActivity` — displays strict recipe matches.
- `RecipeDetailActivity` — displays selected recipe details.
- `SettingsActivity` — preferences and pantry reset.
- `DatabaseHelper` — SQLite schema, CRUD operations and recipe seeding.
- `RecipeMatcher` — strict ingredient/quantity matching and basic normalisation.
- `PantryAdapter` — RecyclerView adapter for pantry items.
- `RecipeAdapter` — RecyclerView adapter for recipes.

## Important logic
`RecipeMatcher.getStrictMatches()` builds a total available quantity for each normalised pantry ingredient. Every ingredient in a recipe is then checked. If any ingredient is missing or below the required quantity, the entire recipe is excluded.

## Before submission
- Run the app in Android Studio and test every screen.
- Capture real screenshots from the running app for the report.
- Record the required 5–7 minute demonstration with your own narration.
- Create/push a public GitHub repository and build a genuine commit history of your development work.
- Prepare the written report using the assignment's required structure.
- Remove build/cache folders and keep the final ZIP below 50 MB.
