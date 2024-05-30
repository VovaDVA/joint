const express = require('express');
const router = express.Router();
const commentController = require('../controllers/commentController');

router.post('/createComment', commentController.createComment);
router.get('/getPostComments', commentController.getPostComments);
router.get('/getComment', commentController.getComment);
router.post('/newLike', commentController.newLike);
router.post('/editComment', commentController.editComment);
router.get('/deleteComment', commentController.deleteComment);

module.exports = router;