package com.example.leftovers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> ingredients;
    private ArrayList<String> ingMatches;
    private String currIng;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AutoCompleteTextView typeIngredient;

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



        // Scrollable ingredients list
        recyclerView = (RecyclerView) findViewById(R.id.ingred_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    protected void addIngredient(View view){
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
}
