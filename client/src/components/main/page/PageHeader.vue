<template>
    <div class="header">
        <div class="header_inner">

            <div class="nav" id="nav">
                <router-link to="/" class="logo"></router-link>
                <router-link to="/about" class="nav_item">О нас</router-link>
                <router-link to="/news" class="nav_item">Новости</router-link>
                <router-link to="/news" class="nav_item">Сервисы</router-link>
            </div>

            <div class="user_info">
                <div class="user_top">
                    <div class="user_icons">
                        <router-link to="/profile-settings">
                            <icon-button icon-name="gear"></icon-button>
                        </router-link>
                        <router-link to="/profile-settings">
                            <icon-button icon-name="chart-simple"></icon-button>
                        </router-link>
                        <router-link to="/profile-settings">
                            <icon-button icon-name="bell"></icon-button>
                        </router-link>
                        <router-link to="/profile-settings">
                            <icon-button icon-name="right-from-bracket"></icon-button>
                        </router-link>
                    </div>

                    <router-link to="/" class="username">{{ user.username ? user.username : "FirstName LastName"
                        }}</router-link>
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
    margin-right: 20px;
    text-align: right;
}

.user_icons {
    display: flex;
    justify-content: right;
}

.profile_photo {
    width: 40px;
    height: 40px;

    margin-top: 15px;

    background-color: #555555;

    border: 1px #FFFFFF solid;
    border-radius: 50%;
}

.username {
    margin-right: 10px;
    margin-bottom: 7px;
    line-height: 20px;
    color: #ffffff;
    text-transform: none;
    transition: color .2s linear;
}

.username:hover {
    color: #6dff72;
    transition: color .2s linear;
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

    /* filter: drop-shadow(0 0 0 #000000) brightness(150%); */
    transition: filter .2s linear;
}

.logo:hover {
    filter: brightness(150%);
    transition: filter .2s linear;
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

a {
    color: white;
}
</style>