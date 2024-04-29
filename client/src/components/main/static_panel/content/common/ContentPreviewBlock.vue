<template>
    <div class="video-preview-block" :class="$store.state.theme">
        <div class="content-block"></div>
        <div class="content-info">
            <div class="content-description">
                <div class="content-title">{{ name }}</div>
                <div class="content-autor">{{ author }}</div>
            </div>
            <div class="content-params">
                <div class="views">121 543 просмотров</div>
                <div class="time">15:00</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'content-preview-block',
    props: ['name', 'author'],
}
</script>

<style scoped>
.video-preview-block {
    display: flex;
    flex: 0 0 100px;
    width: auto;
    margin: 10px;
    padding: 10px;
    border-radius: 15px;
    transition: background .2s linear;
}

.video-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.video-preview-block.light-theme {
    color: #000;
}

.video-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.video-preview-block.light-theme:hover {
    background: rgba(0, 0, 0, 0.1);
}

.content-block {
    width: 160px;
    height: 90px;
    margin-right: 15px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 10px;
}

.video-preview-block.light-theme .content-block {
    border: 1px #0000002f solid;
    background: rgba(0, 0, 0, 0.2);
}

.video-preview-block.light-theme .content-title {
    color: #965000;
}

.content-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.content-description {
    margin-top: 5px;
}

.content-title {
    font-size: 17px;
    color: #ffbf6c;
}

.content-autor {
    display: flex;
    font-size: 15px;
}

.content-params {
    display: flex;
    justify-content: space-between;
    margin-bottom: 5px;
}

.views {
    font-size: 13px;
    color: #969696;
}

.time {
    display: flex;
    font-size: 13px;
}
</style>