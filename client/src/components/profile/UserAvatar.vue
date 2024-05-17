<template>
    <div class="user-info" :class="$store.state.theme">
        <div class="avatar-container" @click="change">
            <div class="avatar">
                <img :src="url" alt="">
            </div>
            <div class="cover">
                <single-icon iconName="camera"></single-icon>
            </div>
        </div>
        <div class="user-info-text">
            <div class="username">{{ getName() }}</div>
        </div>
    </div>
</template>

<script>
import { getUserAvatar, getUserName } from '@/modules/auth';

export default {
    name: 'user-avatar',
    data() {
        return {
            url: null,
            // url: 'https://images.pexels.com/photos/1254140/pexels-photo-1254140.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940',
        }
    },
    mounted() {
        this.url = getUserAvatar();

        this.emitter.on('confirm-change-avatar', () => {
            this.url = getUserAvatar();
        });
    },
    methods: {
        getName() {
            return getUserName();
        },
        change() {
            this.emitter.emit('request-change-avatar');
        }
    }
}
</script>

<style scoped>
.user-info {
    display: flex;
}

.avatar-container {
    position: relative;
    width: 5.5vw;
    height: 5.5vw;

    margin-top: -2.75vw;
    margin-left: 2.2vw;

    border: 1px #ffffff7c solid;
    border-radius: 50%;
    overflow: hidden;
    background: #555555;
}

.avatar,
.cover {
    position: absolute;
    width: 100%;
    height: 100%;
}

.cover {
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    background: #0000007c;
    transition: opacity .2s linear;
}

.avatar-container:hover>.cover {
    opacity: 1;
    cursor: pointer;
    transition: opacity .2s linear;
}

img {
    width: 100%;
    height: 100%;
}

.user-info.light-theme {
    color: #000;
}

.user-info.light-theme .avatar {
    border: 1px #0000007c solid;
    background: #a3a3a3 center no-repeat;
    background-size: cover;
}

.user-info-text {
    display: flex;
    flex-direction: column;
    margin-top: 10px;
    margin-left: 10px;
}

.icon {
    color: #ffffffa1;
    font-size: 30px;
}


@media (max-width: 1300px) {
    .avatar-container {
        width: 80px;
        height: 80px;
        margin-top: -40px;
        margin-left: 30px;
    }
}


@media (max-width: 800px) {
    .avatar-container {
        margin: auto;
        margin-top: -40px;
    }

    .user-info {
        text-align: center;
        flex-direction: column;
    }
}
</style>