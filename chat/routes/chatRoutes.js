const express = require('express');
const router = new express.Router();
const chatController = require('../controllers/chatController');

// Здесь будут реализованы маршруты для чатов

router.post("/createChat", chatController.createChat);
router.get("/getChat", chatController.getChat);
router.get("/getMessages", chatController.getMessages);
router.get("/getUserChats", chatController.getUserChats);

module.exports = router;
