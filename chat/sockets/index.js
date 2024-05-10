const messageService = require("../services/messageService");
const CryptoJS = require('crypto-js');

const onlineUsers = {};
const chats = {};

module.exports = function (io) {
    io.on("connection", (socket) => {
        console.log(`Подключение установлено ${socket.id}`);

        socket.on('userConnected', (userId) => {
            console.log('user connected')
            onlineUsers[userId] = socket.id;
            io.emit('updateOnlineUsers', onlineUsers);
        });

        socket.on('joinChat', (chatId) => {
            socket.join(chatId);
            console.log('client joined', chatId);

            if (!chats[chatId]) {
                chats[chatId] = [];
            }
            chats[chatId].push(socket.id);
        });

        socket.on('typing', (chatId) => {
            socket.broadcast.to(chatId).emit('typing', chatId);
        });

        socket.on('stopTyping', (chatId) => {
            socket.broadcast.to(chatId).emit('stopTyping', chatId);
        });

        socket.on('sendMessage', (message) => {
            const decryptedMessage = CryptoJS.AES.decrypt(message.text, 'secret').toString(CryptoJS.enc.Utf8);
            message.text = decryptedMessage;

            io.to(message['chat_id']).emit('message', message);
            messageService.createMessage(message);
        });

        socket.on('disconnect', () => {
            console.log(`Подключение закрыто ${socket.id}`);

            const userId = Object.keys(onlineUsers).find(key => onlineUsers[key] === socket.id);
            if (userId) {
                delete onlineUsers[userId];
                io.emit('updateOnlineUsers', onlineUsers);
            }

            for (const chatId in chats) {
                if (chats.hasOwnProperty(chatId)) {
                    chats[chatId] = chats[chatId].filter(id => id !== socket.id);
                }
            }
        });
    });
};