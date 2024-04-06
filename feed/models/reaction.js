const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.ObjectId;

const reactionSchema = new mongoose.Schema({
  post_id: {
    type: ObjectId,
    required: true
  },
  user_id : {
    type: String,
    required: true
  },
  created_at: {
    type: Date,
    required: true
  }
});

module.exports = mongoose.model('Reaction', reactionSchema);