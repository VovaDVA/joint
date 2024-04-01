<template>
    <div class="icon">
        <font-awesome-icon :icon="iconName" />
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'single-icon',
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
.icon {
    font-size: 15px;
    line-height: 15px;

    user-select: none;
}
</style>