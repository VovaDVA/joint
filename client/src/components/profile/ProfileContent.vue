<template>
    <div class='profile-content'>
        <div class="profile-inner">
            <div class="settings_form"></div>
            <div class="settings_about">
                <!-- <content-block-title>О себе</content-block-title>
                <content-block-text>
                    <p>Указывать свои данные необязательно.</p>
                    <p>Здесь вы можете добавить описание вашего профиля</p>
                </content-block-text> -->
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

.settings_about {
    flex: 1;
    height: fit-content;
    min-height: 500px;
    padding: 10px 20px 50px;

    border-radius: 30px;
    background: rgba(0, 0, 0, 0.5);

    position: relative;
}

.settings_form {
    flex: 0.5;
    margin-right: 10px;
    padding: 20px;
    height: 300px;
    min-height: 200px;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 30px;
}

.settings_cancel {
    line-height: 28px;
    font-size: 13px;
    border: 1px #ffffff solid;
    border-radius: 30px;
    max-height: 30px;
    margin: 5px 0 15px 10px;
    padding: 0 20px;
    width: 49%;
    text-align: center;
    color: #ffffff;

    transition: color, background .3s linear;
}

.settings_cancel:hover {
    background: #ffffff;
    color: #000000;

    transition: color, background .3s linear;
}

#settings_inner {
    display: flex;
}
</style>