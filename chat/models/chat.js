const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema({
    participitans: {
        type: [String]
    }, 
    created_at:{ 
        type: Date
    },
    last_message_at: {
        type: Date
    }
});

module.exports = mongoose.model("Chat", chatSchema);