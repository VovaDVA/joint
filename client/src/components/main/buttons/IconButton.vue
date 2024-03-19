<template>
    <div class="icon-button">
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
    border-radius: 30px;

    user-select: none;
    transition: color .2s linear;
}

.icon-button:hover {
    /* color: #ffd900; */
    color: rgb(0, 255, 42);
    transition: color .2s linear;
}

</style>