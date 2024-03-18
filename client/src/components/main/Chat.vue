<template>
    <div class="chat">
        <div class="chat-inner">

        </div>
    </div>
</template>

<script>
export default {
    name: 'side-chat',
}
</script>

<style scoped>
.chat {
    position: fixed;
    top: 70px;
    right: 0;
    width: 30vw;
    /* max-width: 500px; */
    height: calc(100vh - 90px);
    margin: 10px;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.5);
}

/* .chat {
    position: fixed;
    top: 0;
    right: -300px;
    width: 300px;
    height: 100%;
    background-color: #3f3f3f;
    transition: right 0.3s ease;
} */
</style>