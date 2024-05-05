const app = require('./app');
const http = require('http');
const server = http.createServer(app);
const sockets = require('./sockets');
const { db, PORT } = require('./config');
const { Server } = require("socket.io");

const io = new Server(server, {
    cors: {
        origin: "*"
    }
});

sockets(io);

db.connectDB();

server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});