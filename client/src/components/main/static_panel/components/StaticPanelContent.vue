<template>
    <div class="static-panel-content" :class="$store.state.theme"><slot></slot></div>
</template>

<script>
export default {
    name: 'static-panel-content',
}
</script>

<style scoped>
.static-panel-content {
    flex: 1;
    position: relative;

    border: 1px #ffffff4f solid;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.5);

    overflow-y: auto;
    scrollbar-color: #ffffff;
}

.static-panel-content::-webkit-scrollbar {
    width: 0;
}

.static-panel-content.light-theme {
    border: 1px #0000004f solid;
    background-color: #fff;
}
</style>