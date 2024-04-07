const commentService = require('../services/commentService');

class commentController {
    constructor(commentService) {
        this.commentService = commentService;
    }

    async createComment(req, res) {
        try {
            const {post_id, author_id, content} = req.body;
            const comment = await commentService.createComment(post_id, author_id, content);
            return res.status(201).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async getComment(req, res) {
        try {
            const commentId = req.params.id;
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
}

module.exports = new commentController();