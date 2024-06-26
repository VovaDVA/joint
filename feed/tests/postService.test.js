const mongoose = require('mongoose');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const postService = require('../services/postService');
const commentService = require('../services/commentService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Post', () => {
    describe('Create', () => {
        test('create post', async() => {
            let data = {
                "author_id": 1,
                "title": "title",
                "content": "Hello Joint!"
            };
            const post = await postService.createPost(data);

            expect(post).toBeTruthy();
            expect(post).toHaveProperty("author_id");
            expect(post).toHaveProperty("content");
            expect(post).toHaveProperty("title");
            expect(post.author_id).toEqual(1);
            expect(post.content).toEqual("Hello Joint!");
            expect(post.title).toEqual("title")
        });
    });

    describe('Find', () => {
        test('find all posts by author', async() => {
            let author_id = 1;
            let content = "Hello Joint!";

            const posts = await postService.getPostsByAuthor(author_id);

            expect(posts).toBeTruthy();
            expect(posts[0]).toHaveProperty("author_id");
            expect(posts[0].author_id).toEqual(author_id);
            expect(posts[0]).toHaveProperty("content");
            expect(posts[0].content).toEqual(content);
        });

        test('find post by ID', async() => {
            let data = {
                "author_id": 2,
                "title": "title",
                "content": "<3"
            };
            const Post = await postService.createPost(data);

            let id = Post._id;
            const post = await postService.getPostById(id);

            expect(post).toBeTruthy();
            expect(post).toHaveProperty("author_id");
            expect(post).toHaveProperty("content");
            expect(post.author_id).toEqual(2);
            expect(post.content).toEqual("<3");
        });

        test('receive all posts', async() => {
            const posts = await postService.getAllPosts();
            expect(posts).toBeTruthy();
            
            await dbClear();
        });
    });

    describe('Add', () => {
        test('add new comment for post', async() => {
            let data = {
                "author_id": 4,
                "title": "title",
                "content": "Do you like Joint? Yes, of course"
            };
            let post = await postService.createPost(data);

            let comment_data = {
                "post_id": post._id,
                "author_id": "123",
                "content": "How did you know it??"
            }
            const comment = await commentService.createComment(comment_data);

            let comment_id = comment._id;
            post = await postService.newComment(post._id, comment_id);
            expect(post).toBeTruthy();
            expect(post).toHaveProperty("comments");
            expect(post.comments[0]).toEqual(comment_id);

            await dbClear();
        });

        test('add new reaction for post', async() => {
            let data = {
                "author_id": 4,
                "title": "title",
                "content": "Do you like Joint? Yes, of course"
            };
            let post = await postService.createPost(data);

            let post_id = post._id;
            let user_id = 123;
            
            post = await postService.newLike(post_id, user_id);
            expect(post).toBeTruthy();
            expect(post).toHaveProperty("likes");
            expect(post.likes[0]).toEqual(user_id);
            
            await dbClear();
        });
    });

    describe('Edit', () => {
        test('edit the post', async() => {
            let data = {
                "author_id": 2,
                "title": "title",
                "content": "<3"
            };
            const Post = await postService.createPost(data);

            let post_id = Post._id;
            let new_content = "Something";
            const post = await postService.editPost(post_id, new_content);

            expect(post).toBeTruthy();
            expect(post.author_id).toEqual(2);
            expect(post.content).not.toEqual("<3");
            expect(post.content).toEqual(new_content);

            await dbClear();
        });
    });

    describe('Delete', () => {
        test('delete the post', async() => {
            let data = {
                "author_id": 2,
                "title": "title",
                "content": "<3"
            };
            const Post = await postService.createPost(data);

            let post_id = Post._id;
            const deleted_post = await postService.deletePost(post_id);
            const post = await postService.getPostById(post_id);

            expect(deleted_post).toBeTruthy();
            expect(post).toBeFalsy();

            await dbClear();
        });
    });
});
