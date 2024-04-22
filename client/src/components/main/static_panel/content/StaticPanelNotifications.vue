<template>
    <static-panel-header>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-content>
        <notification-preview-block v-for="item in filteredContent" :key="item.id" :name="item.name"
            :author="item.author"></notification-preview-block>
    </static-panel-content>
</template>

<script>
export default {
    data() {
        return {
            items: [
                { id: 1, author: 'VovaDVA', name: 'Как манипулировать людьми?' },
                { id: 2, author: 'VovaDVA', name: 'Как манипулировать женщинами?' },
                { id: 3, author: 'VovaDVA', name: 'Как понять, что вами манипулируют?' },
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