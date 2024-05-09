const mongoose = require('mongoose');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const postService = require('../services/postService');
const commentService = require('../services/commentService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Comment', () => {
    test('create comment', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let author_id = "1234";
        let content = "Hi!";
        const comment = await commentService.createComment(post_id, author_id, content);

        expect(comment).toBeTruthy();
        expect(comment).toHaveProperty("post_id");
        expect(comment.post_id).toEqual(post_id);
        expect(comment).toHaveProperty("author_id");
        expect(comment.author_id).toEqual(author_id);
        expect(comment).toHaveProperty("content");
        expect(comment.content).toEqual(content);

        await dbClear();
    });

    test('find comment by ID', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let author_id = "123";
        let content = "comment";
        const Comment = await commentService.createComment(post_id, author_id, content);

        let comment_id = Comment._id;
        const comment = await commentService.getCommentById(comment_id);

        expect(comment).toBeTruthy();
        expect(comment).toHaveProperty("post_id");
        expect(comment.post_id).toEqual(post_id);
        expect(comment).toHaveProperty("author_id");
        expect(comment.author_id).toEqual(author_id);
        expect(comment).toHaveProperty("content");
        expect(comment.content).toEqual(content);

        await dbClear();
    });

    test('add new reaction for comment', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let author_id = "123";
        let content = "comment";
        const Comment = await commentService.createComment(post_id, author_id, content);

        let comment_id = Comment._id;
        let user_id = "321";
        const comment = await commentService.newLike(comment_id, user_id);

        expect(comment).toBeTruthy();
        expect(comment["likes"]).toEqual(["321"]);

        await dbClear();
    });

    test('edit the comment', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let author_id = "123";
        let content = "comment";
        const Comment = await commentService.createComment(post_id, author_id, content);

        let comment_id = Comment._id;
        let new_content = "something";
        const comment = await commentService.editComment(comment_id, new_content);

        expect(comment).toBeTruthy();
        expect(comment.content).toEqual(new_content);
        expect(comment._id).toEqual(comment_id);

        await dbClear();
    });

    test('delete the comment', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let author_id = "123";
        let content = "comment";
        const Comment = await commentService.createComment(post_id, author_id, content);

        let comment_id = Comment._id;
        const deleted_comment = await commentService.deleteComment(comment_id);
        const comment = await commentService.getCommentById(comment_id);

        expect(deleted_comment).toBeTruthy();
        expect(comment).toBeFalsy();

        await dbClear();
    });
});