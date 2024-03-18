<template>
    <div class='profile-content'>
        <div class="profile-inner">
            <div class="profile-info"></div>
            <div class="profile-media">
                <div class="menu_inner">
                    <router-link to="/table-view">
                        <stroke-icon-button icon-name="image">Фото</stroke-icon-button>
                    </router-link>
                    <router-link to="/graphics-view">
                        <stroke-icon-button icon-name="video">Видео</stroke-icon-button>
                    </router-link>
                    <router-link to="/table-view">
                        <stroke-icon-button icon-name="music">Музыка</stroke-icon-button>
                    </router-link>
                    <router-link to="/graphics-view">
                        <stroke-icon-button icon-name="book">Книги</stroke-icon-button>
                    </router-link>
                    <router-link to="/graphics-view">
                        <stroke-icon-button icon-name="code">Программы</stroke-icon-button>
                    </router-link>
                </div>
                <router-view></router-view>
            </div>
        </div>
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
    margin-top: 20px;
}

.profile-inner {
    display: flex;
    justify-content: space-between;
}

.profile-info {
    flex: 0.5;
    margin-right: 10px;
    padding: 20px;
    height: 300px;
    min-height: 200px;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 30px;
}

.profile-media {
    flex: 1;
    height: fit-content;
    min-height: 500px;
    padding: 10px 20px 50px;

    border-radius: 30px;
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
</style>