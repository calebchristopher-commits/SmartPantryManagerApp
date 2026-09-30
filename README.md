# Smart Pantry Manager

A Java Android application for Mobile App Development 700. The app stores pantry ingredients locally with SQLite and suggests recipes only when **every required ingredient and required quantity** is available.

## Features
- Add, edit and delete pantry ingredients.
- Optional expiry date field.
- Pantry data persists locally with SQLite.
- 20 pre-loaded recipes stored in SQLite.
- Strict recipe matching: a recipe is suggested only when every ingredient and required quantity is available.
- Basic ingredient normalisation for singular/plural names such as `tomato` / `tomatoes`.
- Basic unit conversion for kg/g and L/ml.
- Suggested Recipes list with an empty-state message.
- Recipe Detail screen with ingredients and preparation method.
- Settings screen with expiry-alert preference and clear-pantry action.
- RecyclerView + custom adapters.
- Activities and Intents for navigation.

## Screens
1. Pantry
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

## Database
SQLite is used because the assignment requires persistent local data and the application does not need a remote server. The database contains:
- `pantry_items`
- `recipes`
- `recipe_ingredients`

The database upgrade path preserves existing pantry data when recipe functionality is added.

## Android Studio setup
1. Open Android Studio.
2. Select **Open** and choose the `SmartPantryManager` folder.
3. Allow Android Studio to sync the Gradle project and download dependencies.
4. Use a JDK supported by your installed Android Studio/Android Gradle Plugin.
5. Create an Android emulator or connect an Android phone with USB debugging enabled.
6. Press **Run** and select the emulator/device.

If Android Studio asks to create/update the Gradle wrapper, allow it to do so.

## Test the strict-matching rule
Example:
- Add `Chicken` = 200 g.
- Add `Pasta` = 200 g.
- Add `Tomato` = 2 count.
- Open Suggested Recipes. `Chicken Pasta` should **not** appear because 50 g cheese is missing.
- Add `Cheese` = 50 g.
- Return to Suggested Recipes. `Chicken Pasta` should now appear.

## GitHub
The assignment requires a public GitHub repository with at least 10 genuine, meaningful commits. Do not create artificial history. Commit actual development milestones as you work.

Suggested commit sequence:
1. Create Android project structure
2. Add pantry data model
3. Implement SQLite pantry table
4. Implement pantry CRUD
5. Add RecyclerView pantry adapter
6. Add recipe database tables
7. Seed 20 recipes
8. Implement strict recipe matcher
9. Add recipe list and detail screens
10. Add Settings and improve validation/UI
11. Final testing and README update

## Important submission note
Do not include `build/` or `.gradle/` folders in the final submission ZIP. The assignment ZIP must contain the full source project, your 5–7 minute demonstration video and your written report, and must remain under 50 MB.
