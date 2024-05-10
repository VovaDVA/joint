<template>
    <div class="container" :class="$store.state.theme">
        <icon-button icon-name="arrow-left" style="font-size: 17px;" @click="closeChat()"></icon-button>
        <div class="chat-title">
            <div class="div">
                <slot></slot>
            </div>
            <div class="status">{{ status }}</div>
        </div>
        <div></div>
    </div>
</template>

<script>
export default {
    name: 'chat-title',
    props: ['status'],
    methods: {
        closeChat() {
            this.$emit('close-chat');
        }
    }
}
</script>

<style scoped>
.container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
    width: 100%;
    padding: 5px 10px;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50px;
}

.container.light-theme {
    background-color: #fff;
}

.chat-title {
    display: flex;
    flex-direction: column;
    gap: 3px;
    color: #ffbf6c;
}

.container.light-theme .chat-title {
    color: #965000;
}

.container.light-theme .status {
    color: #000000c4;
}

.status {
    font-size: 12px;
    color: #ffffffc4;
}
</style>