<template>
    <static-panel-header>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-header-menu>
        <icon-button icon-name="user"></icon-button>
        <icon-button icon-name="user-check"></icon-button>
        <icon-button icon-name="user-tie"></icon-button>
    </static-panel-header-menu>
    <static-panel-content>
        <account-preview-block v-for="item in filteredContent" :key="item.id" :person="item"></account-preview-block>
    </static-panel-content>
</template>

<script>
import apiClient from '@/modules/ApiClient';

export default {
    data() {
        return {
            items: [
                // { id: 1, firstName: 'Евгений Зойкин' },
                // { id: 2, firstName: 'Владимир Двойнишников' },
            ],
            searchText: '',
        };
    },
    async mounted() {
        await apiClient.auth.getAll((data) => this.items = data);
    },
    computed: {
        filteredContent() {
            return this.items.filter(item => {
                return item.firstName.toLowerCase().includes(this.searchText.toLowerCase());
            });
        }
    },
    methods: {
        updateSearch(text) {
            if (typeof text === 'string') {
                this.searchText = text;
            }
        }
    }
}
</script>

<style scoped>
.icon-button {
    font-size: 20px;
}
</style>