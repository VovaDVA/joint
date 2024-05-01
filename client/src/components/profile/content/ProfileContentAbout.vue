<template>
    <content-block>
        <content-block-title>Об авторе</content-block-title>
        <content-block-text>{{ getUserDescription }}
            <p>Меня зовут Владимир и я занимаюсь разработкой соцсети Joint.</p>
        </content-block-text>
    </content-block>
    <post-grid>
        <feed-block-post v-for="post in posts" :key="post._id" :post="post"></feed-block-post>
    </post-grid>
</template>

<script>
import { getUser, getUserDescription } from '@/modules/auth';
export default {
    data() {
        return {
            posts: [],
        };
    },
    async mounted() {
        try {
            const response = await fetch('/post/getPostsByAuthor?author_id=' + getUser().userId);
            const data = await response.json();
            console.log(data);
            this.posts = data;

        } catch (error) {
            console.error(error);
        }
    },
    methods: {
        getDescription() {
            return getUserDescription();
        }
    }
}
</script>