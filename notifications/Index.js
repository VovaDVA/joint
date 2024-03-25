const express = require('express');
const mongoose = require('mongoose');

const app = express();

const PORT = 3002;

app.get('/', (req, res) => {
    res.send('Hekko World!')
})

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});