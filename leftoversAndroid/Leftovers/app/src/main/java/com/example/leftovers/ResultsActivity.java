package com.example.leftovers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultsActivity extends AppCompatActivity {

    TextView titleText, ratingText, descText, ingText, insnText;

    String responseData;
    Recipe newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        titleText = findViewById(R.id.recipe_title);
        ratingText = findViewById(R.id.recipe_rating);
        descText = findViewById(R.id.description_text);
        ingText = findViewById(R.id.ingredients_text);
        insnText = findViewById(R.id.instructions_text);

        Intent currIntent = getIntent();
        String recInd = (currIntent.getExtras().getString("chosen"));
        System.out.println(recInd);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://working-u55kjwwuea-uc.a.run.app/recipe?index=" + recInd;
        System.out.println(url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("RESPONSE:", response);
                        responseData = response;
                        System.out.println(responseData);

                        JsonObject jsonObject = new JsonParser().parse(responseData).getAsJsonObject();

                        JsonArray arr = jsonObject.getAsJsonArray("items");
                        for (int i = 0; i < arr.size(); i++) {
                            int index = Integer.parseInt(arr.get(0).getAsJsonObject().get("index").getAsString());
                            String title = arr.get(0).getAsJsonObject().get("title").getAsString();
                            double rating = Double.parseDouble(arr.get(0).getAsJsonObject().get("rating").getAsString());
                            String description = arr.get(0).getAsJsonObject().get("description").getAsString();
                            String ingredients = arr.get(0).getAsJsonObject().get("ingredients").getAsString();
                            String instructions = arr.get(0).getAsJsonObject().get("directions").getAsString();

                            System.out.println("HELLOOOOO");

                            newRecipe = new Recipe(index, title, ingredients, description, instructions, rating);

                            titleText.setText(newRecipe.getName());
                            ingText.setText(newRecipe.getIngredients());
                            descText.setText(newRecipe.getDescription());
                            insnText.setText(newRecipe.getInstructions());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR:", "That didn't work!");
            }
        });

        queue.add(stringRequest);







    }

}
