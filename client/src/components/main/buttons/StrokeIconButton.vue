<template>
    <div class="stroke-icon-button">
        <div class="icon">
            <font-awesome-icon :icon="iconName" />
        </div>
        <div class="icon-button-text">
            <slot></slot>
        </div>
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'stroke-icon-button',
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
.stroke-icon-button {
    display: flex;
    width: auto;
    /* padding: 10px; */
    font-size: 15px;
    line-height: 15px;
    border: 1px solid white;
    border-radius: 30px;

    /* background-color: rgba(0, 0, 0, 0.5); */
    user-select: none;
    transition: color, background .3s linear;
}

.stroke-icon-button:hover {
    color: black;
    background-color: white;
    transition: color, background .3s linear;
}

.icon-button-text {
    display: none;
    margin: 10px;
}

.stroke-icon-button:hover > .icon-button-text {
    display: block;
    transition: .3s linear;
}

.icon {
    margin: 10px;
    width: 15px;
    height: 15px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}

/* .icon .font-awesome-icon {
    position: absolute; 
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
} */
</style>