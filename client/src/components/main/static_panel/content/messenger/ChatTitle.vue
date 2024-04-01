<template>
    <div class="chat-title"><slot></slot></div>
</template>

<script>
export default {
    name: 'chat-title',
}
</script>

<style scoped>
.chat-title {
    margin-bottom: 10px;
    padding: 10px;
    color: #ffbf6c;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50px;
}
</style>