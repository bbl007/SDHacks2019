import os
import pandas as pd
import numpy as np
from flask import Flask, request
#from flask_restful import Resource, Api
#import firebase_admin
#from firebase_admin import credentials

#cred = credentials.Certificate("ServiceAccountKey.json")
#firebase_admin.initialize_app(cred)

app = Flask(__name__)
#api = Api(app)

full_df = pd.read_csv('https://raw.githubusercontent.com/bbl007/SDHacks2019/master/image/full_df.csv')

@app.route('/')
def hello_world():
    target = os.environ.get('TARGET', 'World')
    return 'Hello {}!\n'.format(target)

@app.route('/test')
def test():
    target = os.environ.get('TARGET', 'World')
    return 'Test {}!\n'.format(target)

#class Recommender(Resource):
@app.route('/recommend')
def recommend():
    items = request.args
    ingredients = items.get('ing', '')
    ingredients = ingredients.split(',')
    ingredients = [' '.join(x.split('_')) for x in ingredients]
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


@app.route('/recipe')
def recipe():
    items = request.args
    idx = int(items.get('index', -1))
    recipe = full_df.iloc[idx]
    
    output = {}
    output['directions'] = recipe['directions']
    output['ingredients'] = recipe['ingredients']
    return output

if __name__ == '__main__':
    app.run(debug=True)
