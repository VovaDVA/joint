const Reaction = require('../models/reaction');

class reactionService {
    constructor(Reaction) {
        this.Reaction = Reaction;
    }

    async createReaction(data) {
        const date = new Date();
        const reaction = new Reaction({
            "post_id": data.post_id,
            "user_id": data.user_id,
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

    async deleteReaction(userId) {
        return await Reaction.findOneAndDelete({"user_id": userId});
    }
}

module.exports = new reactionService;