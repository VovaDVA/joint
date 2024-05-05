const express = require('express');
const app = express();

const chatRouter = require('./routes/chatRoutes');
const messageRouter = require('./routes/messageRoutes');

app.use(express.json());
app.use('/chat', chatRouter);
app.use('/message', messageRouter);

module.exports = app;