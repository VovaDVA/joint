const express = require('express');
const mongoose = require('mongoose');
const http = require('http');

//const models = require('./models');
//const socketIO = require('socket.io');
//const routes = require('./routes');
//const sockets = require('./sockets');

const { db, PORT } = require('./config');

const app = express();
const server = http.createServer(app);
//const io = socketIO(server);

app.use(express.json());
//app.use('/api', routes);
//sockets(io);

db.connectDB();
server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`)
});