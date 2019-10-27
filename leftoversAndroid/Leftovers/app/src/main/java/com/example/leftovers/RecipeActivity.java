package com.example.leftovers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    private Recipe chosen;

    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList.add(new Recipe(
           0, "BLT", 5
        ));

        recipeList.add(new Recipe(
                1, "Tomato Beef", 3
        ));

        adapter = new RecipeAdapter(this, recipeList);

        adapter.setOnItemClickListener(new RecipeAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(int position) {
                chosen = recipeList.get(position);

                System.out.println("LAUNCH RESULT");
                Intent launchResults = new Intent(RecipeActivity.this, ResultsActivity.class);
                launchResults.putExtra("chosen", chosen.getIndex() + "");
                startActivity(launchResults);
            }
        });

        recyclerView.setAdapter(adapter);
    }


}
