<template>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Montserrat:wght@500&display=swap
    " rel="stylesheet">

    <page-header v-if="!isLoginPage && !isRegisterPage"></page-header>

    <div class="page">
        <div class="section">
            <div class="container">
                <router-view></router-view>
            </div>
        </div>
        <side-chat></side-chat>
    </div>

    <div class="intro">
        <div class="intro_fade"></div>
    </div>
</template>

<script>
import { deleteSession } from './modules/auth';

export default {
    name: 'App',
    mounted() {
        const userData = localStorage.getItem('user');
        if (userData) {
            this.user = JSON.parse(userData);
        }
    },
    computed: {
        isLoginPage() {
            return this.$route.path === '/login'; // Проверка текущего маршрута на страницу входа (login)
        },
        isRegisterPage() {
            return this.$route.path === '/register'; // Проверка текущего маршрута на страницу регистрации (register)
        }
    },
    methods: {
        logoutUser() {
            deleteSession();
        }
    }
}
</script>

<style scoped>
* {
    box-sizing: content-box;
}

.chart {
    margin-bottom: 30px;
}

.page {
    position: relative;
    display: flex;
    height: auto;
    min-height: 200px;
    margin-bottom: 500px;
    padding-left: 10px;
}

.container {
    /* display: flex; */
    flex: 1;
    /* justify-content: center; */
    /* max-width: 1200px; */
    /* padding: 0 20px; */
    margin: 0 auto;
    font-family: 'Montserrat', sans-serif;
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

/* Section */

.section {
    display: flex;
    margin-top: 10px;
    /* flex: 0.7; */
    width: calc(100vw - 30vw - 90px);
    /* overflow-y: auto; */
    /* margin-top: 200px;
    padding: 50px 0 100px;
    line-height: 30px;
    text-align: justify; */
}

.section_header {
    font-size: 25px;
    text-transform: uppercase;
    text-align: center;
}

.section_header:after {
    content: "";
    display: block;
    margin: 20px auto 30px;

    width: 400px;
    height: 1px;

    background-image: radial-gradient(#ffffff -30%, transparent);
}

.container p {
    max-width: 750px;
    margin: 15px auto;
}
</style>