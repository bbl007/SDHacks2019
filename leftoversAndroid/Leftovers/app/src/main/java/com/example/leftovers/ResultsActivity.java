package com.example.leftovers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultsActivity extends AppCompatActivity {

    TextView titleText, ratingText, descText, ingText, insnText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        titleText = findViewById(R.id.recipe_title);
        ratingText = findViewById(R.id.recipe_rating);

        Intent currIntent = getIntent();
        String recInd = (currIntent.getExtras().getString("chosen"));

        String url ="https://test1-u55kjwwuea-uc.a.run.app/recipe?index=" + recInd;
        System.out.println(url);



    }

}
