<template>
    <static-panel-header>
        <messenger-action-icon icon-name="user"></messenger-action-icon>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <content-preview-block v-for="item in filteredContent" :key="item.id" :name="item.name"
            :author="item.author"></content-preview-block>
    </static-panel-content>
</template>

<script>
export default {
    data() {
        return {
            items: [
                { id: 1, author: 'VovaDVA', name: 'Как манипулировать людьми?' },
                { id: 2, author: 'VovaDVA', name: 'Пишем свою соцсеть' },
                { id: 3, author: 'VovaDVA', name: 'Планета Н-0131 | Обзор №1' },
                { id: 4, author: 'VovaDVA', name: 'Планета Н-0131 | Обзор №2 | Сюжет' },
                { id: 5, author: 'VovaDVA', name: 'Планета Н-0131 | Обзор №3 | Геймплей' },
                { id: 6, author: 'VovaDVA', name: 'Планета Н-0131 | Обзор №4 | Бой' },
                { id: 7, author: 'VovaDVA', name: 'Разработка Телеграм-бота с нуля на Go и C++' },
                { id: 8, author: 'VovaDVA', name: 'Подключение MongoDB в С++' },
                { id: 9, author: 'VovaDVA', name: 'ООП для чайников' },
                { id: 10, author: 'VovaDVA', name: 'Секретные секреты' },
            ],
            searchText: '',
        };
    },
    computed: {
        // Фильтруем чаты в соответствии с текстом поиска
        filteredContent() {
            return this.items.filter(item => {
                return item.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
                    item.author.toLowerCase().includes(this.searchText.toLowerCase());
            });
        }
    },
    methods: {
        updateSearch(text) {
            if (typeof text === 'string') {
                this.searchText = text;
            }
            console.log(typeof this.searchText);
        }
    }
}
</script>

<style scoped></style>