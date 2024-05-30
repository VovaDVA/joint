const Comment = require('../models/comment');

class commentService {
    constructor(Comment) {
        this.Comment = Comment;
    }

    async createComment(data) {
        let date = new Date();
        const comment = new Comment({
            "post_id": data.post_id,
            "author_id": data.author_id,
            "content": data.content,
            "created_at": date,
            "likes": []
        });
        await comment.save();
        return comment;
    }

    async getCommentById(commentId) {
        return await Comment.findById(commentId);
    }

    async getPostComments(postId) {
        return await Comment.find({ "post_id": postId });
    }

    async newLike(comment_id, user_id) {
        const comment = await this.getCommentById(comment_id);
        comment.likes.push(user_id);
        await comment.save();
        return comment;
    }

    async editComment(comment_id, content) {
        return await Comment.findByIdAndUpdate(comment_id, { "content": content }, { new: true });
    }

    async deleteComment(userId) {
        return await Comment.findOneAndDelete({ "user_id": userId });
    }
}

module.exports = new commentService;