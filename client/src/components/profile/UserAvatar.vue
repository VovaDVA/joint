<template>
    <div class="user-info">
        <div class="avatar"></div>
        <div class="user-info-text">
            <div class="username">Username</div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'user-avatar',
}
</script>

<style scoped>
.user-info {
    margin-left: -20px;
    display: flex;
}

.avatar {
    width: 100px;
    height: 100px;

    margin-top: -50px;
    margin-left: 40px;

    border: 1px #ffffff7c solid;
    border-radius: 50%;

    background: #555555 center no-repeat;
    background-size: cover;
    transition: background .3s linear;
}

.avatar:hover {
    background-color: rgba(0, 0, 0, 1);
    transition: background .3s linear;
}

.user-info-text {
    display: flex;
    flex-direction: column;
    margin-top: 10px;
    margin-left: 10px;
}
</style>