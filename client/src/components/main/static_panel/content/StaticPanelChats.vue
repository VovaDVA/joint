<template>
    <static-panel-header>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <chat-preview-block v-for="chat in chats" :key="chat._id" @click="openChat(chat)"
            :chat="chat"></chat-preview-block>
    </static-panel-content>
</template>

<script>
import apiClient from '@/modules/ApiClient';

export default {
    data() {
        return {
            chats: [
                // {
                //     "_id": 1,
                //     "members": [1234, 5678],
                //     "created_at": '2024-04 - 16T09: 21: 52.696+00:00',
                //     "last_message_at": '21:00',
                //     "last_message": 'Привет'
                // },
            ],
            searchText: '',
        };
    },
    created() {
        this.emitter.on('create-chat', async (members) => {
            await apiClient.chat.createChat(members, (data) => this.openChat(data));
        });
    },
    async mounted() {
        await apiClient.chat.getUserChats((data) => this.chats = data);
    },
    computed: {
        filteredChats() {
            return this.chats;
            // return this.chats.filter(() => {
            //     // return chat.name.toLowerCase().includes(this.searchText.toLowerCase());
            //     return '';
            // });
        }
    },
    methods: {
        updateSearch(text) {
            if (typeof text === 'string') {
                this.searchText = text;
            }
        },
        openChat(chat) {
            this.emitter.emit('open-messenger', chat);
            this.$emit('open-chat', chat);
        }
    }
}
</script>

<style scoped></style>