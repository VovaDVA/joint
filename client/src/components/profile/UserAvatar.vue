<template>
    <div class="user-info" :class="$store.state.theme">
        <div class="avatar"></div>
        <div class="user-info-text">
            <div class="username">{{ getName() }}</div>
        </div>
    </div>
</template>

<script>
import { getUserName } from '@/modules/auth';

export default {
    name: 'user-avatar',
    methods: {
        getName() {
            return getUserName();
        }
    }
}
</script>

<style scoped>
.user-info {
    margin-left: -20px;
    display: flex;
}

.avatar {
    width: 5.5vw;
    height: 5.5vw;

    margin-top: -2.75vw;
    margin-left: 2.2vw;

    border: 1px #ffffff7c solid;
    border-radius: 50%;

    background: #555555 center no-repeat;
    background-size: cover;
    transition: background .3s linear;
}

.user-info.light-theme {
    color: #000;
}

.user-info.light-theme .avatar {
    border: 1px #0000007c solid;
    background: #a3a3a3 center no-repeat;
    background-size: cover;
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


@media (max-width: 1300px) {
    .avatar {
        width: 80px;
        height: 80px;
        margin-top: -40px;
        margin-left: 50px;
    }
}


@media (max-width: 800px) {
    .avatar {
        margin: auto;
        margin-top: -40px;
    }

    .user-info {
        margin: 0;
        text-align: center;
        flex-direction: column;
    }
}
</style>