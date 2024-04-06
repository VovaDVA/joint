const express = require('express');
const router = express.Router();
const postController = require('../controllers/postController');

router.post('/createPost', postController.createPost);
router.get('/getPost', postController.getPost);

module.exports = router;