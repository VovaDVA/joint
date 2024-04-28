<template>
    <static-panel-header>
        <messenger-action-icon icon-name="arrow-left" @click="openChatList"></messenger-action-icon>
        <static-panel-search-bar></static-panel-search-bar>
    </static-panel-header>
    <chat-title>{{ chatName }}</chat-title>
    <static-panel-content>
        <single-message v-for="message in messages" :key="message._id" :message="message" />
    </static-panel-content>
    <div class="chat-input">
        <icon-button icon-name="paperclip"></icon-button>
        <input class="message-input" type="text" placeholder="Напишите сообщение..." v-model="newMessage"
            @keyup.enter="sendMessage">
        <icon-button icon-name="face-smile"></icon-button>
        <icon-button class="right" icon-name="paper-plane" @click="sendMessage"></icon-button>
        <!-- <icon-button class="right" icon-name="microphone" @click="sendMessage"></icon-button> -->
    </div>
</template>

<script>
import { getUser, getUserById } from '@/modules/auth';

// import { getUser } from '@/modules/auth';

// import { getUserById } from '@/modules/auth';

// import { io } from 'socket.io-client';

export default {
    props: ['chat'],
    data() {
        return {
            socket: null,
            chatName: 'ЕПТА team',
            messages: [
                {
                    "_id": 1,
                    "chat_id": 1,
                    "sender_id": 5678,
                    "text": "Привет",
                    "created_at": '2024-04 - 16T09: 54:00.063 +00:00'
                },
                {
                    "_id": 2,
                    "chat_id": 1,
                    "sender_id": 1234,
                    "text": "Привет",
                    "created_at": '2024-04 - 16T09: 54:00.063 +00:00'
                }
            ],
            newMessage: ''
        }
    },
    async mounted() {
        const otherUserId = this.chat.members.find(id => id != getUser().id);
        this.otherUser = await getUserById(otherUserId);
        if (this.otherUser) {
            this.chatName = this.otherUser.firstName + ' ' + this.otherUser.lastName;
        }

        const chat = this.chat;
        try {
            const response = await fetch('/chat/getMessages?chat_id=' + chat._id, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });

            const data = await response.json();
            console.log(data);
            this.messages = data;

        } catch (error) {
            console.error(error);
        }
        // const otherUserId = this.user
        // this.chatName = getUserById();
        // this.socket = io('http://127.0.0.1:3000');
        // this.socket.on('chat message', (msg) => {
        //     this.messages.push({ id: this.messages.length + 1, text: msg });
        // });
    },
    methods: {
        sendMessage() {
            if (this.newMessage) {
                this.socket.emit('chat message', this.newMessage);
                this.newMessage = '';
            }
        },
        openChatList() {
            this.$emit('open-chat-list');
        }
    }
}
</script>

<style scoped>
.chat-input {
    flex: 0 0 50px;
    margin-top: 10px;
    display: flex;
    padding: 5px;
    font-size: 15px;
    border: 1px #ffffff4f solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
    outline: none;
}

.message-input {
    flex: 1;
    font-size: 15px;
    line-height: 15px;
    border: none;
    background-color: rgba(0, 0, 0, 0);
    outline: none;
}

.icon-button {
    font-size: 20px;
}
</style>