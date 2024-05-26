const express = require('express');
const app = express();

const postRouter = require('./routes/postRoutes');
const commentRouter = require('./routes/commentRoutes');
const reactionRouter = require('./routes/reactionRoutes');

app.use(express.json());
app.use('/post', postRouter);
app.use('/comment', commentRouter);
app.use('/reaction', reactionRouter);

module.exports = app;