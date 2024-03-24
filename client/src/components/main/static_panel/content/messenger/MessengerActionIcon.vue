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
    width: 50px;
    /* padding: 10px; */
    font-family: 'Montserrat';
    font-size: 20px;
    line-height: 30px;
    border-radius: 30px;

    align-items: center;
    justify-content: center;

    background-color: rgba(0, 0, 0, 0.5);
    user-select: none;
    transition: color, background .3s linear;
}

.messenger-action-icon:hover {
    color: black;
    background-color: white;
    transition: color, background .3s linear;
}

.icon {
    display: flex;
    /* margin: 10px; */
    height: 20px;
    /* height: 15px; */
}

.svg-inline--fa {
    height: auto;
}
</style>