import pandas as pd
import numpy as np
from flask import Flask, request
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)

full_df = pd.read_csv('../data/test_df.csv')

class Recommender(Resource):
    def get(self):
        items = request.args['ings']
        food_items = items.split(',')
        output = np.zeros(len(full_df))
        for food in food_items:
            output += full_df[food]

        food_sim = pd.DataFrame({'similarity':output})
        top_10 = list(food_sim.sort_values(by=['similarity'], ascending=False).index)
        
        return {'data':list([full_df.iloc[top_10].values[0][9]])}

api.add_resource(Recommender, '/recommend')

if __name__ == '__main__':
    app.run(debug=True)