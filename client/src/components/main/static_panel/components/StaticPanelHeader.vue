<template>
    <div class="static-panel-header">
        <slot></slot>
        <static-panel-search-bar></static-panel-search-bar>
    </div>
</template>

<script>
export default {
    name: 'static-panel-header',
}
</script>

<style scoped>
.static-panel-header {
    flex: 0 0 43px;
    display: flex;
    margin-bottom: 10px;
}
</style>