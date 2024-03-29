const express = require('express');
const mongoose = require('mongoose');
const {MONGO_URI, PORT} = require('./config/index.js');
const connectDB = require('./config/db.js')

const app = express();

connectDB(); 

app.listen(PORT);


