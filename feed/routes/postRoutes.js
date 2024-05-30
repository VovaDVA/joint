const express = require('express');
const router = express.Router();
const postController = require('../controllers/postController');

router.post('/createPost', postController.createPost);
router.get('/getPost', postController.getPost);
router.get('/getPostsByAuthor', postController.getPostsByAuthor);
router.get('/getAllPosts', postController.getAllPosts);
router.post('/deletePost', postController.deletePost);
router.post('/editPost', postController.editPost);

module.exports = router;