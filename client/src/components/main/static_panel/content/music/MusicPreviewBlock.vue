<template>
    <div class="music-preview-block" :class="$store.state.theme">
        <div class="music-cover"></div>
        <div class="music-info">
            <div class="music-header">
                <div class="music-title">{{ name }}</div>
                <span>&nbsp;-&nbsp;</span>
                <div class="music-autor">{{ author }}</div>
            </div>
            <div class="buttons">
                <div class="music-block">
                    <icon-button icon-name="play"></icon-button>
                    <div class="music-scale"></div>
                </div>
                <div class="time">3:05</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'music-preview-block',
    props: ['name', 'author'],
}
</script>

<style scoped>
.music-preview-block {
    display: flex;
    flex: 0 0 100px;
    width: auto;
    margin: 10px;
    padding: 10px;
    border-radius: 15px;
    transition: background .2s linear;
}
.music-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.music-preview-block.light-theme {
    color: #000;
}

.music-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.music-preview-block.light-theme:hover {
    background: rgba(0, 0, 0, 0.1);
}

.music-cover {
    width: 60px;
    flex: 0 0 60px;
    height: 60px;
    margin-right: 20px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 10px;
}

.music-preview-block.light-theme .music-cover {
    border: 1px #0000002f solid;
    background: rgba(0, 0, 0, 0.2);
}

.music-preview-block.light-theme .music-title {
    color: #965000;
}

.music-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}
.music-header {
    display: flex;
    justify-content: flex-start;
}

.music-title {
    font-size: 15px;
    color: #ffbf6c;
}
.music-autor, .time {
    font-size: 15px;
}
.buttons {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.buttons * {
    margin-left: 0px;
}

.music-block {
    margin-right: 20px;
    flex: 1;
    display: flex;
    justify-content: start;
    align-items: center;
}
.icon-button {
    padding: 0;
    margin-right: 10px;
}
.music-scale {
    flex: 1;
    height: 4px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 2px;
    margin-left: 10px;
}

.music-preview-block.light-theme .music-scale {
    background: rgba(0, 0, 0, 0.2);
}

</style>