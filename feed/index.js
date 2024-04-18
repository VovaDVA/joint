const express = require('express');
const mongoose = require('mongoose');
const {MONGO_URI, PORT} = require('./config/index');
const connectDB = require('./config/db');
const http = require('http');
const socketIO = require('socket.io');
const sockets = require('./sockets');

const postRouter = require('./routes/postRoutes');
const commentRouter = require('./routes/commentRoutes');
const reactionRouter = require('./routes/reactionRoutes');

const app = express();
const server = http.createServer(app);
const io = socketIO(server);

connectDB(); 

app.use(express.json());
app.use('/post', postRouter);
app.use('/comment', commentRouter);
app.use('/reaction', reactionRouter);

sockets(io);

server.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});