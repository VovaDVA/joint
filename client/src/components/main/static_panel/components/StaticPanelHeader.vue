<template>
    <div class="static-panel-header">
        <slot></slot>
    </div>
</template>

<script>
export default {
    name: 'static-panel-header',
}
</script>

<style scoped>
.static-panel-header {
    flex: 0 0 45px;
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
}
</style>