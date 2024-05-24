const Post = require('../models/post');

class postService {	
	constructor(Post){
		this.Post = Post;
	}

	async createPost(data){
		const post = new Post({
			"author_id": data.author_id,
			"title": data.title,
			"content": data.content,
			"created_at": new Date(),
			"likes": [],
			"comments": []
		});л
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
		const post = await this.getPostById(postId);
		post.comments.push(commentId);
		await post.save();
		return post;
	}

	async newLike(postId, userId){
		const post = await this.getPostById(postId);
		post.likes.push(userId);
		await post.save();
		return post;
	}

	async editPost(post_id, content) {
		return await Post.findByIdAndUpdate(post_id, {"content": content}, { new: true });
	}

	async deletePost(postId) {
		return await Post.findByIdAndDelete(postId);
	}
}

module.exports = new postService;