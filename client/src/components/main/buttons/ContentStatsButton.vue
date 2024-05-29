<template>
    <div class="content-stats-button" :class="$store.state.theme">
        <div class="icon">
            <font-awesome-icon :icon="iconName" />
        </div>
        <span v-if="textExists" class="icon-button-text">
            <slot></slot>
        </span>
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';
import { useSlots } from 'vue';

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
    },
    methods: {
        textExists() {
            return !!useSlots();
        }
    }
}
</script>

<style scoped>
.content-stats-button {
    display: flex;
    gap: 10px;
    width: auto;
    padding: 10px;
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

.content-stats-button.marked {
    background-color: rgba(0, 140, 255, 0.747);
}

.content-stats-button:hover {
    color: black;
    background-color: white;
    transition: color, background .3s linear;
}

.content-stats-button.marked:hover {
    color: rgba(0, 0, 0, 1);
}

.content-stats-button.light-theme:hover {
    background-color: #000000;
    color: #ffffff;
}

.icon-button-text {
    white-space: nowrap;
}

.content-stats-button.marked {
    color: #ffffff;
}

.icon {
    display: flex;
    justify-content: center;
    width: 15px;
    height: 15px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}
</style>