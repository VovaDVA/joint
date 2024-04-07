<template>
    <div class="video-preview-block">
        <div class="content-block"></div>
        <div class="content-info">
            <div class="content-description">
                <div class="content-title">Перфекционизм - зло!</div>
                <div class="content-autor">Nastya</div>
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

.vodeo-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.content-block{
    width: 160px;
    height: 90px;
    margin-right: 15px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 10px;
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
    color: rgb(255, 164, 54);
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