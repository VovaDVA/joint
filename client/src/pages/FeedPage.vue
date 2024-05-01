<template>
    <token-check></token-check>
    <post-grid>
        <feed-block-post v-for="post in posts" :key="post._id" :post="post"></feed-block-post>
    </post-grid>
</template>

<script>
export default {
    data() {
        return {
            posts: [],
        };
    },
    async mounted() {
        try {
            const response = await fetch('/post/getAllPosts');

            const data = await response.json();
            console.log(data);
            this.posts = data;

        } catch (error) {
            console.error(error);
        }
    },
}
</script>

<style scoped></style>