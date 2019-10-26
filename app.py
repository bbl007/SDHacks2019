import pandas as pd
import numpy as np
from flask import Flask, request
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)

full_df = pd.read_csv('../data/test_df.csv')

class Recommender(Resource):
    def get(self):
        items = request.args
        ingredients = items.get('ing', -1)
        ingredients = ingredients.split(',')
        n = items.get('n', 10)
        
        similarity = np.zeros(len(full_df))
        for ing in ingredients:
            similarity += full_df[ing]

        food_sim = pd.DataFrame({'similarity':similarity})
        top_n = list(food_sim.sort_values(by=['similarity'], ascending=False).index)[:n]
        top_meals = full_df.iloc[top_n]
        
        
        output = {}
        output['title'] = list(top_meals['title'].values)
        output['rating'] = list(top_meals['rating'].values)
        output['index'] = top_n  
        return output

class Ingredients_Recipe(Resource):
    def get(self):
        items = request.args
        idx = int(items.get('index', -1))
        recipe = full_df.iloc[idx]
        
        output = {}
        output['directions'] = recipe['directions']
        output['ingredients'] = recipe['ingredients']
        output['description'] = recipe['desc'] 
        return output


api.add_resource(Recommender, '/recommend')
api.add_resource(Ingredients_Recipe, '/recipe')

if __name__ == '__main__':
    app.run(debug=True)