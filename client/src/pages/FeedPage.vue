<template>
    <token-check></token-check>
    <post-grid>
        <feed-block-post v-for="post in posts" :key="post._id" :post="post"></feed-block-post>
    </post-grid>
</template>

<script>
import apiClient from '@/modules/ApiClient';

export default {
    data() {
        return {
            posts: [],
        };
    },
    async mounted() {
        await apiClient.content.getAllPosts((data) => this.posts = data);
    },
}
</script>

<style scoped></style>