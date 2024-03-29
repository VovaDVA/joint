const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.ObjectId;

const reactionSchema = new mongoose.Schema({
  _id: ObjectId,
  post_id: ObjectId,
  user_id : String,
  created_at: Date
});

module.exports = mongoose.model('Reactions', reactionSchema);