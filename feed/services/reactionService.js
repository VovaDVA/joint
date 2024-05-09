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
        return await Reaction.findById(reactionId);
    }

    async getReactionByUser(userId) {
        return await Reaction.findOne({"user_id": userId});
    }

    async deleteReaction(reactionId) {
        return await Reaction.findByIdAndDelete(reactionId);
    }
}

module.exports = new reactionService;