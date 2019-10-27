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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


public class ResultsActivity extends AppCompatActivity {

    TextView titleText, ratingText, descText, ingText, insnText;

    ToggleButton favs;

    private FirebaseAuth mAuth;

    String responseData;
    Recipe newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mAuth = getInstance();

        titleText = findViewById(R.id.recipe_title);
        ratingText = findViewById(R.id.rating_text);
        descText = findViewById(R.id.description_text);
        ingText = findViewById(R.id.ingredients_text);
        insnText = findViewById(R.id.instructions_text);

        favs = findViewById(R.id.toggleButton2);

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
                            ratingText.setText("Rating: " + newRecipe.getRating() + "/5.0");
                            ingText.setText(newRecipe.getIngredients());
                            descText.setText(newRecipe.getDescription());
                            insnText.setText(newRecipe.getInstructions());

                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            final String userUID = user.getUid();
                            System.out.println(userUID);
                            final DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("Users").document(userUID);

                            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        try {
                                            ArrayList<String> favorites = (ArrayList<String>) documentSnapshot.get("favsList");
                                            if(favorites.contains(newRecipe.getIndex()+"")) {
                                                favs.setChecked(true);
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(ResultsActivity.this, e.toString(),
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                            });

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

    protected void addFavs(View view){

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String userUID = user.getUid();
        System.out.println(userUID);
        final DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("Users").document(userUID);

        if(favs.isChecked()){
            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        try {
                            ArrayList<String> favorites = (ArrayList<String>) documentSnapshot.get("favsList");
                            favorites.add(newRecipe.getIndex() + "");
                            userDocRef.update("favsList", favorites);
                        } catch (Exception e) {
                            Toast.makeText(ResultsActivity.this, e.toString(),
                                    Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();

                            startActivity(new Intent(ResultsActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }
            });
        } else {
            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        try {
                            ArrayList<String> favorites = (ArrayList<String>) documentSnapshot.get("favsList");
                            favorites.remove(newRecipe.getIndex() + "");
                            userDocRef.update("favsList", favorites);
                        } catch (Exception e) {
                            Toast.makeText(ResultsActivity.this, e.toString(),
                                    Toast.LENGTH_LONG).show();

                            FirebaseAuth.getInstance().signOut();

                            startActivity(new Intent(ResultsActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }
            });

        }

    }
}
