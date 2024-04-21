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
export default {
    data() {
        return {
            chats: [
                {
                    "_id": 1,
                    "members": [1234, 5678],
                    "created_at": '2024-04 - 16T09: 21: 52.696+00:00',
                    "last_message_at": '21:00',
                    "last_message": 'Привет'
                },
                {
                    "_id": 2,
                    "members": [1234, 6256],
                    "created_at": '2024-04 - 16T09: 21: 52.696+00:00',
                    "last_message_at": '15:01',
                    "last_message": 'Хорошо, договорились'
                },
                {
                    "_id": 3,
                    "members": [1234, 2626],
                    "created_at": '2024-04 - 16T09: 21: 52.696+00:00',
                    "last_message_at": '7:45',
                    "last_message": 'Не знаю, скорее всего'
                },
                {
                    "_id": 4,
                    "members": [1234, 3474],
                    "created_at": '2024-04 - 16T09: 21: 52.696+00:00',
                    "last_message_at": '2:54',
                    "last_message": 'Пока'
                },
                // { id: 2, name: 'Евгений Зойкин' },
                // { id: 1, name: 'Владимир Двойнишников' },
                // { id: 2, name: 'Алексей Ищенко' },
                // { id: 3, name: 'Анастасия Зубенко' },
                // { id: 3, name: 'Сергей Копытов' },
                // { id: 3, name: 'Максим Примак' },
                // { id: 3, name: 'Елизавета Рябухина' },
                // { id: 3, name: 'Кристина Плетюк' },
            ],
            searchText: '',
        };
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