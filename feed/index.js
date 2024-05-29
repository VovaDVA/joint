const cors = require('cors');

const mongoose = require('mongoose');
const {MONGO_URI, PORT} = require('./config/index');
const connectDB = require('./config/db');
const http = require('http');
const socketIO = require('socket.io');
const sockets = require('./sockets');

const app = require('./app');

app.use(cors());

const server = http.createServer(app);
const io = socketIO(server);

connectDB(); 

sockets(io);

server.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});