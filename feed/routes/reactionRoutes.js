const express = require('express');
const router = express.Router();
const reactionController = require('../controllers/reactionController');

router.post('/createReaction', reactionController.createReaction);
router.get('/getReactionById', reactionController.getReactionById);
router.get('/getReactionByUser', reactionController.getReactionByUser);

module.exports = router;