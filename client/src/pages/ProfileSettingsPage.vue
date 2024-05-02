<template>
    <token-check></token-check>
    <content-block>
        <content-block-title>Настройки</content-block-title>

        <form class="settings-form" enctype="multipart/form-data">
            <form-text-area v-model="firstName" :value="user.description" :data="'Расскажите о себе...'">Обо
                мне</form-text-area>
            <form-input v-model="firstName" :value="user.firstName">Имя</form-input>
            <form-input v-model="firstName" :value="user.lastName">Фамилия</form-input>
            <date-input v-model="firstName" :value="user.birthday">Дата рождения</date-input>
            <form-input v-model="firstName" :value="user.country" data="Укажите страну">Страна</form-input>
            <form-input v-model="firstName" :value="user.city" data="Укажите город">Город</form-input>
            <email-input v-model="firstName" :value="user.email">Почта</email-input>
            <password-input class="password" v-model="firstName" data="**********"
                style="pointer-events: none;">Пароль</password-input>

            <div class="toggle-wrapper">
                <input name="terms" id="terms" type="checkbox" value="0" required>
                <label for="terms">Включить двухфакторнуй аутентификацию</label>
            </div>

            <submit-button class="submit" data="Сохранить изменения"></submit-button>
            <div class="delete" @click="deleteAccountRequest">Удалить аккаунт</div>
        </form>
    </content-block>
</template>

<script>
import { getUser } from '@/modules/auth';
export default {
    data() {
        return {
            user: {},
        }
    },
    mounted() {
        this.user = getUser();
    },
    methods: {
        deleteAccountRequest() {
            this.emitter.emit('delete-account-request');
        },
    }
}
</script>

<style scoped>
.settings-form {
    flex: 2;
    margin-right: 10px;
    max-width: 600px;
    margin: auto;
}

/* input,
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

input {
    margin-left: 0;
} */

.password {
    margin-bottom: 20px;
}

.submit {
    margin-top: 30px;
}

.toggle-wrapper {
    display: flex;
    margin-left: 20px;
    text-align: left;
}

.delete {
    margin-top: 20px;
    color: #ff6161;
    cursor: pointer;
    user-select: none;
}

.submit-btn {
    line-height: 28px;
    padding: 5px;
    width: 100%;
    margin: 20px auto 0;

    border: 1px #ffffff solid;
    border-radius: 30px;

    font-family: 'Montserrat', sans-serif;
    font-size: 17px;
    text-align: center;
    text-decoration: none;
    color: #ffffff;
    background: none;

    transition: color, background .3s linear;
}

.submit-btn.light-theme {
    border: 1px #000000 solid;
    color: #000 !important;
}

.submit-btn:hover {
    background-color: #ffffff;
    color: #000000 !important;

    transition: color, background .3s linear;
}

.submit-btn.light-theme:hover {
    background-color: #000000;
    color: #ffffff !important;

    transition: color, background .3s linear;
}

.modal-buttons {
    display: flex;
    justify-content: space-between;
    gap: 20px;
}

.red {
    border: 1px #ff6161 solid;
    color: #ff6161;
}

.red:hover {
    background-color: #ff6161;
}

.dark-bg {
    width: 100vw;
    height: 100vh;
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #00000071;
    z-index: 10000;
}
</style>