<template>
    <div class="music-block" :class="$store.state.theme">
        <div class="album">
            <icon-button iconName="play"></icon-button>
        </div>
        <div class="feed-info">
            <div class="music-info">
                <div class="title">Песня</div>
                <div class="author">Владимир Двойнишников</div>
                <div class="stats">
                    <div class="stats-item">
                        <single-icon iconName="headphones"></single-icon>
                        <div class="amount">154</div>
                    </div>
                    <div class="stats-item">
                        <single-icon iconName="plus"></single-icon>
                        <div class="amount">154</div>
                    </div>
                </div>
            </div>
            <div class="money">
                <div class="time">5:02</div>
                <div class="cost">
                    <content-stats-button iconName="ruble-sign">15</content-stats-button>
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
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    height: 100%;
}

.cost {
    display: flex;
    align-items: center;
    font-size: 17px;
}

.stats {
    display: flex;
    gap: 10px;
    font-size: 13px;
    color: #ffffff7c;
}

.stats * {
    font-size: 13px;
}

.music-block.light-theme .stats {
    color: #0000007c;
}

.stats-item {
    display: flex;
    align-items: center;
    gap: 5px;
}
</style>