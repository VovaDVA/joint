<template>
    <div class="search-bar">
        <div class="icon">
            <font-awesome-icon icon="search" />
        </div>
        <input class="message-input" type="text" placeholder="Поиск..." @input="search">
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'static-panel-search-bar',
    components: {
        FontAwesomeIcon
    },
    props: {
        iconName: {
            type: String, // Ожидаем строковое значение для имени иконки
            default: 'pen' // Значение по умолчанию
        }
    },
    methods: {
        search(event) {
            this.$emit('input', event.target.value); // Генерируем событие при изменении текста поиска
        }
    }
}
</script>

<style scoped>
.search-bar {
    display: flex;
    flex: 1;
    padding: 12px;

    line-height: 14px;
    color: #aaaaaa;
    align-items: center;

    border: 1px #ffffff4f solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
}

.icon {
    width: 14px;
    height: 14px;
    margin-right: 10px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}

input {
    padding: 0;
    border: none;
    outline: none;
    background-color: transparent;
    flex: 1;

    font-family: 'Montserrat', sans-serif;
    font-size: 14px;
}
</style>