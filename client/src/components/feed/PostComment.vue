<template>
    <div class="post-comment" :class="$store.state.theme">
        <div class="avatar">
            <user-avatar></user-avatar>
        </div>
        <div class="user-info">
            <div class="username">{{ username }}</div>
            <div class="comment">{{ comment.content }}</div>
            <div class="date">{{ $formatDate(comment.created_at) }}</div>
        </div>
    </div>
</template>

<script>
import { getUserById } from '@/modules/auth';

export default {
    name: 'post-comment',
    props: ['comment'],
    data() {
        return {
            username: '-'
        }
    },
    async mounted() {
        if (this.comment) {
            const user = await getUserById(this.comment.author_id);
            this.username = user.firstName + ' ' + user.lastName;
        }
    }
}
</script>

<style scoped>
.post-comment {
    display: flex;
    gap: 10px;
}

.user-info {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.username {
    font-size: 13px;
    color: #ffbf6c;
}

.post-comment.light-theme .username {
    color: #af5d00;
}

.avatar {
    height: 30px;
}

.comment {
    font-size: 13px;
}

.date {
    font-size: 12px;
    color: #ffffff7c;
}

.post-comment.light-theme .date {
    color: #0000007c;
}
</style>