<template>
    <div class="chat-title" :class="$store.state.theme">
        <div class="div">
            <slot></slot>
        </div>
        <div class="status">{{ status }}</div>
    </div>
</template>

<script>
export default {
    name: 'chat-title',
    props: ['status']
}
</script>

<style scoped>
.chat-title {
    display: flex;
    flex-direction: column;
    gap: 3px;
    margin-bottom: 10px;
    padding: 5px;
    color: #ffbf6c;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50px;
}

.chat-title.light-theme {
    color: #965000;
    background-color: #fff;
}

.chat-title.light-theme .status {
    color: #000000c4;
}

.status {
    font-size: 12px;
    color: #ffffffc4;
}
</style>