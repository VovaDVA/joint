<template>
    <div class="chat-block search-bar"></div>
    <div class="chat-block chat-content"></div>
    <div class="chat-input">
        <messenger-action-icon icon-name="face-smile"></messenger-action-icon>
        <input class="message-input" type="text" placeholder="Напишите сообщение...">
        <messenger-action-icon icon-name="paper-plane"></messenger-action-icon>
        <messenger-action-icon class="voice" icon-name="microphone"></messenger-action-icon>
    </div> 
</template>

<style scoped>
.chat-block {
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.5);
}

.search-bar {
    flex: 0 0 50px;
    margin-bottom: 10px;
}

.chat-content {
    flex: 1;
}

.chat-input {
    flex: 0 0 50px;
    margin-top: 10px;
    display: flex;
}

.message-input {
    flex: 1;
    height: 100%;
    padding: 10px 20px;
    font-size: 15px;
    border: 1px #ffffff7c solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
}

.message-input {
    margin: 0 10px;
    border: 1px #ffffff7c solid;
}

.voice {
    margin-left: 10px;
}
</style>