<template>
    <div class="header" :class="$store.state.theme">
        <div class="header_inner">

            <div class="right-container">
                <router-link to="/" class="logo"></router-link>
                <button class="nav-toggle" :class="{ 'active': isActive }" type="button" @click="toggleNav()">
                    <span class="nav-toggle_item">Menu</span>
                </button>
            </div>

            <div class="nav" :class="{ 'active': isActive }">
                <router-link to="/about" class="nav-item" :class="{ selected: selectedTab === 1 }"
                    @click="selectTab(1)">
                    <div>О нас</div>
                    <div class="tab-circle"></div>
                </router-link>
                <router-link to="/news" class="nav-item" :class="{ selected: selectedTab === 2 }" @click="selectTab(2)">
                    Новости
                    <div class="tab-circle"></div>
                </router-link>
                <router-link to="/news" class="nav-item" :class="{ selected: selectedTab === 3 }" @click="selectTab(3)">
                    Сервисы
                    <div class="tab-circle"></div>
                </router-link>
                <router-link to="/news" class="nav-item" :class="{ selected: selectedTab === 4 }" @click="selectTab(4)">
                    Сотрудничество
                    <div class="tab-circle"></div>
                </router-link>
                <router-link to="/news" class="nav-item" :class="{ selected: selectedTab === 5 }" @click="selectTab(5)">
                    Реклама
                    <div class="tab-circle"></div>
                </router-link>
            </div>

            <div class="user_info">
                <div class="user_top">
                    <div class="user_icons">
                        <icon-button icon-name="sun" @click="changeTheme()"></icon-button>
                        <router-link to="/profile-settings" @click="changePage()">
                            <icon-button icon-name="gear"></icon-button>
                        </router-link>
                        <router-link to="/stats" @click="changePage()">
                            <icon-button icon-name="chart-simple"></icon-button>
                        </router-link>
                        <icon-button icon-name="right-from-bracket" @click="logoutUser()"></icon-button>
                    </div>

                    <router-link to="/" class="username">{{ getName() }}</router-link>
                </div>

                <router-link to="/" @click="changePage()">
                    <div class="profile_photo" style="background: #555555 center no-repeat; background-size: cover">
                    </div>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
import { deleteSession, getUserName } from '../../../modules/auth';

export default {
    name: 'page-header',
    data() {
        return {
            selectedTab: null,
            isActive: false,
        }
    },
    methods: {
        changePage() {
            this.$store.commit('hideStaticPanel');
        },
        selectTab(index) {
            this.selectedTab = index;
            this.toggleNav();
            this.changePage();
        },
        logoutUser() {
            deleteSession();
        },
        toggleNav() {
            this.isActive = !this.isActive;
        },
        changeTheme() {
            this.$store.commit('changeTheme');
        },
        getName() {
            return getUserName();
        }
    }
}
</script>

<style scoped>
.header {
    width: auto;
    height: 70px;
    padding: 0 20px 0 0;

    font-family: 'Montserrat';
    font-size: 15px;
    text-transform: uppercase;

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

.header.light-theme {
    background: #fff;
    color: #000;
    border-image: radial-gradient(#000000 60%, transparent);
    border-image-slice: 1;
}

.header.light-theme .nav {
    background: #fff;
}

.header.light-theme .nav-item {
    color: #000;
}

.header.light-theme .nav-item:hover {
    color: #965000;
    transition: color .2s linear;
}

.header.light-theme .nav-item.selected {
    color: #965000;
}

.header.light-theme .tab-circle {
    background: #965000;
}

.header.light-theme .nav-toggle_item {
    background: #000;
}

.header.light-theme .nav-toggle_item::after {
    background: #000;
}

.header.light-theme .nav-toggle_item::before {
    background: #000;
}

.header.light-theme .username {
    color: #000;
}

.header.light-theme .profile_photo {
    background: #b9b9b9 !important;
    border: 1px #000000 solid;
}

.header_inner {
    width: 100%;
    height: inherit;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.right-container {
    display: flex;
    gap: 10px;
    margin-left: 10px;
    z-index: 101;
}

.user_info {
    display: flex;
    align-items: center;
    z-index: 101;
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
    color: #ffbf6c;
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
    width: 40px;
    height: 40px;

    background: url(../../../../public/icon.png) center no-repeat;
    background-size: contain;
    transition: filter .2s linear;
}

.logo:hover {
    filter: brightness(150%);
    transition: filter .2s linear;
}

.nav,
.nav.active {
    display: flex;
    z-index: 100;
}

.nav-item {
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 0 15px;

    color: #FFFFFF;
    text-decoration: none;
    text-align: center;

    transition: color .2s linear;
    user-select: none;
}

.nav-item:hover {
    color: #ffbf6c;
    transition: color .2s linear;
}

.nav-item.selected {
    color: #ffbf6c;
}

.nav-item.selected>.tab-circle {
    display: block;
}

.tab-circle {
    margin: 0 auto;
    display: none;
    width: 10px;
    height: 10px;
    border-radius: 10px;
    margin-top: 10px;
    background: #ffbf6c;
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


.nav-toggle {
    width: 30px;
    padding: 10px 0;
    display: none;

    font-size: 0;
    color: transparent;

    border: 0;
    background: none;
    cursor: pointer;
    outline: none !important;
    user-select: none !important;
}

.nav-toggle.active .nav-toggle_item {
    background: none;
}

.nav-toggle_item {
    position: relative;
    display: block;
    width: 100%;
    height: 3px;

    background-color: #fff;
    transition: background .2s linear;
}

.nav-toggle_item:before,
.nav-toggle_item:after {
    content: "";
    width: 100%;
    height: 3px;

    background-color: #fff;

    position: absolute;
    left: 0;
    z-index: 1;

    transition: transform .2s linear;
}

.nav-toggle_item:before {
    top: -10px;
}

.nav-toggle_item:after {
    bottom: -10px;
}

.nav-toggle.active .nav-toggle_item:before {
    transform-origin: left top;
    transform: translateX(5px)rotate(45deg);
}

.nav-toggle.active .nav-toggle_item:after {
    transform-origin: left bottom;
    transform: translateX(5px)rotate(-45deg);
}


@media (max-width: 1000px) {
    .nav {
        display: flex;
        /* opacity: 0; */
        visibility: hidden;
        position: absolute;
        flex-direction: column;
        width: 100%;
        top: -150px;
        left: 0;
        right: 0;
        padding: 10px;

        background: #000000;
        border-bottom: 1px solid #ffffff7c;
        border-radius: 0 0 25px 25px;
        transition: top .2s, visibility .1s ease-out;
    }

    .header.light-theme .nav {
        border-bottom: 1px solid #0000007c;
    }

    .nav.active {
        visibility: visible;
        top: 70px;
        transition: visibility, top .2s ease-out;
    }

    .nav-toggle {
        display: block;
    }

    .nav-item {
        width: 100%;
        padding: 10px;
    }

    .username {
        display: none;
    }

    .user_top {
        margin-right: 10px;
    }
}
</style>