<template>
    <div class="content-block">
        <div class="content-block-inner">
            <div class="info" :class="$store.state.theme">
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
    margin-bottom: 10px;
}

.content-block-inner {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.info {
    text-align: center;
    position: relative;
    flex: 1;
    height: calc(100% - 45px);
    padding: 20px 30px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.info.light-theme {
    color: #000000;
    border: 1px #0000007c solid;
    background: #ebebeb;
}


@media (max-width: 500px) {
    .info {
        padding: 20px 10px;
    }
}
</style>