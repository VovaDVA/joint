const mongoose = require('mongoose');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const postService = require('../services/postService');
const commentService = require('../services/commentService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Comment', () => {
    test('create comment', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let comment_data = {
            "post_id": post._id,
            "author_id": "1234",
            "content": "Hi"
        }
        const comment = await commentService.createComment(comment_data);

        expect(comment).toBeTruthy();
        expect(comment).toHaveProperty("post_id");
        expect(comment.post_id).toEqual(post._id);
        expect(comment).toHaveProperty("author_id");
        expect(comment.author_id).toEqual("1234");
        expect(comment).toHaveProperty("content");
        expect(comment.content).toEqual("Hi");

        await dbClear();
    });

    test('find comment by ID', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let comment_data = {
            "post_id": post._id,
            "author_id": "1234",
            "content": "comment"
        }
        const Comment = await commentService.createComment(comment_data);

        let comment_id = Comment._id;
        const comment = await commentService.getCommentById(comment_id);

        expect(comment).toBeTruthy();
        expect(comment).toHaveProperty("post_id");
        expect(comment.post_id).toEqual(post._id);
        expect(comment).toHaveProperty("author_id");
        expect(comment.author_id).toEqual("1234");
        expect(comment).toHaveProperty("content");
        expect(comment.content).toEqual("comment");

        await dbClear();
    });

    test('find post comments', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let comment_data = {
            "post_id": post._id,
            "author_id": "1234",
            "content": "comment"
        }
        const Comment = await commentService.createComment(comment_data);

        const comment = await commentService.getPostComments(post._id);

        expect(comment).toBeTruthy();

        await dbClear();
    });

    test('add new reaction for comment', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let comment_data = {
            "post_id": post._id,
            "author_id": "1234",
            "content": "comment"
        }
        const Comment = await commentService.createComment(comment_data);

        let comment_id = Comment._id;
        let user_id = "321";
        const comment = await commentService.newLike(comment_id, user_id);

        expect(comment).toBeTruthy();
        expect(comment["likes"]).toEqual(["321"]);

        await dbClear();
    });

    test('edit the comment', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let comment_data = {
            "post_id": post._id,
            "author_id": "1234",
            "content": "comment"
        }
        const Comment = await commentService.createComment(comment_data);

        let comment_id = Comment._id;
        let new_content = "something";
        const comment = await commentService.editComment(comment_id, new_content);

        expect(comment).toBeTruthy();
        expect(comment.content).toEqual(new_content);
        expect(comment._id).toEqual(comment_id);

        await dbClear();
    });

    test('delete the comment', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let comment_data = {
            "post_id": post._id,
            "author_id": "1234",
            "content": "comment"
        }
        const Comment = await commentService.createComment(comment_data);

        const deleted_comment = await commentService.deleteComment("1234");
        const comment = await commentService.getCommentById(Comment._id);

        expect(deleted_comment).toBeTruthy();
        expect(comment).toBeFalsy();

        await dbClear();
    });
});
