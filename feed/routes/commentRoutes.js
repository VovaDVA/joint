const express = require('express');
const router = express.Router();
const commentController = require('../controllers/commentController');

router.post('/createComment', commentController.createComment);
router.get('/getComment', commentController.getComment);

module.exports = router;