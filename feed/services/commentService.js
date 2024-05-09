const Comment = require('../models/comment');

class commentService { 
    constructor(Comment) {
        this.Comment = Comment;
    }

    async createComment(post_id, author_id, content) {
        let date = new Date();
        const comment = new Comment({
            "post_id": post_id,
            "author_id": author_id,
            "content": content,
            "created_at": date,
            "likes": []
        });
        await comment.save();
        return comment;
    }

    async getCommentById(commentId) {
        return await Comment.findById(commentId);
    }

    async newLike(comment_id, user_id) {
        const comment = await this.getCommentById(comment_id);
		comment.likes.push(user_id);
		await comment.save();
		return comment;
    }

    async editComment(comment_id, content) {
        return await Comment.findByIdAndUpdate(comment_id, {"content": content}, { new: true });
    }
    
    async deleteComment(commentId) {
        return await Comment.findByIdAndDelete(commentId);
    }
}

module.exports = new commentService;