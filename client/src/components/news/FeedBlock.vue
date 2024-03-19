<template>
    <div class="feed-block">
        <div class="feed-preview"></div>
        <div class="feed-info"></div>
    </div>
</template>

<script>
export default {
    name: 'feed-block',
}
</script>

<style scoped>
.feed-block {
    display: flex;
    width: 100%;
    height: 200px;
}

.feed-preview {
    flex: 0 0 300px;
    margin-bottom: 10px;
    margin-right: 10px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-info {
    flex: 1;
    margin-bottom: 10px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}
</style>