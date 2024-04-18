<template>
    <static-panel-header>
        <messenger-action-icon icon-name="user"></messenger-action-icon>
        <messenger-action-icon icon-name="user-tag"></messenger-action-icon>
        <messenger-action-icon icon-name="wallet"></messenger-action-icon>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <account-preview-block v-for="item in filteredContent" :key="item.id" :name="item.name"></account-preview-block>
    </static-panel-content>
</template>

<script>
export default {
    data() {
        return {
            items: [
                { id: 1, name: 'Евгений Зойкин' },
                { id: 2, name: 'Владимир Двойнишников' },
                { id: 3, name: 'Алексей Ищенко' },
                { id: 4, name: 'Анастасия Зубенко' },
                { id: 5, name: 'Сергей Копытов' },
                { id: 6, name: 'Максим Примак' },
                { id: 7, name: 'Елизавета Рябухина' },
                { id: 8, name: 'Кристина Плетюк' },
            ], 
            searchText: '',
        };
    },
    computed: {
        // Фильтруем чаты в соответствии с текстом поиска
        filteredContent() {
            return this.items.filter(item => {
                return item.name.toLowerCase().includes(this.searchText.toLowerCase());
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