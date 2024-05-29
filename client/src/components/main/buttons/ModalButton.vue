<template>
    <div class="button" :class="$store.state.theme">
        <div v-if="iconName" class="icon">
            <font-awesome-icon :icon="iconName" />
        </div>
        <span class="button-text">
            <slot></slot>
        </span>
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'modal-button',
    components: {
        FontAwesomeIcon
    },
    props: {
        iconName: {
            type: String, // Ожидаем строковое значение для имени иконки
        }
    }
}
</script>

<style scoped>
.button {
    display: flex;
    gap: 10px;
    width: 100%;
    max-width: 200px;
    font-family: 'Montserrat';
    font-size: 15px;
    border-radius: 30px;
    padding: 5px 20px;
    color: #000;

    background-color: #ffffff;
    user-select: none;
    transition: color, background .2s linear;
}

.button:hover {
    background-color: #cecece;
    cursor: pointer;
    transition: color, background .2s linear;
}

.button.light-theme {
    background: #d39000;
    color: #ffffff;
}

.button.cancel {
    background: #8585853d;
    color: #ffffff;
}

.button.light-theme:hover {
    background: #b47b00;
    color: #ffffff;
}

.cancel:hover,
.cancel.light-theme:hover {
    background: #85858557;
}

.cancel.light-theme, .cancel.light-theme:hover {
    color: #000;
}

.button.red {
    color: #ffffff;
    background: #b33232;
}

.button.red:hover {
    background: #912828;
}

.button-text {
    width: 100%;
    white-space: nowrap;
    text-align: center;
}

.icon {
    display: flex;
    align-items: center;
    margin: 10px;
    width: 15px;
    height: 15px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}
</style>
