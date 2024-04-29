<template>
    <div class="content-stats-button" :class="$store.state.theme">
        <div class="icon">
            <font-awesome-icon :icon="iconName" />
        </div>
        <span class="icon-button-text">
            <slot></slot>
        </span>
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'content-stats-button',
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
.content-stats-button {
    display: flex;
    width: auto;
    /* padding: 10px; */
    font-family: 'Montserrat';
    font-size: 15px;
    line-height: 15px;
    border-radius: 30px;

    align-items: center;
    justify-content: center;

    background-color: rgba(0, 0, 0, 0.5);
    user-select: none;
    transition: color, background .3s linear;
}

.content-stats-button.light-theme {
    background-color: #ffffff;
    color: #000;
}

.content-stats-button:hover {
    color: black;
    background-color: white;
    transition: color, background .3s linear;
}

.icon-button-text {
    white-space: nowrap;
    margin-right: 10px;
}

.icon {
    display: flex;
    justify-content: center;
    margin: 10px;
    width: 15px;
    height: 15px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}
</style>