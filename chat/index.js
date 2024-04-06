// const express = require('express');
// const mongoose = require('mongoose');
// const http = require('http');

// //const models = require('./models');
// //const socketIO = require('socket.io');
// //const routes = require('./routes');
// //const sockets = require('./sockets');

// const { db, PORT } = require('./config');

// const app = express();
// const server = http.createServer(app);
// //const io = socketIO(server);

// app.use(express.json());
// //app.use('/api', routes);
// //sockets(io);

// db.connectDB();
// server.listen(PORT, () => {
//     console.log(`Server is running on http://localhost:${PORT}`)
// });

const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
// const io = socketIo(server);
const io = require('socket.io')(server, {
    cors: {
        origin: '*',
    }
})

// Обработка подключения нового клиента к сокету
io.on('connection', (socket) => {
    console.log('a user connected');

    // Обработка отправки сообщения от клиента
    socket.on('chat message', (msg) => {
        console.log('message: ' + msg);

        // Отправить сообщение всем подключенным клиентам
        io.emit('chat message', msg);
    });

    // Обработка отключения клиента от сокета
    socket.on('disconnect', () => {
        console.log('user disconnected');
    });
});

// Запуск сервера на порте 3000
server.listen(3000, () => {
    console.log('listening on *:3000');
});
