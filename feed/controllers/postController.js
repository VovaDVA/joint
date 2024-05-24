const postService = require('../services/postService');

class postController {
	constructor(postService) {
		this.postService = postService;
	}

	async createPost(req, res) {
		try {
			const data = req.body;
			const post = await postService.createPost(data);
			return res.status(201).json(post);
		}
		catch (error) {
			return res.status(500).json({ message: error.message });
		}
	}

	async getPost(req, res) {
		try {
			const postId = req.query.id;
			const post = await postService.getPostById(postId);

			if (!post) {
				return res.status(404).json({ message: "Post not found" });
			}

			return res.status(200).json(post);
		}
		catch (error) {
			return res.status(500).json({ message: error.message });
		}
	}

	async getPostsByAuthor(req, res) {
		try {
			const author_id = req.query.author_id;
			const posts = await postService.getPostsByAuthor(author_id);
			return res.status(200).json(posts);
		}
		catch (error) {
			return res.status(500).json({ message: error.message });
		}
	}

	async getAllPosts(req, res) {
		try {
			const posts = await postService.getAllPosts();
			return res.status(200).json(posts);
		}
		catch (error) {
			return res.status(500).json({ message: error.message });
		}
	}

	async editPost(req, res) {
		try {
			const { post_id, content } = req.body;

			if (!await postService.getPostById(post_id)) {
				return res.status(404).json({ message: "Post not found" });
			}

			const post = await postService.editPost(post_id, content);
			return res.status(201).json(post);
		}
		catch (error) {
			return res.status(500).json({ message: error.message });
		}
	}

	async deletePost(req, res) {
		try {
			const postId = req.query.id;
			const Post = await postService.getPostById(postId);

			if (!Post) {
				return res.status(404).json({ message: "Post not found" });
			}

			const comments_id = Post.comments;
			const likes_auth_id = Post.likes;

			for (let i in comments_id) {
				await commentService.deleteComment(comments_id[i]);
			}

			for (let i in likes_auth_id) {
				const reaction = await reactionService.getReactionByUser(likes_auth_id[i]);
				await reactionService.deleteReaction(reaction._id);
			}

			const post = await postService.deletePost(postId);
			return res.status(200).json(post);
		}
		catch (error) {
			return res.status(500).json({ message: error.message });
		}
	}
}

module.exports = new postController();