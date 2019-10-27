package com.example.leftovers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> ingredients;
    private ArrayList<String> ingMatches;
    private String currIng;
    private String responseData;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AutoCompleteTextView typeIngredient;

    private ArrayList<Recipe> recipes;

    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ingredients = Arrays.asList(new Ingredients().getIngredients());
        ingMatches = new ArrayList<String>();

        // Autocomplete ingredients
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ingredients);
        typeIngredient = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteIngred);
        typeIngredient.setAdapter(adapter);
        typeIngredient.setInputType(InputType.TYPE_CLASS_TEXT);

        typeIngredient.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                System.out.println(actionId);
                System.out.println(event);

                if( actionId == EditorInfo.IME_ACTION_NEXT && event == null) {
                    addIngredient();
                    handled = true;
                }
                return handled;
            }
        });

        addBtn = (Button) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        // Scrollable ingredients list
        recyclerView = (RecyclerView) findViewById(R.id.ingred_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    protected void toRecipeList(View view){
        if(ingMatches.size() > 0){
            String requestStr = getRequestStr();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://working-u55kjwwuea-uc.a.run.app" + requestStr;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("RESPONSE:", response);
                            responseData = response;
                            System.out.println(responseData);

                            recipes = new ArrayList<Recipe>();

                            JsonObject jsonObject = new JsonParser().parse(responseData).getAsJsonObject();

                            JsonArray arr = jsonObject.getAsJsonArray("items");
                            for (int i = 0; i < arr.size(); i++) {
                                int index = Integer.parseInt(arr.get(i).getAsJsonObject().get("index").getAsString());
                                String title = arr.get(i).getAsJsonObject().get("title").getAsString();
                                double rating = Double.parseDouble(arr.get(i).getAsJsonObject().get("rating").getAsString());

                                Recipe newRecipe = new Recipe(index, title, rating);
                                recipes.add(newRecipe);
                            }

                            System.out.println(recipes);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR:", "That didn't work!");
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);


            if(responseData != null) {

                Toast.makeText(this, "HELLO" + responseData.indexOf(']'), Toast.LENGTH_LONG).show();


                System.out.println("LAUNCH RECIPES");
                Intent launchRecipes = new Intent(this, RecipeActivity.class);

                launchRecipes.putExtra("recipes", recipes);
                startActivity(launchRecipes);
            } else {
                Toast.makeText(this, "Failed to get response", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Add Ingredients", Toast.LENGTH_SHORT).show();
        }


    }
/*
    private void jsonParse() {

        String url = "https://api.myjson.com/bins/kp9wz";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("indices");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                String firstName = employee.getString("indices");
                                int age = employee.getInt("age");
                                String mail = employee.getString("mail");

                                mTextViewResult.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
*/
    protected void addIngredient(){
        currIng = typeIngredient.getText().toString();
        if(!currIng.equals("")) {
            if (!ingMatches.contains(currIng)) {
                if (ingredients.contains(currIng))
                    ingMatches.add(currIng);
                else
                    Toast.makeText(this, "This ingredient is not used in any recipes", Toast.LENGTH_SHORT).show();
            } else
            Toast.makeText(this, "Ingredient already added", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Please enter the name of the ingredient", Toast.LENGTH_SHORT).show();

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(ingMatches);
        recyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        System.out.println(ingMatches);
        typeIngredient.selectAll();

    }

    private ArrayList<String> autoComplete(String currStr){
        ArrayList<String> matches = new ArrayList<String>();
        for(String ingredient : ingredients){
            if(ingredient.contains(currStr)){
                matches.add(ingredient);
                System.out.println(currStr);
            }
        }

        return matches;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ingMatches.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();
        }
    };

    private String getRequestStr(){
        String requestStr = "/recommend?ing=" + ingMatches.get(0);
        for(String match: ingMatches) {
            requestStr = requestStr + "," + match;
        }

        requestStr = requestStr.replace(" ", "_");
        System.out.println(requestStr);
        return requestStr;
    }
}
