<template>
    <div class="header">
        <div class="header_inner">

            <div class="nav" id="nav">
                <a class="logo" href="index.html" target="_self"></a>
                <router-link to="/about" class="nav_item">О нас</router-link>
                <router-link to="/news" class="nav_item">Новости</router-link>
            </div>

            <div class="user_info">
                <div class="user_top">
                    <div class="user_icons">
                        <img class="icon logout" src="../../../assets/icons/logout.png" alt="" v-on:click="logoutUser">
                        <img class="icon" src="../../../assets/icons/notifications.png" alt="">
                        <img class="icon" src="../../../assets/icons/rating.png" alt="">
                    </div>

                    <router-link to="/" class="user_name">{{ user.username ? user.username : "-" }}</router-link>
                </div>

                <div class="profile_photo" style="background: #555555 center no-repeat; background-size: cover">
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'page-header',
    data() {
        return {
            user: {
                username: '',
            },
        }
    },
}
</script>

<style scoped>
.header {
    width: auto;
    height: 70px;
    padding: 0 20px;

    font-family: 'Montserrat';
    font-size: 15px;
    text-transform: uppercase;
    line-height: 70px;

    border-bottom: 1px solid transparent;
    border-image: radial-gradient(#ffffff 60%, transparent);
    border-image-slice: 1;

    background-color: #000000;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 100;
}

.header_inner {
    width: 100%;
    height: inherit;
    display: flex;
    justify-content: space-between;
}

.user_info {
    display: flex;
}

.user_top {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-left: 20px;
}

.user_icons {
    display: flex;
    justify-content: left;
    margin: 5px 0;
}

.user_icons .icon {
    margin-right: 20px;
}

.icon {
    -webkit-filter: sepia(1) saturate(0);
    filter: sepia(1) saturate(0);
    width: 15px;
    height: 15px;
}

.icon:hover {
    -webkit-filter: sepia(1) hue-rotate(70deg) saturate(100);
    filter: sepia(1) hue-rotate(70deg) saturate(100);
}

.logout_btn {
    line-height: 0;
}

.profile_photo {
    width: 40px;
    height: 40px;

    margin-top: 15px;

    background-color: #555555;

    border: 1px #FFFFFF solid;
    border-radius: 50%;
}

.user_name {
    margin-top: 7px;
    line-height: 20px;
    color: #ffffff;
    text-transform: none;
}

.user_name:hover {
    color: #6dff72;
}

.button {
    margin: 0 15px;
    padding: 5px 30px;

    border: 1px #ffffff solid;
    border-radius: 50px;

    font-size: 13px;
    text-transform: uppercase;
    text-decoration: none;
    color: #dff4ff;

    transition: color, background .3s linear;
}

.button.profile {
    margin-right: 130px;
}

.button:hover {
    background-color: #ffffff;
    color: #000000;

    transition: color, background .3s linear;
}

.logo {
    height: 100%;
    width: 70px;

    background: url(../../../assets/logo.png) center no-repeat;
    background-size: contain;

    filter: drop-shadow(0 0 0 #000000) brightness(150%);
    transition: filter .3s linear;
}

.logo:hover {
    filter: drop-shadow(0 0 30px #003971) brightness(200%);
    transition: filter .3s linear;
}

.nav {
    display: flex;
}

.nav_item {
    padding: 0 15px;

    /* font-weight: 700; */
    color: #FFFFFF;
    text-decoration: none;

    transition: background .3s linear;
}

.nav_item:hover {
    background-color: #00366c;
    transition: background .3s linear;
}

.nav_item.active {
    background-color: #00366c;
}

.header_fade {
    width: 100%;
    height: 300px;
    background-image: linear-gradient(#00111e 70px, transparent);

    position: fixed;
    top: 0;
    left: 0;
    z-index: 90;
}
</style>