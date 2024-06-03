<template>
    <static-panel-header>
        <!-- <messenger-action-icon icon-name="arrow-left" @click="openChatList"></messenger-action-icon> -->
        <!-- <static-panel-search-bar></static-panel-search-bar> -->
        <chat-title :status="status" @close-chat="openChatList">{{ chatName }}</chat-title>
    </static-panel-header>
    <static-panel-content>
        <div class="message-container">
            <single-message v-for="message in messages" :key="message._id" :message="message"
                @messageClick="selectMessage(message)" />
        </div>
    </static-panel-content>
    <div class="chat-input" :class="$store.state.theme">
        <icon-button icon-name="paperclip"></icon-button>
        <input class="message-input" type="text" placeholder="Напишите сообщение..." v-model="newMessage"
            @keyup.enter="inputAction()" @input="type()">
        <icon-button icon-name="face-smile"></icon-button>
        <icon-button v-if="!messageEdited" class="right" icon-name="paper-plane" @click="sendMessage"></icon-button>
        <icon-button v-if="messageEdited" class="right" icon-name="check" @click="editMessage"></icon-button>
        <!-- <icon-button class="right" icon-name="microphone" @click="sendMessage"></icon-button> -->
    </div>
    <message-context-menu v-if="showMenu" :topPosition="menuTop" :leftPosition="menuLeft" :message="selectedMessage"
        ref="menuRef" />
</template>

<script>
import CryptoJS from "crypto-js";
import apiClient, { BASE_URL } from '@/modules/ApiClient';
import { getUser, isUserIdEqual, getUserById } from '@/modules/auth';

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
            newMessage: '',
            isOnline: false,
            status: this.getStatus(),
            typingTimeout: null,
            showMenu: false,
            menuTop: 0,
            menuLeft: 0,
            selectedMessage: null,
            messageEdited: false,
        }
    },
    created() {
        this.emitter.on('delete-message', (messageId) => {
            this.showMenu = false;
            const message = this.messages.find(message => message._id == messageId);
            const index = this.messages.indexOf(message);
            this.messages.splice(index, 1);
        });
        this.emitter.on('edit-message', (message) => {
            this.showMenu = false;
            this.newMessage = message.text;
            this.messageEdited = true;
        });
        this.emitter.on('copy-message', () => {
            this.showMenu = false;
        });
    },
    async mounted() {
        const otherUserId = this.chat.members.find(id => isUserIdEqual(id));
        this.otherUser = await getUserById(otherUserId);

        if (this.otherUser) {
            this.chatName = this.otherUser.firstName + ' ' + this.otherUser.lastName;
        }

        const chat = this.chat;
        try {
            const response = await fetch(`${BASE_URL}:3000/chat/getMessages?chat_id=` + chat._id, {
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

        this.socket = this.$store.state.chatSocket;
        this.socket.emit('joinChat', this.chat._id);

        this.socket.on('message', (message) => {
            this.messages.unshift(message);
        });
        this.socket.on('typing', () => {
            this.status = `${this.otherUser.firstName} печатает...`;
        });
        this.socket.on('stopTyping', () => {
            this.status = this.getStatus();
        });

        this.showStatus(this.$store.state.onlineUsers, otherUserId);
        this.socket.on('updateOnlineUsers', (onlineUsers) => this.showStatus(onlineUsers, otherUserId));
    },
    methods: {
        inputAction() {
            if (this.messageEdited) {
                this.editMessage();
            } else {
                this.sendMessage();
            }
        },
        sendMessage() {
            if (this.newMessage !== '') {
                const encryptedMessage = CryptoJS.AES.encrypt(this.newMessage, 'secret').toString();

                const messageData = {
                    "chat_id": this.chat._id,
                    "sender_id": getUser().userId,
                    "text": encryptedMessage,
                    "created_at": new Date()
                }

                this.socket.emit('sendMessage', messageData);
                this.newMessage = '';
            }
        },
        async editMessage() {
            await apiClient.message.editMessage({
                message_id: this.selectedMessage._id,
                text: this.newMessage,
            }, (data) => {
                const message = this.messages.find(message => message._id == data._id);
                const index = this.messages.indexOf(message);
                this.messages[index] = data;

                this.newMessage = '';
                this.messageEdited = false;
            });
        },
        openChatList() {
            this.$emit('open-chat-list');
        },
        type() {
            clearTimeout(this.typingTimeout);
            this.socket.emit('typing', this.chat._id);

            this.typingTimeout = setTimeout(() => {
                this.socket.emit('stopTyping', this.chat._id);
            }, 1000);
        },
        selectMessage(message) {
            this.showMenu = true;
            this.selectedMessage = message;

            const containerRect = document.querySelector('.message-container').getBoundingClientRect();
            // Получите позицию сообщения относительно верхнего края контейнера
            const messageTop = event.clientY - containerRect.top;
            // Получите позицию сообщения относительно левого края контейнера
            const messageLeft = event.clientX - containerRect.left;
            // Установите позицию меню на основе позиции сообщения в контейнере
            this.menuTop = messageTop;
            this.menuLeft = messageLeft;

            // window.addEventListener('click', this.handleClickOutside);
        },
        handleClickOutside(event) {
            // Проверяем, был ли клик сделан вне компонента сообщения или меню
            if (!this.$refs.menuRef.contains(event.target)) {
                this.showMenu = false;
                window.removeEventListener('click', this.handleClickOutside);
            }
        },
        getStatus() {
            return this.isOnline ? 'В сети' : 'Был(а) в сети недавно';
        },
        showStatus(onlineUsers, userId) {
            this.isOnline = userId in onlineUsers;
            this.status = this.getStatus();
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

.chat-input.light-theme {
    background: #fff;
    border: 1px #0000004f solid;
}

.chat-input.light-theme input {
    color: #000 !important;
}

.chat-input.light-theme input::-webkit-input-placeholder {
    /* WebKit, Blink, Edge */
    color: #0000007e;
}

.chat-input.light-theme input:-moz-placeholder {
    /* Mozilla Firefox 4 to 18 */
    color: #0000007e;
    opacity: 1;
}

.chat-input.light-theme input::-moz-placeholder {
    /* Mozilla Firefox 19+ */
    color: #0000007e;
    opacity: 1;
}

.chat-input.light-theme input:-ms-input-placeholder {
    /* Internet Explorer 10-11 */
    color: #0000007e;
}

.chat-input.light-theme input::-ms-input-placeholder {
    /* Microsoft Edge */
    color: #0000007e;
}

.chat-input.light-theme input::placeholder {
    /* Most modern browsers support this now. */
    color: #0000007e;
}

.message-input {
    flex: 1;
    font-size: 17px;
    line-height: 15px;
    border: none;
    background-color: rgba(0, 0, 0, 0);
    outline: none;
}

.icon-button {
    font-size: 20px;
}

@media (max-width: 380px) {
    .message-input {
        font-size: 14px;
    }
}
</style>