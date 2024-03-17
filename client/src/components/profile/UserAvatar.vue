<template>
    <div class="avatar"></div>
</template>

<script>
export default {
    name: 'user-avatar',
}
</script>

<style scoped>
.avatar {
    width: 100px;
    height: 100px;

    margin-top: -50px;
    margin-left: 40px;

    border: 1px #FFFFFF solid;
    border-radius: 50%;

    background: #555555 center no-repeat;
    background-size: cover;
}
</style>