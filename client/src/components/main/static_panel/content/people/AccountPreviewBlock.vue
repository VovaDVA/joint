<template>
    <div class="chat-preview-block">
        <div class="avatar"></div>
        <div class="chat-info">
            <div class="chat-header">
                <div class="chat-title">Владимир Двойнишников</div>
                <div class="buttons">
                    <icon-button icon-name="phone"></icon-button>
                    <icon-button icon-name="message"></icon-button>
                </div>
            </div>
            <div class="account-info">
                <div class="info">Программист</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'account-preview-block',
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
    transition: background .2s linear;
}

.chat-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.avatar,
.last-message-avatar {
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
    justify-content: space-around;
}

.chat-header {
    display: flex;
    justify-content: space-between;
}

.chat-title {
    font-size: 15px;
}

.account-info {
    display: flex;
    align-items: center;
}

.info {
    font-size: 13px;
    color: #969696;
}

.icon-button {
    padding: 0;
}

.buttons {
    display: flex;
}

.buttons * {
    margin-left: 20px;
}
</style>