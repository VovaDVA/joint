<template>
    <div class="banner">
        <div class="profile-icon-container">
            <router-link to="/profile-settings">
                <filled-icon-button icon-name="house"></filled-icon-button>
            </router-link>
            <router-link to="/profile-settings">
                <filled-icon-button icon-name="user"></filled-icon-button>
            </router-link>
            <router-link to="/profile-settings">
                <filled-icon-button icon-name="star"></filled-icon-button>
            </router-link>
            <router-link to="/profile-settings">
                <filled-icon-button icon-name="calendar"></filled-icon-button>
            </router-link>
            <router-link to="/profile-settings">
                <filled-icon-button icon-name="phone-volume"></filled-icon-button>
            </router-link>
            <router-link to="/profile-settings">
                <filled-icon-button icon-name="gear"></filled-icon-button>
            </router-link>
        </div>
        <div class="banner-inner">
            <div class="banner-content">
                <filled-icon-button>Изменить обложку</filled-icon-button>
            </div>
            <user-avatar></user-avatar>
            <profile-content></profile-content>
        </div>
    </div>
</template>

<script>
export default {
    name: 'profile-banner',
}
</script>

<style scoped>
.banner {
    display: flex;
    width: 100%;
}

.banner-inner {
    flex: 1;
    position: relative;
    height: 0;
    border: none;
    padding-top: 22%;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.banner-content {
    position: absolute;
    top: 0;
    right: 0;
    padding: 10px;
}

.banner img {
    width: 100%;
    height: 100%;
}

.icon_btn.change_banner {
    margin: 10px 10px 0 auto;
}

.icon_btn span {
    padding: 9px 0 9px 15px;
}

.profile-icon-container {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    align-items: start;
    flex: 0 0 45px;
}

.profile-icon-container .filled-icon-button {
    margin-bottom: 10px;
}

.profile-icon {
    display: block;
    width: 35px;
    height: 35px;
    margin-bottom: 10px;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
}

a {
    color: white;
}
</style>