const messageService = require("../services/messageService");

module.exports = function (io) {
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
};