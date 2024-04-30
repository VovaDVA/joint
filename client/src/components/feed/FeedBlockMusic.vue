<template>
    <div class="music-block" :class="$store.state.theme">
        <div class="album">
            <icon-button iconName="play"></icon-button>
        </div>
        <div class="feed-info">
            <div class="music-info">
                <div class="title">Песня</div>
                <div class="author">Владимир Двойнишников</div>
                <div class="time">5:02</div>
            </div>
            <div class="money">
                <div class="cost">
                    <content-stats-button iconName="ruble-sign">15</content-stats-button>
                </div>
            </div>

            <!-- <div class="music-controls">
                <div class="music-timer">1:25</div>
                <div class="player-bar"></div>
                <div class="music-timer">1:25</div>
                <icon-button icon-name="play"></icon-button>
            </div> -->
        </div>
        <!-- <div class="music-buttons">
                <div class="feed-stats left">
                    <content-stats-button icon-name="heart">15k</content-stats-button>
                    <content-stats-button icon-name="share">93</content-stats-button>
                </div>
                <div class="feed-stats">
                    <content-stats-button icon-name="plus">Бесплатно</content-stats-button>
                </div>
            </div> -->
    </div>
</template>

<script>
export default {
    name: 'feed-block-music',
}
</script>

<style scoped>
.music-block {
    display: flex;
    gap: 10px;
    height: fit-content;
    padding: 10px;
    height: fit-content;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.music-block.light-theme {
    border: 1px #0000007c solid;
    background: rgba(255, 255, 255, 0.5);
    color: #000;
}

.album {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 60px;
    width: 60px;
    border-radius: 12px;
    background: rgba(0, 0, 0, 0.5);
    border: 1px #ffffff4f solid;
}
.music-block.light-theme .album {
    border: 1px #0000004f solid;
    background: #fff;
}

.feed-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex: 1;
}

.music-info {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.title {
    font-size: 15px;
    color: #ffbf6c;
}
.music-block.light-theme .title {
    color: #af5d00;
}

.author {
    font-size: 13px;
    text-align: left;
}

.time {
    font-size: 15px;
    color: #ffffff7c;
}
.music-block.light-theme .time {
    color: #0000007c;
}

.money {
    display: flex;
    align-items: right;
    flex-direction: column;
}

.cost {
    display: flex;
    align-items: center;
    font-size: 17px;
    /* color: #d9ff00; */
}

/* .music-buttons {
    display: flex;
    justify-content: space-between;
} */

/* .feed-stats {
    display: flex;
    justify-content: flex-end;
}

.feed-stats * {
    margin-left: 10px;
}

.feed-stats.left * {
    margin-right: 10px;
    margin-left: 0;
} */

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

/* .player-bar {
    margin: 0 20px 0 10px;
    flex: 1;
    height: 3px;
    border-radius: 2px;
    background-color: rgba(255, 255, 255, 0.555);
} */
</style>