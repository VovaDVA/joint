const express = require('express');
// const mongoose = require('mongoose');
const app = express();
const http = require('http');
const server = http.createServer(app);
const { Server } = require("socket.io");

const io = new Server(server, {
    cors: {
        origin: "*"
    }
});

const chatRouter = require('./routes/chatRoutes');
const messageRouter = require('./routes/messageRoutes')

//const models = require('./models');

const { db, PORT } = require('./config');
const messageService = require('./services/messageService');
// const sockets = require('./sockets');

app.use(express.json());
app.use('/chat', chatRouter);
app.use('/message', messageRouter);

db.connectDB();

io.on("connection", (socket) => {
    console.log(`Подключение установлено ${socket.id}`);
    socket.emit('connection', 'OK');

    const chatId = socket.handshake.query.chatId;
    socket.join(chatId);

    socket.on('typing', () => {
        socket.broadcast.to(chatId).emit('typing');
    });

    socket.on('stopTyping', () => {
        socket.broadcast.to(chatId).emit('stopTyping');
    });

    socket.on('sendMessage', (message) => {
        console.log(message);
        io.to(chatId).emit('message', message);
        messageService.createMessage(message);
    });
    
    socket.on('disconnect', () => {
        console.log(`Подключение закрыто ${socket.id}`);
      });
});


server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`)
});
