<template>
    <div class='profile-content'>
        <router-view></router-view>
        <div class="profile-inner"></div>
    </div>
</template>

<script>
export default {
    name: 'profile-content',
}
</script>

<style scoped>
.profile-content {
    width: 100%;
    margin-top: 70px;
    margin-bottom: 20px;
}

.profile-inner {
    display: flex;
    justify-content: space-between;
}

.profile-about {
    padding: 20px;
    min-height: 70px;
    background: rgba(0, 0, 0, 0.5);
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    margin-bottom: 20px;
}

.profile-info {
    flex: 0.5;
    margin-right: 10px;
    padding: 20px;
    height: 300px;
    min-height: 200px;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 20px;
}

.profile-media {
    flex: 1;
    height: fit-content;
    min-height: 500px;
    padding: 10px 20px 50px;

    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);

    position: relative;
}

.menu_inner {
    margin-top: 10px;
    /* height: 40px; */
    display: flex;
    justify-content: flex-start;
    /* font-size: 13px; */
}

.menu_inner .stroke-icon-button {
    margin-right: 20px;
    color: white;
}

.menu_item {
    padding: 10px 30px;

    color: #fff;
    text-transform: uppercase;
    text-decoration: none;

    transition: color .3s linear;

    border-bottom: 1px #ffffff7c solid;
}

.menu_item:hover {
    border-bottom: 1px #ffe030 solid;
    color: #ffe030;
    transition: color .3s linear;
}


@media (max-width: 1000px) {
    .profile-content {
        margin-top: 10px;
    }
}
</style>