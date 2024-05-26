const postService = require('../services/postService');
const Comment = require('../models/comment');
const Reaction = require('../models/reaction');

class postController {
	constructor(postService) {
    this.postService = postService;
	}

	
	async createPost(req, res) {
		try {
			const {author_id, content} = req.body;
			const post = await postService.createPost(author_id, content);
			return res.status(201).json(post);
		} 
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}

	async getPost(req, res) {
		try {
			const postId = req.query.id;
			const post = await postService.getPostById(postId);

			if (!post) {
				return res.status(404).json({message: "Post not found"});
			}

			return res.status(200).json(post);
		} 
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}

	async getPostsByAuthor(req, res) {
		try {
			const author_id = req.query.author_id;
			const posts = await postService.getPostsByAuthor(author_id);			
			return res.status(200).json(posts);
		}
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}

	async getAllPosts(req, res) {
		try {
			const posts = await postService.getAllPosts();
			return res.status(200).json(posts);
		}
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}

	async editPost(req, res) {
		try {
			const {post_id, content} = req.body;

			if (!await postService.getPostById(post_id)) {
				return res.status(404).json({message: "Post not found"});
			}

			const post = await postService.editPost(post_id, content);
			return res.status(201).json(post);
		}
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}

	async deletePost(req, res) {
		try {
			const postId = req.query.id;
			const post1 = await postService.getPostById(postId);
			
			if (!post1) {
				return res.status(404).json({message: "Post not found"});
			}

			const comments_id = post1.comments;
			const likes_auth_id = post1.likes;

			await Comment.deleteMany({"post_id": post1._id})
			await Reaction.deleteMany({"post_id": post1._id})

			const post = await postService.deletePost(postId);
			return res.status(200).json(post);
		}
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}
}

module.exports = new postController();