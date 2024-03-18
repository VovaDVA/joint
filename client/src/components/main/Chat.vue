<template>
    <div class="icon-container">
        <filled-icon-button icon-name="user-group">Друзья</filled-icon-button>
        <filled-icon-button icon-name="message">Мессенджер</filled-icon-button>
        <filled-icon-button icon-name="code">Ресурсы</filled-icon-button>
        <filled-icon-button icon-name="search">Поиск</filled-icon-button>
        <filled-icon-button icon-name="envelope">Почта</filled-icon-button>
        <filled-icon-button icon-name="wallet">Кошелек</filled-icon-button>
        <filled-icon-button icon-name="suitcase">Работа</filled-icon-button>
    </div>
    <div class="chat">
        <div class="chat-inner">
            <div class="chat-block search-bar"></div>
            <div class="chat-block chat-content"></div>
            <div class="chat-block chat-input"></div>
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

.chat-inner {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    padding: 10px;
}

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

.chat-input{
    flex: 0 0 100px;
    margin-top: 10px;
}

.icon-container {
    position: fixed;
    top: 80px;
    right: calc(30vw + 20px);
    margin-top: 10px;
    display: flex;
    justify-content: flex-end;
    align-items:end;
    flex-direction: column;
}

.icon-container .filled-icon-button {
    margin-bottom: 10px;
}
</style>