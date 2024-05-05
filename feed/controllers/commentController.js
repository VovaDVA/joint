const commentService = require('../services/commentService');
const postService = require('../services/postService')

class commentController {
    constructor(commentService) {
        this.commentService = commentService;
    }

    async createComment(req, res) {
        try {
            const {post_id, author_id, content} = req.body;
            const comment = await commentService.createComment(post_id, author_id, content);
            const post = await postService.newComment(post_id, comment._id)
            return res.status(201).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async getComment(req, res) {
        try {
            const commentId = req.query.id;
            const comment = await commentService.getCommentById(commentId);

            if (!comment) {
                return res.status(404).json({message: "Comment not found"});
            }

            return res.status(201).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async newLike(req, res) {
        try {
            const {comment_id, user_id} = req.body;
            const comment = await commentService.newLike(comment_id, user_id);
            return res.status(201).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }
}

module.exports = new commentController();