<template>
    <div class="chat-preview-block" :class="$store.state.theme">
        <user-avatar :photo="photo" :online="isOnline"></user-avatar>
        <div class="chat-info">
            <div class="chat-header">
                <div class="chat-title">{{ getUserName() }}</div>
                <div class="chat-last-changed">{{ lastMessageAt ?? '15:00' }}</div>
            </div>
            <div class="messages">
                <div class="last-message">
                    <user-avatar></user-avatar>
                    <div class="last-message-text" :class="{ typing: isTyping }">{{ status ?? ellipsify(lastMessage) }}
                    </div>
                </div>
                <div v-if="unreadMessages > 0" class="unread-messages">{{ unreadMessages }}</div>
            </div>
        </div>
    </div>
</template>

<script>
import { getUserById, isUserIdEqual } from '@/modules/auth';
import { formatTime } from '@/modules/utils';

export default {
    name: 'chat-preview-block',
    props: ['chat'],
    data() {
        return {
            otherUser: null,
            photo: require('@/assets/user.png'),
            isOnline: false,
            lastMessage: this.chat.last_message,
            lastMessageAt: formatTime(this.chat.last_message_at),
            unreadMessages: 0,
            isTyping: false,
            status: null
        }
    },
    async mounted() {
        const otherUserId = this.chat.members.find(id => isUserIdEqual(id));
        this.otherUser = await getUserById(otherUserId);
        this.photo = this.otherUser.avatar ?? this.photo;

        this.socket = this.$store.state.chatSocket;
        console.log(this.socket)

        this.socket.on('typing', (chatId) => {
            if (this.chat._id === chatId) {
                this.status = 'Печатает...';
                this.isTyping = true;
            }
        });

        this.socket.on('message', (message) => {
            if (this.chat._id === message.chat_id) {
                this.lastMessage = message.text;
                this.lastMessageAt = formatTime(message.created_at);
                this.unreadMessages++;
            }
        });

        this.socket.on('stopTyping', (chatId) => {
            if (this.chat._id === chatId) {
                this.status = null;
                this.isTyping = false;
            }
        });

        this.isOnline = otherUserId in this.$store.state.onlineUsers;
        this.socket.on('updateOnlineUsers', (onlineUsers) => {
            this.isOnline = otherUserId in onlineUsers;
        });
    },
    methods: {
        getUserName() {
            if (!this.otherUser) return '';
            return this.otherUser.firstName + ' ' + this.otherUser.lastName;
        },
        ellipsify(str) {
            if (str.length > 15) {
                return (str.substring(0, 15) + "...");
            }
            else {
                return str;
            }
        }
    }
}
</script>

<style scoped>
.chat-preview-block {
    display: flex;
    gap: 15px;
    flex: 0 0 100px;
    height: 100px;
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
    cursor: pointer;
    transition: background .2s linear;
}

.chat-preview-block.light-theme:hover {
    background: rgba(0, 0, 0, 0.1);
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

.messages {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.last-message {
    display: flex;
    gap: 10px;
    height: 30px;
    align-items: center;
}

.last-message-text,
.chat-last-changed {
    font-size: 13px;
    color: #969696;
}

.typing {
    color: #fff;
}

.unread-messages {
    padding: 5px 10px;
    font-size: 13px;
    background: #ffffff3b;
    border-radius: 30px;
}

.chat-preview-block.light-theme .unread-messages {
    background: #0000003b;
}


@media (max-width: 500px) {
    .chat-title {
        font-size: 14px;
    }
}
</style>