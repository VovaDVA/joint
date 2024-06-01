<template>
    <content-block>
        <content-block-title>Об авторе</content-block-title>
        <content-block-text>{{ getDescription() }}</content-block-text>
    </content-block>
    <post-grid>
        <feed-block-post v-for="post in posts" :key="post._id" :post="post"></feed-block-post>
    </post-grid>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { getUserDescription } from '@/modules/auth';
export default {
    data() {
        return {
            posts: [],
        };
    },
    async mounted() {
        await apiClient.content.getPostsByAuthor((data) => this.posts = data);
    },
    methods: {
        getDescription() {
            return getUserDescription();
        }
    }
}
</script>