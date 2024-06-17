<template>
    <div class="context-menu" :style="{ top: topPosition + 'px', left: leftPosition + 'px' }" :class="[menuClass, $store.state.theme]">
        <div class="context-menu-item" @click="copyMessage">
            <single-icon iconName="copy"></single-icon>
            <div class="item-name">Копировать</div>
        </div>
        <div class="context-menu-item" @click="editMessage">
            <single-icon iconName="pen"></single-icon>
            <div class="item-name">Редактировать</div>
        </div>
        <div class="context-menu-item" @click="editMessage">
            <single-icon iconName="share"></single-icon>
            <div class="item-name">Переслать</div>
        </div>
        <div class="context-menu-item delete" @click="deleteMessage">
            <single-icon iconName="trash-can"></single-icon>
            <div class="item-name">Удалить</div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'message-context-menu',
    props: ['topPosition', 'leftPosition', 'message'],
    computed: {
        menuClass() {
            // Определите класс, который будет использоваться для позиционирования меню
            const classes = [];
            if (this.topPosition > window.innerHeight / 2) {
                classes.push('top');
            } else {
                classes.push('bottom');
            }
            if (this.leftPosition > window.innerWidth / 2) {
                classes.push('left');
            } else {
                classes.push('right');
            }
            return classes.join(' ');
        }
    },
    methods: {
        editMessage() {
            this.emitter.emit('edit-message', this.message);
        },
        async deleteMessage() {
            try {
                const response = await fetch('http://82.97.245.142:3000/message/deleteMessage?message_id=' + this.message._id);
                const data = await response.json();
                console.log(data);
                this.emitter.emit('delete-message', this.message._id);

            } catch (error) {
                console.error(error);
            }
        },
        copyMessage() {
            navigator.clipboard.writeText(this.message.text)
                .then(() => {
                    console.log('Текст скопирован в буфер обмена');
                })
                .catch(err => {
                    console.error('Не удалось скопировать текст: ', err);
                });

            this.emitter.emit('copy-message');
        }
    }
}
</script>

<style scoped>
.context-menu {
    position: absolute;
    border: 1px solid #ffffff7c;
    z-index: 1000;
    min-width: 150px;
    padding: 10px;
    background-color: rgba(0, 0, 0, 0.9);
    border-radius: 20px;
    user-select: none;
}

.context-menu.light-theme {
    color: #000000;
    border: 1px solid #0000007c;
    background-color: rgba(255, 255, 255, 1);
}

.context-menu-item {
    padding: 10px;
    font-size: 13px;
    display: flex;
    align-items: center;
    gap: 10px;
    border-radius: 10px;
    transition: background .2s linear;
}

.context-menu-item:hover {
    background: rgba(255, 255, 255, 0.2);
    transition: background .2s linear;
}

.context-menu.light-theme .context-menu-item:hover {
    background: rgba(0, 0, 0, 0.1);
}

.delete {
    color: #ff8686;
}

/* Позиционирование угла */
.context-menu.top.left {
    transform: translate(-100%, -100%);
}

.context-menu.top.right {
    transform: translate(0, -100%);
}

.context-menu.bottom.left {
    transform: translate(-100%, 0);
}

.context-menu.bottom.right {
    transform: translate(0, 0);
}
</style>
