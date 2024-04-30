const express = require('express');
const router = new express.Router();
const messageController = require('../controllers/messageController');


router.post("/sendMessage", messageController.sendMessage);
router.post("/editMessage", messageController.editMessage);
router.get("/deleteMessage", messageController.deleteMessage);

module.exports = router;