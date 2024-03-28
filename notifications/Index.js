const express = require('express');
const mongoose = require('mongoose');
const http = require('http');
const { MONGO_URI, PORT } = require('./config');

const app = express();
const server = http.createServer(app);

mongoose.connect(MONGO_URI);

app.get('/', (req, res) => {
    res.send('Hello World!')
})

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});

mongoose
 .connect(MONGO_URI)
 .then((res) => console.log('MongoDB connected'))
 .catch ((error) => console.log(error));
