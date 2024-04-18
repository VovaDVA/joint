<template>
    <div class="content-block">
        <div class="content-block-inner">
            <div class="news-info">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'content-block',
}
</script>

<style scoped>
.content-block {
    display: flex;
    width: 100%;
    height: fit-content;
    margin-bottom: 20px;
}

.content-block-inner {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.news-info {
    text-align: center;
    position: relative;
    flex: 1;
    height: calc(100% - 45px);
    margin-bottom: 10px;
    padding: 20px 30px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}
</style>