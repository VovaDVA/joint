const express = require('express');
const router = new express.Router();
const messageController = require('../controllers/messageController');


router.post("/sendMessage", messageController.sendMessage);
module.exports = router;