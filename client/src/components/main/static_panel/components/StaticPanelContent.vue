<template>
    <div class="static-panel-content"><slot></slot></div>
</template>

<script>
export default {
    name: 'static-panel-content',
}
</script>

<style scoped>
.static-panel-content {
    flex: 1;
    /* padding: 0 10px; */

    border: 1px #ffffff4f solid;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.5);

    overflow-y: scroll;
    scrollbar-color: #ffffff;
}

.static-panel-content::-webkit-scrollbar {
    width: 0;
}
</style>