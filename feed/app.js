const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());

const postRouter = require('./routes/postRoutes');
const commentRouter = require('./routes/commentRoutes');
const reactionRouter = require('./routes/reactionRoutes');

app.use(express.json());
app.use('/post', postRouter);
app.use('/comment', commentRouter);
app.use('/reaction', reactionRouter);

module.exports = app;