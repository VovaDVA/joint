<template>
    <static-panel-header>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <chat-preview-block v-for="chat in filteredChats" :key="chat.id" @click="openChat" :chatName="chat.name"></chat-preview-block>
    </static-panel-content>
</template>

<script>
export default {
    data() {
        return {
            chats: [
                { id: 2, name: 'Евгений Зойкин' },
                { id: 1, name: 'Владимир Двойнишников' },
                { id: 2, name: 'Алексей Ищенко' },
                { id: 3, name: 'Анастасия Зубенко' },
                { id: 3, name: 'Сергей Копытов' },
                { id: 3, name: 'Максим Примак' },
                { id: 3, name: 'Елизавета Рябухина' },
                { id: 3, name: 'Кристина Плетюк' },
            ], 
            searchText: '',
        };
    },
    computed: {
        // Фильтруем чаты в соответствии с текстом поиска
        filteredChats() {
            return this.chats.filter(chat => {
                return chat.name.toLowerCase().includes(this.searchText.toLowerCase());
            });
        }
    },
    methods: {
        updateSearch(text) {
            if (typeof text === 'string') {
                this.searchText = text;
            }
            console.log(typeof this.searchText);
        },
        openChat() {
            this.$emit('open-chat');
        }
    }
}
</script>

<style scoped></style>