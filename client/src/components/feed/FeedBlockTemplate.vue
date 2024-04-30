<template>
    <div class="feed-block" :class="$store.state.theme">
        <div class="header">
            <div class="avatar"></div>
            <div class="author">
                <div class="username">{{ name }} Владимир Двойнишников</div>
                <div class="date">{{ author }} 5 марта 21:00</div>
            </div>
        </div>
        <slot></slot>
    </div>
</template>

<script>
export default {
    name: 'feed-block-template',
}
</script>

<style scoped>
.feed-block {
    display: flex;
    flex-direction: column;
    gap: 10px;
    height: fit-content;
    padding: 10px;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-block.light-theme {
    border: 1px #0000007c solid;
    background: rgba(255, 255, 255, 0.5);
    color: #000;
}

.header {
    display: flex;
    gap: 10px;
}

.author {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}

.avatar {
    width: 45px;
    height: 45px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
}
.feed-block.light-theme .avatar {
    border: 1px #0000002f solid;
    background: rgba(0, 0, 0, 0.2);
}

.date {
    font-size: 12px;
    color: #ffffff7c;
}
.feed-block.light-theme .date {
    font-size: 12px;
    color: #0000007c;
}
</style>