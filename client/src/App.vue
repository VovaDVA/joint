<template>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Montserrat:wght@500&display=swap
    " rel="stylesheet">

    <page-header v-if="!isLoginPage && !isRegisterPage"></page-header>
    <div class="page" :class="{ 'fixed': isPageScrollable() }">
        <div class="section">
            <div class="container">
                <router-view></router-view>
            </div>
        </div>
    </div>
    <static-panel v-if="!isLoginPage && !isRegisterPage"></static-panel>

    <!-- <modal-change-banner></modal-change-banner> -->
    <modal-change-avatar></modal-change-avatar>

    <div class="intro" :class="$store.state.theme">
        <div class="intro_fade"></div>
    </div>
    <modal-delete-account></modal-delete-account>
    <modal-change-password></modal-change-password>
</template>

<script>
import { getUserId } from './modules/auth';

export default {
    name: 'App',
    computed: {
        isLoginPage() {
            return this.$route.path === '/login'; // Проверка текущего маршрута на страницу входа (login)
        },
        isRegisterPage() {
            return this.$route.path === '/register'; // Проверка текущего маршрута на страницу регистрации (register)
        }
    },
    mounted() {
        const userId = getUserId();
        if (!userId) return;

        this.$store.commit('socketInit');
        this.$store.state.chatSocket.emit('userConnected', userId);

        this.$store.state.chatSocket.on('updateOnlineUsers', (onlineUsers) => {
            this.$store.state.onlineUsers = onlineUsers;
        });
    },
    methods: {
        isPageScrollable() {
            return this.$store.state.staticPanelVisible && window.innerWidth < 1300;
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
    margin-bottom: 80px;
    padding-left: 55px;
}

.page.fixed {
    position: fixed;
}

.container {
    flex: 1;
    margin: 0 auto;
    font-family: 'Montserrat', sans-serif;
}

/* Section */

.section {
    display: flex;
    margin-top: 10px;
    width: calc(100vw - 30vw - 135px);
    z-index: 10;
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


@media (max-width: 1300px) {
    .section {
        width: calc(100vw - 110px);
    }
}

@media (max-width: 1000px) {
    .page {
        padding: 0 10px;
    }

    .section {
        width: 100%;
    }
}
</style>