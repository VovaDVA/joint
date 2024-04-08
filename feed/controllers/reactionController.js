const reactionService = require('../services/reactionService');

class reactionController {
    constructor(reactionService) {
        this.reactionService = reactionService;
    }

    async createReaction(req, res) {
        try {
            const {post_id, user_id} = req.body;
            const reaction = await reactionService.createReaction(post_id, user_id);
            return res.status(201).json(reaction);
        }
        catch (error) {
            return res.status(500).json({message: error.message});
        }
    }

    async getReactionById(req, res) {
        try {
            const reactionId = req.params.id;
            const reaction = await reactionService.getReactionById(id);
    
            if (!reaction) {
                return res.status(404).json("Reaction not found");
            }
    
            return res.status(201).json(reaction);
        }
        catch (error)  {
            res.status(500).json({message: error.message});
        }
    }
}

module.exports = new reactionController;