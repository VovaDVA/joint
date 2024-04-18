<template>
    <div class="messenger-action-icon">
        <div class="icon">
            <font-awesome-icon :icon="iconName" />
        </div>
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'messenger-action-icon',
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
.messenger-action-icon {
    display: flex;
    /* height: auto; */
    aspect-ratio: 1/1;
    border-radius: 30px;
    margin-right: 10px;

    align-items: center;
    justify-content: center;

    background-color: rgba(0, 0, 0, 0.5);
    user-select: none;
    transition: color, background .3s linear;
}

.messenger-action-icon.right {
    margin-right: 0;
    margin-left: 10px;
}

.messenger-action-icon:hover {
    color: black;
    background-color: white;
    transition: color, background .3s linear;
}

.icon {
    display: flex;
    height: 20px;
}

.svg-inline--fa {
    height: auto;
}
</style>