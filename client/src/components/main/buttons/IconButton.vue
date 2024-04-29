<template>
    <div class="icon-button" :class="$store.state.theme">
        <font-awesome-icon :icon="iconName" />
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'icon-button',
    components: {
        FontAwesomeIcon
    },
    props: {
        iconName: {
            type: String, // Ожидаем строковое значение для имени иконки
            default: 'pen' // Значение по умолчанию
        }
    }
}
</script>

<style scoped>
.icon-button {
    padding: 10px;
    font-size: 15px;
    line-height: 15px;

    user-select: none;
    transition: color .2s linear;
}

.icon-button.light-theme {
    color: #000;
}

.icon-button:hover {
    color: #ffa600;
    transition: color .2s linear;
}
</style>