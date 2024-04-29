<template>
    <static-panel-header>
        <static-panel-search-bar @input="updateSearch"></static-panel-search-bar>
    </static-panel-header>
    <static-panel-header-menu>
        <icon-button icon-name="user"></icon-button>
        <icon-button icon-name="user-tag"></icon-button>
        <icon-button icon-name="wallet"></icon-button>
    </static-panel-header-menu>
    <static-panel-content>
        <account-preview-block v-for="item in filteredContent" :key="item.id" :person="item"></account-preview-block>
    </static-panel-content>
</template>

<script>
import { getToken } from '@/modules/auth';

export default {
    data() {
        return {
            items: [
                // { id: 1, firstName: 'Евгений Зойкин' },
                // { id: 2, firstName: 'Владимир Двойнишников' },
                // { id: 3, firstName: 'Алексей Ищенко' },
                // { id: 4, firstName: 'Анастасия Зубенко' },
                // { id: 5, firstName: 'Сергей Копытов' },
                // { id: 6, firstName: 'Максим Примак' },
                // { id: 7, firstName: 'Елизавета Рябухина' },
                // { id: 8, firstName: 'Кристина Плетюк' },
            ],
            searchText: '',
        };
    },
    async mounted() {
        const token = getToken();
        try {
            const response = await fetch('/auth/get-all', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
            });

            const data = await response.json();
            this.items = data;
            console.log(data);

        } catch (error) {
            console.error(error);
        }
    },
    computed: {
        // Фильтруем чаты в соответствии с текстом поиска
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

<style scoped></style>