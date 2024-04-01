<template>
    <div class="message">
        <div class="message-inner">
            <!-- <div class="author">Владимир</div> -->
            <div>
                <slot></slot>
            </div>
            <div class="send-time">
                <span>13:38 </span>
                <div class="icon">
                    <font-awesome-icon icon="check" />
                </div>
                <div class="icon read">
                    <font-awesome-icon icon="check" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'single-message',
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
.message {
    display: flex;
    justify-content: start;
    width: auto;
    height: fit-content;
    min-height: 30px;
    margin: 20px 10px;
    /* padding: 0 10px; */
}

.message.you {
    justify-content: end;
}

.message.you .message-inner {
    background-color: rgb(255, 210, 87);
}

.message-inner {
    /* display: flex; */
    width: fit-content;
    /* min-width: 100px; */
    max-width: 70%;
    height: 100%;
    padding: 10px;

    font-size: 17px;
    line-height: 17px;
    color: #000;

    border-radius: 15px;
    background-color: rgb(202, 202, 202);
}

.author {
    font-size: 15px;
    margin-bottom: 5px;
    font-weight: bold;
}

.send-time {
    padding-top: 3px;
    display: flex;
    justify-content: end;
    font-size: 12px;
    text-align: right;
    color: #000000a8;
    margin-bottom: -5px;
    user-select: none;
}

.icon {
    width: 12px;
    height: 12px;
    margin-left: 5px;
    align-items: baseline;
}

.icon.read {
    margin-left: -8px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}
</style>