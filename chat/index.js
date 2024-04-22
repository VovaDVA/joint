const express = require('express');
const mongoose = require('mongoose');
const http = require('http');
const socketIO = require('socket.io');

const chatRouter = require('./routes/chatRoutes');
const messageRouter = require('./routes/messageRoutes')

//const models = require('./models');


const { db, PORT } = require('./config');
const app = express();
const server = http.createServer(app);
const io = socketIO(server);
const sockets = require('./sockets');

app.use(express.json());
app.use('/chat', chatRouter);
app.use('/message', messageRouter);

db.connectDB();

io.on("connection", (socket) => {
    console.log(`Подключение установлено ${socket.id}`);
    socket.emit('connection', 'OK');

    socket.on('message', (data) => {
        console.log(data);
        socket.emit('message', 'OK');
    })
    
    socket.on('disconnect', () => {
        console.log(`Подключение закрыто ${socket.id}`);
      });
});


server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`)
});