const express = require('express');
const router = new express.Router();
const chatController = require('../controllers/chatController');

// Здесь будут реализованы маршруты для чатов

router.post("/createChat", chatController.createChat);
router.get("/getChat", chatController.getChat)

module.exports = router;
