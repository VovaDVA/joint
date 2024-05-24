<template>
    <div class="avatar-container">
        <div class="avatar">
            <img :src="photo || basePhoto" :onerror="onError">
        </div>
        <slot></slot>
        <div class="online-mark" :class="[{ active: isOnline || online }, $store.state.theme]"></div>
    </div>
</template>

<script>
// import { getUserAvatar } from '@/modules/auth';

export default {
    name: 'user-avatar',
    props: ['photo', 'online'],
    data() {
        return {
            basePhoto: require('@/assets/user.png'),
            isOnline: false,
        }
    },
    onError() {
        this.basePhoto = require('@/assets/user.png');
    }
}
</script>

<style scoped>
.avatar-container {
    position: relative;
    height: 100%;
    aspect-ratio: 1;
}

.avatar {
    position: absolute;
    width: 100%;
    height: 100%;
}

.avatar {
    background: #555555;
    border-radius: 50%;
    overflow: hidden;
}

img {
    width: 100%;
    height: 100%;
}

.online-mark.active {
    visibility: visible;
    width: 15%;
    height: 15%;
    /* border: 2px #818181 solid; */
    background: rgb(94, 255, 0);
    border-radius: 50%;
}

.online-mark {
    visibility: hidden;
    position: absolute;
    top: 78%;
    left: 78%;
}
</style>