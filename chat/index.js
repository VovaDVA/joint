const express = require('express');
const mongoose = require('mongoose');
const http = require('http');
//const socketIO = require('socket.io');

const chatRouter = require('./routes/chatRoutes');
const messageRouter = require('./routes/messageRoutes')

//const models = require('./models');
//const sockets = require('./sockets');

const { db, PORT } = require('./config');

const app = express();
const server = http.createServer(app);
//const io = socketIO(server);

app.use(express.json());
app.use('/chat', chatRouter);
app.use('/message', messageRouter);

db.connectDB();
server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`)
});