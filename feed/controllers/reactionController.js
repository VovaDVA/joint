const reactionService = require('../services/reactionService');
const postService = require('../services/postService')

class reactionController {
    constructor(reactionService) {
        this.reactionService = reactionService;
    }

    async createReaction(req, res) {
        try {
            const {post_id, user_id} = req.body;
            const reaction = await reactionService.createReaction(post_id, user_id);
            const post = await postService.newLike(post_id, user_id)
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
                return res.status(404).json("Reaction not found");
            }
    
            return res.status(201).json(reaction);
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
                return res.status(404).json("Reaction not found");
            } 

            return res.status(201).json(reaction);
        }
        catch (error) {
            res.status(500).json({message: error.message});
        }
    }
}

module.exports = new reactionController();