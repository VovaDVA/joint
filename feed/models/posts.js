const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.ObjectId;

const postSchema = new mongoose.Schema({
  _id: ObjectId,
  author_id: String,
  content: String,
  created_at: Date,
  likes: [String],
  comments: [ObjectId]
});

module.exports = mongoose.model('Posts', postSchema);