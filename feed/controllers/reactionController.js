const reactionService = require('../services/reactionService');
const postService = require('../services/postService')

class reactionController {
    constructor(reactionService) {
        this.reactionService = reactionService;
    }

    async createReaction(req, res) {
        try {
            const data = req.body;
            const reaction = await reactionService.createReaction(data);
            const post = await postService.newLike(data.post_id, data.user_id)
            return res.status(201).json(reaction);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async getReactionById(req, res) {
        try {
            const reaction_id = req.query.id;
            const reaction = await reactionService.getReactionById(reaction_id);
    
            if (!reaction) {
                return res.status(404).json({message: "Reaction not found"});
            }
    
            return res.status(200).json(reaction);
        }
        catch (error)  {
            res.status(500).json({message: error.message});
        }
    }

    async getReactionByUser(req, res) {
        try {
            const user_id = req.query.user_id;
            const reaction = await reactionService.getReactionByUser(user_id);

            if (!reaction) {
                return res.status(404).json({message: "Reaction not found"});
            } 

            return res.status(200).json(reaction);
        }
        catch (error) {
            res.status(500).json({message: error.message});
        }
    }

    async deleteReaction(req, res) {
        try {
            const data = req.body;

            if (!await reactionService.getReactionByUser(data.user_id)) {
                return res.status(404).json({message: "Reaction not found"});
            }

            console.log(data);

            const post = await postService.getPostById(data.post_id);
            const reaction_index = post.likes.indexOf(data.user_id);
            post.likes.splice(reaction_index, 1);
            await post.save();
            const reaction = await reactionService.deleteReaction(data.user_id);
            return res.status(200).json(reaction);
        }
        catch (error) {
            res.status(500).json({message: error.message});
        }
    }
}

module.exports = new reactionController();