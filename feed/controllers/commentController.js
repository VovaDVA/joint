const commentService = require('../services/commentService');
const postService = require('../services/postService')

class commentController {
    constructor(commentService) {
        this.commentService = commentService;
    }

    async createComment(req, res) {
        try {
            const data = req.body;
            console.log(data);
            const comment = await commentService.createComment(data);
            await postService.newComment(data.post_id, comment._id)
            return res.status(201).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async getPostComments(req, res) {
        try {
            const postId = req.query.postId;
            const post = await postService.getPostById(postId);

            if (!post) {
                return res.status(404).json({message: "Post not found"});
            }

            const comments =  await commentService.getPostComments(postId);

            return res.status(200).json(comments);
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

            return res.status(200).json(comment);
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

    async editComment(req, res) {
        try {
            const {comment_id, content} = req.body;

            if (!await commentService.getCommentById(comment_id)) {
				return res.status(404).json({message: "Comment not found"});
			}

            const comment = await commentService.editComment(comment_id, content);
            return res.status(201).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async deleteComment(req, res) {
        try {
            const commentId = req.query.id;
            const postId = req.query.post_id;

            if (!await commentService.getCommentById(commentId)) {
                return res.status(404).json({message: "Comment not found"});
            }

            const post = await postService.getPostById(postId);
            const comment_index = post.comments.indexOf(commentId);
            post.comments.splice(comment_index, 1);
            await post.save();    
            const comment = await commentService.deleteComment(commentId);
            return res.status(200).json(comment);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }
}

module.exports = new commentController();