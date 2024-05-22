<template>
    <div class="user-info" :class="$store.state.theme">
        <user-avatar :photo="photo" :online="true" @click="change">
            <div class="cover">
                <single-icon iconName="camera"></single-icon>
            </div>
        </user-avatar>
        <div class="user-info-text">
            <div class="username">{{ getName() }}</div>
        </div>
    </div>
</template>

<script>
import { getUserAvatar, getUserName } from '@/modules/auth';

export default {
    name: 'user-info-banner',
    data() {
        return {
            photo: null,
        }
    },
    mounted() {
        this.photo = getUserAvatar();

        this.emitter.on('confirm-change-avatar', () => {
            this.photo = getUserAvatar();
        });
    },
    methods: {
        getName() {
            return getUserName();
        },
        change() {
            this.emitter.emit('request-change-avatar');
        },
    }
}
</script>

<style scoped>
.user-info {
    display: flex;
}

.avatar-container {
    width: 5.5vw;
    height: 5.5vw;

    margin-top: -2.75vw;
    margin-left: 2.2vw;

    /* border: 3px #2d3544 solid; */
    border: 1px #ffffff7c solid;
    border-radius: 50%;
    background: #555555;
}

.user-info.light-theme .avatar-container {
    /* border: 3px #d4d4d4 solid; */
    border: 1px #0000007c solid;
}

.cover {
    position: absolute;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    background: #0000009a;
    border-radius: 50%;
    transition: opacity .2s linear;
}

.avatar-container:hover>.cover {
    opacity: 1;
    cursor: pointer;
    transition: opacity .2s linear;
}

.user-info.light-theme {
    color: #000;
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
        border: 2px #ffffff4f solid;
    }

    .user-info {
        text-align: center;
        flex-direction: column;
    }
}
</style>