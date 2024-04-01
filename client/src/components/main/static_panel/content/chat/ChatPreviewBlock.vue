<template>
    <div class="chat-preview-block">
        <div class="avatar"></div>
        <div class="chat-info">
            <div class="chat-header">
                <div class="chat-title">Владимир Двойнишников</div>
                <div class="chat-last-changed">21:00</div>
            </div>
            <div class="last-message">
                <div class="last-message-avatar"></div>
                <div class="last-message-text">Сообщение</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'chat-preview-block',
}
</script>

<style scoped>
.chat-preview-block {
    display: flex;
    flex: 0 0 100px;
    width: auto;
    margin: 10px;
    padding: 20px;
    border-radius: 15px;
    /* border-bottom: 1px #ffffff2f solid; */
    transition: background .2s linear;
}

.chat-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.avatar, .last-message-avatar {
    width: 60px;
    height: 60px;
    margin-right: 20px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
}

.chat-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.chat-header {
    display: flex;
    justify-content: space-between;
}

.chat-title {
    font-size: 15px;
    /* color: #ffbf6c; */
}

.last-message {
    display: flex;
    align-items: center;
}

.last-message-avatar {
    width: 30px;
    height: 30px;
    margin-right: 10px;
}

.last-message-text, .chat-last-changed {
    font-size: 13px;
    color: #969696;
}
</style>