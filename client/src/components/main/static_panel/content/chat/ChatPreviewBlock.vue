<template>
    <div class="chat-preview-block" :class="$store.state.theme">
        <div class="avatar"></div>
        <div class="chat-info">
            <div class="chat-header">
                <div class="chat-title">{{ getUserName() }}</div>
                <div class="chat-last-changed">{{ chat.last_message_at ?? '15:00' }}</div>
            </div>
            <div class="last-message">
                <div class="last-message-avatar"></div>
                <div class="last-message-text">{{ chat.last_message }}</div>
            </div>
        </div>
    </div>
</template>

<script>
import { getUserById, getUserId } from '@/modules/auth';

export default {
    name: 'chat-preview-block',
    props: ['chat'],
    data() {
        return {
            otherUser: null,
        }
    },
    async mounted() {
        const otherUserId = this.chat.members.find(id => getUserId(id));
        // console.log(this.chat.members, otherUserId);
        this.otherUser = await getUserById(otherUserId);
        // console.log(this.otherUser)
    },
    methods: {
        getUserName() {
            if (!this.otherUser) return '';
            return this.otherUser.firstName + ' ' + this.otherUser.lastName;
        }
    }
}
</script>

<style scoped>
.chat-preview-block {
    display: flex;
    flex: 0 0 100px;
    width: auto;
    margin: 10px;
    padding: 20px;
    border-radius: 15px;
    transition: background .2s linear;
}

.chat-preview-block.light-theme {
    color: #000;
}

.chat-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.chat-preview-block.light-theme:hover {
    background: rgba(0, 0, 0, 0.1);
}

.avatar,
.last-message-avatar {
    width: 60px;
    height: 60px;
    margin-right: 20px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
}

.chat-preview-block.light-theme .avatar {
    border: 1px #0000002f solid;
    background: rgba(0, 0, 0, 0.2);
}

.chat-preview-block.light-theme .last-message-avatar {
    border: 1px #0000002f solid;
    background: rgba(0, 0, 0, 0.2);
}

.chat-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.chat-header {
    display: flex;
    justify-content: space-between;
}

.chat-title {
    font-size: 15px;
}

.last-message {
    display: flex;
    align-items: center;
}

.last-message-avatar {
    width: 30px;
    height: 30px;
    margin-right: 10px;
}

.last-message-text,
.chat-last-changed {
    font-size: 13px;
    color: #969696;
}
</style>