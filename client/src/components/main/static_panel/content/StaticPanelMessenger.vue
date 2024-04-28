<template>
    <static-panel-header>
        <messenger-action-icon icon-name="arrow-left" @click="openChatList"></messenger-action-icon>
        <static-panel-search-bar></static-panel-search-bar>
    </static-panel-header>
    <chat-title>{{ chatName }}</chat-title>
    <static-panel-content>
        <div class="message-container">
            <single-message v-for="message in messages" :key="message._id" :message="message" />
        </div>
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
import { getUser, getUserId, getUserById } from '@/modules/auth';
import { io } from 'socket.io-client';

export default {
    props: ['chat'],
    data() {
        return {
            socket: null,
            chatName: '',
            messages: [
                // {
                //     "_id": 1,
                //     "chat_id": 1,
                //     "sender_id": 5678,
                //     "text": "Привет",
                //     "created_at": '2024-04 - 16T09: 54:00.063 +00:00'
                // },
            ],
            newMessage: ''
        }
    },
    async mounted() {
        const otherUserId = this.chat.members.find(id => getUserId(id));
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
            this.messages = data.reverse();

        } catch (error) {
            console.error(error);
        }

        this.socket = io('http://192.168.0.107:3000', {
            query: {
                chatId: this.chat._id
            }
        });
        this.socket.on('message', (message) => {
            this.messages.unshift(message);
        });
    },
    methods: {
        sendMessage() {
            const messageData = {
                chat_id: this.chat._id,
                sender_id: getUser().userId,
                text: this.newMessage,
            }
            if (this.newMessage !== '') {
                this.socket.emit('sendMessage', messageData);
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
.message-container {
    height: 100%;
    position: absolute;
    display: flex;
    flex-direction: column-reverse;
    overflow-y: auto;
    width: 100%;
}

.message-container::-webkit-scrollbar {
    width: 0;
}

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