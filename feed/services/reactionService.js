const Reaction = require('../models/reaction');

class reactionService {
    constructor(Reaction) {
        this.Reaction = Reaction;
    }

    async createReaction(post_id, user_id) {
        const date = new Date();
        const reaction = new Reaction({
            "post_id": post_id,
            "user_id": user_id,
            "created_at": date
        })
        await reaction.save();
        return reaction;
    }

    async getReactionById(reactionId) {
        return Reaction.findById(reactionId);
    }
}

module.exports = new reactionService;