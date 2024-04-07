const postService = require('../services/postService');

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
			const postId = req.params.id;
			const post = await postService.getPostById(postId);

			if (!post) {
				return res.status(404).json({message: "Post not found"});
			}

			return res.status(201).json(post);
		} 
		catch (error) {
			return res.status(500).json({message: error.message});
		}
	}
}

module.exports = new postController();