const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());

const chatRouter = require('./routes/chatRoutes');
const messageRouter = require('./routes/messageRoutes');

app.use(express.json());
app.use('/chat', chatRouter);
app.use('/message', messageRouter);

module.exports = app;