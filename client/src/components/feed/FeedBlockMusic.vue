<template>
    <div class="feed-block">
        <div class="feed-preview"></div>
        <div class="feed-block-inner">
            <div class="feed-info">
                <div class="music-info">
                    <div class="music-title">Самая лучшая музыка всех времен</div>
                    <div class="music-author">Владимир Двойнишников</div>
                </div>
                <div class="music-controls">
                    <div class="music-timer">1:25</div>
                    <div class="player-bar"></div>
                    <div class="music-timer">1:25</div>
                    <icon-button icon-name="play"></icon-button>
                </div>
            </div>
            <div class="music-buttons">
                <div class="feed-stats left">
                    <content-stats-button icon-name="heart">15k</content-stats-button>
                    <content-stats-button icon-name="share">93</content-stats-button>
                </div>
                <div class="feed-stats">
                    <content-stats-button icon-name="plus">Бесплатно</content-stats-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'feed-block-music',
}
</script>

<style scoped>
.feed-block {
    display: flex;
    width: 100%;
    height: 100px;
    margin-bottom: 20px;
}

.feed-preview {
    flex: 0 0 100px;
    margin-right: 10px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-block-inner {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.feed-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    flex: 1;
    height: calc(100% - 45px);
    margin-bottom: 10px;
    padding: 5px 20px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.music-info {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}

.music-title {
    font-size: 15px;
    color: #ffbf6c;
}

.music-author {
    font-size: 13px;
    text-align: left;
}

.music-timer {
    text-align: right;
    margin-right: 10px;
}

.feed-cost {
    font-size: 13px;
    text-align: right;
    color: #6cff80;
}

.music-buttons {
    display: flex;
    justify-content: space-between;
}

.feed-stats {
    display: flex;
    justify-content: flex-end;
}

.feed-stats * {
    margin-left: 10px;
}

.feed-stats.left * {
    margin-right: 10px;
    margin-left: 0;
}

.music-controls {
    display: flex;
    flex: 0 0 50%;
    height: 35px;
    /* padding: 0 20px 0 5px; */
    font-family: 'Montserrat';
    font-size: 15px;
    line-height: 15px;
    border-radius: 30px;

    align-items: center;

    /* background-color: rgba(0, 0, 0, 0.5);
    user-select: none;
    transition: color, background .3s linear; */
}

.player-bar {
    margin: 0 20px 0 10px;
    flex: 1;
    height: 3px;
    border-radius: 2px;
    background-color: rgba(255, 255, 255, 0.555);
}
</style>