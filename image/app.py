import os
import pandas as pd
import numpy as np
from flask import Flask, request

app = Flask(__name__)


full_df = pd.read_csv('https://raw.githubusercontent.com/bbl007/SDHacks2019/master/image/full_df.csv')

@app.route('/')
def hello_world():
    target = os.environ.get('TARGET', 'World')
    return 'Hello {}!\n'.format(target)

@app.route('/test')
def test():
    target = os.environ.get('TARGET', 'World')
    return 'Test {}!\n'.format(target)


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
    
    title = list(top_meals['title'].values)
    rating = list(top_meals['rating'].values)
    index = top_n
    output_list = []
    for i in range(len(index)):
        temp = {}    
        temp['title'] = title[i]
        temp['rating'] = rating[i]
        temp['index'] = index[i]
        output_list.append(temp)
        
    output = {'items':output_list}
    return output


@app.route('/recipe')
def recipe():
    items = request.args
    idx = int(items.get('index', -1))
    recipe = full_df.iloc[idx]
    
    output = {}
    output['index'] = recipe['index']
    output['directions'] = '\n'.join([d for d in eval(recipe['directions'])])
    output['ingredients'] = '\n'.join([r for r in eval(recipe['ingredients'])])
    output['description'] = recipe['desc'] if pd.notnull(recipe['desc']) else ''
    output['rating'] = recipe['rating']
    output['title'] = recipe['title']
    
    return {'items':[output]}

if __name__ == '__main__':
    app.run(debug=True)
