<template>
    <div class="feed-block" :class="$store.state.theme">
        <div class="header">
            <user-avatar :photo="avatar"></user-avatar>
            <div class="author">
                <div class="username">{{ post ? post.author_name : '-' }}</div>
                <div class="date">{{ post ? $formatDate(post.created_at) : '-' }}</div>
            </div>
        </div>
        <slot></slot>
    </div>
</template>

<script>
import { getUserById } from '@/modules/auth';

export default {
    name: 'feed-block-template',
    props: ['post'],
    async mounted() {
        if (this.post) {
            const user = await getUserById(this.post.author_id);
            this.avatar = user.avatar;
        }
    },
    data() {
        return {
            avatar: '',
        }
    }
}
</script>

<style scoped>
.feed-block {
    display: flex;
    flex-direction: column;
    gap: 10px;
    height: fit-content;
    padding: 10px;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-block.light-theme {
    border: 1px #0000007c solid;
    background: rgba(255, 255, 255, 0.5);
    color: #000;
}

.header {
    height: 45px;
    display: flex;
    gap: 10px;
}

.author {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}

.date {
    font-size: 12px;
    color: #ffffff7c;
}

.feed-block.light-theme .date {
    font-size: 12px;
    color: #0000007c;
}
</style>