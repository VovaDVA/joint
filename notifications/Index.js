const express = require('express');
const cors = require('cors');
const app = express();

const mongoose = require('mongoose');
const http = require('http');
const socketIO = require('socket.io');
const sockets = require('./sockets');
const { MONGO_URI, PORT } = require('./config/index');
const connectDB = require('./config/db');

app.use(cors());

const notificationRoutes = require('./routes/notificationsRoutes');
const settingsRoutes = require('./routes/settingsRoutes');

const server = http.createServer(app);
const io = socketIO(server);

connectDB();

app.use(express.json());
app.use('/notifications', notificationRoutes);
app.use('/notificationsSettings', settingsRoutes);

sockets(io);

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
