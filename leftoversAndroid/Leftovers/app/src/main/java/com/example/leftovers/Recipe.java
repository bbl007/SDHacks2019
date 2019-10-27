package com.example.leftovers;

public class Recipe implements java.io.Serializable {
    private String name;
    private String ingredients;
    private String description;

    private String instructions;
    private double rating;
    private int index;

    public Recipe(int index, String name, String ingredients, String description, String instructions, double rating) {
        this.index = index;
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.instructions = instructions;
        this.rating = rating;
    }

    public Recipe(int index, String name, double rating) {
        this.index = index;
        this.name = name;
        this.rating = rating;
    }

    public void changeText1(String text){
        name = text;
    }

    public int getIndex(){
        return index;
    }

    public String getInstructions(){
        return instructions;
    }

    public void setInstructions(String instructions){
        this.instructions = instructions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }
}
