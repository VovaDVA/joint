<template>
    <token-check></token-check>
    <content-block>
        <content-block-title>Созание поста</content-block-title>

        <form enctype="multipart/form-data" @submit.prevent="createPost">
            <form-input v-model="title" :value="title">Заголовок поста</form-input>
            <form-text-area v-model="content" :value="content" :data="'Напишите что-нибудь...'"></form-text-area>

            <submit-button class="submit" data="Опубликовать"></submit-button>
        </form>
    </content-block>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { checkToken, getUser, getUserName } from '@/modules/auth';

export default {
    data() {
        return {
            title: '',
            content: '',
        };
    },
    created() {
        checkToken();
    },
    methods: {
        async createPost(event) {
            event.preventDefault();

            if (this.content == '' && this.title == '') return;

            await apiClient.content.createPost({
                author_id: getUser().userId,
                title: this.title,
                content: this.content
            }, () => this.$router.push('/'));
        }
    }
}
</script>

<style scoped>
input {
    line-height: 28px;
    font-size: 13px;
    border: 1px #ffffff solid;
    border-radius: 30px;
    max-height: 30px;
    margin: 5px 0 15px 10px;
    padding: 0 20px;
    width: 49%;
    text-align: center;
    color: #ffffff;

    transition: color, background .3s linear;
}
</style>