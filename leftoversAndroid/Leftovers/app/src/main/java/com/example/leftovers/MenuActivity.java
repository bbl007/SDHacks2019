package com.example.leftovers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private List<String> ingredients;
    private ArrayList<String> ingMatches;
    private String currIng;
    private String responseData;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AutoCompleteTextView typeIngredient;

    private ArrayList<Recipe> recipes;

    private ToggleButton toggleButton;
    private Button addBtn;
    private ImageButton menuBtn;

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

                if (actionId == EditorInfo.IME_ACTION_NEXT && event == null) {
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

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);


        menuBtn = (ImageButton) findViewById(R.id.imageButton_menu);
        // Shows settings when pressing the Settings Button
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to menu activity
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

}

