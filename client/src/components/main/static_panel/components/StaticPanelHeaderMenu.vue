<template>
    <div class="menu" :class="$store.state.theme">
        <slot></slot>
    </div>
</template>

<script>
export default {
    name: 'static-panel-header-menu',
}
</script>

<style scoped>
.menu {
    display: flex;
    justify-content: left;
    align-items: center;
    margin-bottom: 10px;
    padding: 10px;
    height: 45px;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.5);
    border: 1px #ffffff4f solid;
    border-radius: 50px;
}

.menu.light-theme {
    background-color: #fff;
    border: 1px #0000004f solid;
}
</style>