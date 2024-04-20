const Post = require('../models/post');

class postService {	
	constructor(Post){
		this.Post = Post;
	}

	async createPost(author_id, content){
		let date = new Date();
		const post = new Post({
			"author_id": author_id,
			"content": content,
			"created_at": date,
			"likes": [],
			"comments": []
		});
		await post.save();
		return post;
	}

	async getPostsByAuthor(authorId) {
		return await Post.find({"author_id": authorId});
	}

	async getAllPosts() {
		return await Post.find();
	}

	async getPostById(postId) {
		return await Post.findById(postId);
	}

	async newComment(postId, commentId){
		const post = await Post.getPostById(postId); //было this.getPostById
		post.comments.push(commentId);
		await post.save();
		return post;
	}

	async newLikeForPost(postId, userId){
		const post = await this.getPostById(postId);
		post.likes.push(userId);
		await post.save();
		return post;
	}
}

module.exports = new postService;