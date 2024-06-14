const req = require('supertest');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const app = require('../app');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Post controller', () => {
    test('create post OK - code 201', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const res = await req(app).post('/post/createPost').send(data);

        expect(res.statusCode).toBe(201);
        expect(res.body).toHaveProperty("author_id");
        expect(res.body.author_id).toStrictEqual(123);
        expect(res.body).toHaveProperty("content");
        expect(res.body.content).toStrictEqual("Hello");
        expect(res.body).toHaveProperty("title");
        expect(res.body.title).toStrictEqual("title");

        await dbClear();
    });

    test('error in request parameters for creating post', async() => {
        const res = await req(app).post('/post/createPost').send({
            "id": "123", 
            "qwerty": "Hello"
        });

        expect(res.statusCode).toBe(500);
    });

    test('get post by ID OK - code 200', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let id = post.body._id;
        const res = await req(app).get('/post/getPost').query({
            "id": id
        });

        expect(res.statusCode).toBe(200);
        expect(res.body._id).toStrictEqual(id);

        await dbClear();
    });

    test("get post by ID Error (not found the post) - code 404", async() => {
        let id = "66432f270198f3e138afff10";

        const res = await req(app).get('/post/getPost').query({
            "id": id
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toStrictEqual("Post not found");
    });

    test('get posts by author OK - code 200', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        const res = await req(app).get('/post/getPostsByAuthor').query({
            "author_id": "123"
        });

        expect(res.statusCode).toBe(200);
    });

    test('get all posts OK - code 200', async() => {
        const res = await req(app).get('/post/getAllPosts');

        expect(res.statusCode).toBe(200);

        await dbClear();
    });

    test('edit post OK - code 201', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        const res = await req(app).post('/post/editPost').send({
            "post_id": post.body._id,
            "content": "New content"
        });

        expect(res.statusCode).toBe(201);
        expect(res.body.content).toStrictEqual("New content");

        await dbClear();
    });

    test("edit post Error (not found the post for editing) - code 404", async() => {
        let id = "66432f270198f3e138afff10";
        const res = await req(app).post('/post/editPost').send({
            "post_id": id,
            "content": "New content"
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toStrictEqual("Post not found");
    });

    test('delete post OK - code 200', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let post_id = post.body._id;
        const comment = await req(app).post('/comment/createComment').send({
            "post_id": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        const reaction = await req(app).post('/reaction/createReaction').send({
            "post_id": post_id,
            "user_id": 999
        });

        const res = await req(app).post('/post/deletePost').send({
            "postId": post.body._id
        });

        expect(res.statusCode).toBe(200);
    });

    test("delete post Error (not found the post for deleting) - code 404", async() => {
        const res = await req(app).post('/post/deletePost').send({
            "asdfg": "66432f270198f3e138afff10"
        });

        expect(res.statusCode).toBe(404);
    });
});