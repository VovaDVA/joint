<template>
    <static-panel-header>
        <messenger-action-icon icon-name="user"></messenger-action-icon>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <music-preview-block v-for="item in filteredContent" :key="item.id" :name="item.name"
            :author="item.author"></music-preview-block>
    </static-panel-content>
</template>

<script>
export default {
    data() {
        return {
            items: [
                { id: 1, author: 'Eminem', name: 'Guts Over Fear' },
                { id: 2, author: 'Eminem', name: 'Believe' },
                { id: 3, author: 'Eminem', name: 'Beautiful' },
                { id: 4, author: 'Linkin Park', name: 'Numb' },
                { id: 5, author: 'Linkin Park', name: 'From The Inside' },
                { id: 6, author: 'Linkin Park', name: 'Papercut' },
                { id: 7, author: 'Eminem', name: 'Gnat' },
                { id: 8, author: 'The Script', name: 'Hall of Fame' },
                { id: 9, author: 'PVRIS', name: 'Heaven' },
                { id: 10, author: 'PVRIS', name: 'Half' },
                { id: 11, author: 'Koven', name: 'Lasting' },
                { id: 12, author: 'Koven', name: 'One\'s Own' },
                { id: 13, author: 'Koven', name: 'From The Start' },
                { id: 14, author: 'Eminem', name: 'Legacy' },
                { id: 15, author: 'Bruno Mars, Bad Meets Evil', name: 'Lighters' },
                { id: 16, author: 'Fort Minor', name: 'Remember The Name' },
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