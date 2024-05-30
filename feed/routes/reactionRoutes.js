const express = require('express');
const router = express.Router();
const reactionController = require('../controllers/reactionController');

router.post('/createReaction', reactionController.createReaction);
router.get('/getReaction', reactionController.getReactionById);
router.get('/getReactionByUser', reactionController.getReactionByUser);
router.post('/deleteReaction', reactionController.deleteReaction);

module.exports = router;