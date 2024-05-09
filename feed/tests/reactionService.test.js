const mongoose = require('mongoose');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const reactionService = require('../services/reactionService');
const postService = require('../services/postService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Reaction', () => {
    test('create reaction for post', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let user_id = "123";
        const reaction = await reactionService.createReaction(post_id, user_id);

        expect(reaction).toBeTruthy();
        expect(reaction).toHaveProperty("post_id");
        expect(reaction.post_id).toEqual(post_id);
        expect(reaction).toHaveProperty("user_id");
        expect(reaction.user_id).toEqual(user_id);

        await dbClear();
    });

    test('get reaction by ID', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let user_id = "123";
        const Reaction = await reactionService.createReaction(post_id, user_id);

        let reaction_id = Reaction._id;
        const reaction = await reactionService.getReactionById(reaction_id);

        expect(reaction).toBeTruthy();
        expect(reaction).toHaveProperty("post_id");
        expect(reaction.post_id).toEqual(post_id);
        expect(reaction).toHaveProperty("user_id");
        expect(reaction.user_id).toEqual(user_id);

        await dbClear();
    });

    test('get reaction by user', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let user_id = "123";
        const Reaction = await reactionService.createReaction(post_id, user_id);

        const reaction = await reactionService.getReactionByUser(user_id);

        expect(reaction).toBeTruthy();
        expect(reaction).toHaveProperty("post_id");
        expect(reaction.post_id).toEqual(post_id);
        expect(reaction).toHaveProperty("user_id");
        expect(reaction.user_id).toEqual(user_id);

        await dbClear();
    });

    test('delete the reaction', async() => {
        const post = await postService.createPost("1", "Hello Joint!");

        let post_id = post._id;
        let user_id = "123";
        const Reaction = await reactionService.createReaction(post_id, user_id);

        let reaction_id = Reaction._id;
        const deleted_reaction = await reactionService.deleteReaction(reaction_id);
        const reaction = await reactionService.getReactionById(reaction_id);

        expect(deleted_reaction).toBeTruthy();
        expect(reaction).toBeFalsy();

        await dbClear();
    });
});