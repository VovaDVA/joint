<template>
    <feed-block-template>
        <div class="feed-preview"></div>
        <div class="feed-block-inner">
            <div class="feed-info">
                <div class="feed-title">Суперинтересное мегавидео</div>
            </div>
            <div class="feed-stats">
                <div class="feed-stats-container">
                    <content-stats-button icon-name="eye">1.4k</content-stats-button>
                    <content-stats-button icon-name="heart">15k</content-stats-button>
                    <content-stats-button icon-name="comment">234</content-stats-button>
                    <content-stats-button icon-name="share">93</content-stats-button>
                </div>
                <div class="feed-stats-container cost">
                    <content-stats-button icon-name="plus">Бесплатно</content-stats-button>
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
    border-radius: 20px;
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
}

.feed-stats {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.feed-stats-container {
    display: flex;
    justify-content: space-between;
    gap: 10px;
}

.feed-stats-container * {
    width: 20%;
    min-width: fit-content;
    margin-right: 0;
    margin-bottom: 10px;
}

.feed-stats-container.cost * {
    width: 100%;
    margin-bottom: 0;
}
</style>