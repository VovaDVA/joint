<template>
    <static-panel-header>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <chat-preview-block v-for="chat in filteredChats" :key="chat._id" @click="openChat(chat)"
            :chat="chat"></chat-preview-block>
    </static-panel-content>
</template>

<script>
import { getUser } from '@/modules/auth';

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
    async mounted() {
        const user = getUser();
        if (!user) return;
        try {
            const response = await fetch('/chat/getUserChats?user_id=' + user.userId.toString(), {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });

            const data = await response.json();
            console.log(data);
            this.chats = data;

        } catch (error) {
            console.error(error);
        }
    },
    computed: {
        // Фильтруем чаты в соответствии с текстом поиска
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
            this.$emit('open-chat', chat);
        }
    }
}
</script>

<style scoped></style>