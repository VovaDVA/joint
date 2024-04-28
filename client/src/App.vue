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
        <side-chat v-if="!isLoginPage && !isRegisterPage"></side-chat>
    </div>

    <modal-change-banner></modal-change-banner>
    <modal-change-avatar></modal-change-avatar>

    <div class="intro">
        <div class="intro_fade"></div>
    </div>
</template>

<script>
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
    margin-bottom: 200px;
    padding-left: 55px;
}

.container {
    flex: 1;
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

/* Section */

.section {
    display: flex;
    margin-top: 10px;
    /* flex: 0.7; */
    width: calc(100vw - 30vw - 135px);
    z-index: 10;
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