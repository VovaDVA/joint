<template>
    <div class="post-comment">
        <div class="avatar">
            <user-avatar></user-avatar>
        </div>
        <div class="user-info">
            <div class="username" :class="$store.state.theme">{{ username }}</div>
            <div class="comment">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>
import { getUserById } from '@/modules/auth';

export default {
    name: 'post-comment',
    props: ['post'],
    data() {
        return {
            username: '-'
        }
    },
    async mounted() {
        if (this.post) {
            const user = await getUserById(this.post.author_id);
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

.username.light-theme {
    color: #af5d00;
}

.avatar {
    height: 30px;
}

.comment {
    font-size: 13px;
}
</style>