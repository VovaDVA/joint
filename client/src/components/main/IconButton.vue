<template>
    <div class="icon-button change_banner">
        <span><slot></slot></span>
        <!-- <i class="fas fa-pen"></i> -->
    </div>
</template>

<script>
export default {
    name: 'icon-button',
}
</script>

<style scoped>
.icon-button {
    width: auto;
    padding: 10px;
    font-size: 15px;
    line-height: 15px;
    border-radius: 30px;
    
    background-color: rgba(0, 0, 0, 0.5);
    user-select: none;
    transition: color, background .3s linear;
}

.icon-button:hover {
    color: black;
    background-color: white;
    transition: color, background .3s linear;
}
</style>