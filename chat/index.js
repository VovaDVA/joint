const express = require('express');
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
const messageRouter = require('./routes/messageRoutes');

const { db, PORT } = require('./config');
const sockets = require('./sockets');
sockets(io);

app.use(express.json());
app.use('/chat', chatRouter);
app.use('/message', messageRouter);

db.connectDB();

server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});