<template>
    <div class="header">
        <div class="header_inner">

            <div class="nav" id="nav">
                <router-link to="/" class="logo"></router-link>
                <router-link to="/news" class="nav-item" :class="{ selected: selectedTab === 0 }" @click="selectTab(0)">
                    <div>?</div>
                    <div class="tab-circle"></div>
                </router-link>
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
                        <router-link to="/profile-settings">
                            <icon-button icon-name="gear"></icon-button>
                        </router-link>
                        <router-link to="/stats">
                            <icon-button icon-name="chart-simple"></icon-button>
                        </router-link>
                        <!-- <router-link to="/profile-settings">
                            <icon-button icon-name="bell"></icon-button>
                        </router-link> -->
                        <icon-button icon-name="right-from-bracket" @click="logoutUser()"></icon-button>
                    </div>

                    <router-link to="/" class="username">{{ user.firstName ? (user.firstName + ' ' + user.lastName) : '-'}}</router-link>
                </div>

                <div class="profile_photo" style="background: #555555 center no-repeat; background-size: cover">
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { deleteSession } from '../../../modules/auth';

export default {
    name: 'page-header',
    data() {
        return {
            user: {
                firstName: '',
                lastName: '',
            },
            selectedTab: null,
        }
    },
    created() {
        const userData = localStorage.getItem('user');
        if (userData) {
            this.user = JSON.parse(userData);
            console.log(this.user);
        }
    },
    methods: {
        selectTab(index) {
            this.selectedTab = index;
        },
        logoutUser() {
            deleteSession();
        }
    }
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
    /* line-height: 70px; */

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
    align-items: center;
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
    height: 70px;

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
    /* margin-top: -5px; */
    color: #ffbf6c;
    transition: color .2s linear;
}

.nav-item.selected {
    /* margin-top: -10px; */
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
</style>