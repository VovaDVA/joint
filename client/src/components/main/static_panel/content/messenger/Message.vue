<template>
    <div class="message" :class="isMainUser() ? 'you' : ''">
        <div class="message-inner" @click="messageClick">
            <div>{{ message.text }}</div>
            <div class="send-time">
                <span>{{ createdTime() }}</span>
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
import { getUser } from '@/modules/auth';

export default {
    name: 'single-message',
    components: {
        FontAwesomeIcon
    },
    props: {
        message: {},
        iconName: {
            type: String, // Ожидаем строковое значение для имени иконки
            default: 'pen' // Значение по умолчанию
        }
    },
    methods: {
        isMainUser() {
            const user = getUser();
            if (!user) return true;
            return this.message['sender_id'] == user.userId;
        },
        createdTime() {
            const date = new Date(this.message.created_at);
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            const formattedTime = `${hours}:${minutes}`;
            return formattedTime;
        },
        messageClick() {
            this.$emit('message-click');
        }
    }
}
</script>

<style scoped>
.message {
    position: relative;
    display: flex;
    justify-content: flex-start;
    width: auto;
    height: fit-content;
    /* min-height: 30px; */
    margin: 10px;
    user-select: none;
}

.message.you {
    justify-content: end;
}

.message.you .message-inner {
    background-color: rgb(151, 208, 255);
}

.message-inner {
    width: fit-content;
    max-width: 70%;
    height: 100%;
    padding: 10px;

    font-family: Arial, Helvetica, sans-serif;
    font-size: 17px;
    line-height: 17px;
    color: #000;

    border-radius: 15px;
    background-color: rgb(226, 226, 226);
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


@media (max-width: 800px) {
    .message-inner {
        font-size: 17px;
    }

    .send-time {
        font-size: 10px;
    }
}
</style>