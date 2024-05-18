<template>
    <feed-block-template>
        <div class="feed-preview"></div>
        <div class="feed-block-inner">
            <div class="feed-info">
                <div class="feed-title">Суперинтересное мегавидео</div>
            </div>
            <div class="feed-stats">
                <div class="stats">
                    <div class="stats-item">
                        <single-icon iconName="eye"></single-icon>
                        <div class="amount">154</div>
                    </div>
                    <div class="stats-item">
                        <single-icon iconName="plus"></single-icon>
                        <div class="amount">154</div>
                    </div>
                </div>
                <div class="cost">
                    <content-stats-button iconName="ruble-sign">15</content-stats-button>
                </div>
            </div>
        </div>
    </feed-block-template>
</template>

<script>
export default {
    name: 'feed-block-media',
}
</script>

<style scoped>
.feed-block.light-theme .feed-title {
    color: #af5d00;
}

.feed-block.light-theme .feed-preview {
    border: 1px #0000007c solid;
    background: #ffffff;
}

.feed-block.light-theme .feed-info {
    color: #000;
}

.feed-preview {
    aspect-ratio: 16/9;
    border: 1px #ffffff4e solid;
    border-radius: 10px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-block-inner {
    flex: 1;
    display: flex;
    gap: 10px;
    flex-direction: column;
}

.feed-info {
    position: relative;
    flex: 1;
    height: calc(100% - 45px);
}

.feed-title {
    color: #ffbf6c;
    font-size: 22px;
    padding: 0 10px;
}

.feed-stats {
    display: flex;
    justify-content: space-between;
}

.stats {
    display: flex;
    gap: 10px;
    font-size: 13px;
    color: #ffffff7c;
    padding-left: 10px;
}

.stats * {
    font-size: 13px;
}

.feed-block.light-theme .stats {
    color: #0000007c;
}

.stats-item {
    display: flex;
    align-items: center;
    gap: 5px;
}
</style>