<template>
    <div v-if="modal !== ''" class="dark-bg"></div>
    <auth-block v-if="modal == 'request'">
        <content-block-title>Изменить пароль?</content-block-title>
        <div class="modal-buttons">
            <modal-button @click="hideModal">Нет</modal-button>
            <modal-button class="cancel" @click="sendCode">Да</modal-button>
        </div>
    </auth-block>
    <auth-block v-if="modal == 'enter-code'">
        <content-block-title>Смена пароля</content-block-title>
        <form @submit.prevent="submitCode">
            <form-input v-model="verificationCode" data="Код из E-mail">На ваш email отправлен код</form-input>
            <input class="submit-btn" :class="$store.state.theme" type="submit" name="submit" value="Отправить">
        </form>
    </auth-block>
    <auth-block v-if="modal == 'confirm'">
        <content-block-title>Смена пароля</content-block-title>
		<div class="lost-password">{{ errorMessage }}</div>
        <form @submit.prevent="confirmChangePassword">
            <password-input v-model="password">Текущий пароль</password-input>
            <password-input v-model="newPassword" data="Новый пароль">Новый пароль</password-input>
            <input class="submit-btn" :class="$store.state.theme" type="submit" name="submit" value="Подтвердить">
        </form>
    </auth-block>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { getUser } from '@/modules/auth';
export default {
    name: 'modal-change-password',
    data() {
        return {
            user: {},
            modal: '',
            verificationCode: '',
            password: '',
            newPassword: '',
            errorMessage: '',
        }
    },
    mounted() {
        this.user = getUser();
        this.emitter.on('change-password-request', () => {
            this.modal = 'request';
        });
    },
    methods: {
        async sendCode() {
            await apiClient.auth.changePassword({}, () => {
                this.modal = 'enter-code';
            });
        },
        async submitCode() {
            this.modal = 'confirm';
        },
        async confirmChangePassword() {
            await apiClient.auth.confirmChangePassword({
                userId: getUser().userId,
                verificationCode: this.verificationCode,
                currentPassword: this.password,
                newPassword: this.newPassword
            }, () => this.hideModal(), (data) => {
                this.errorMessage = data['message'];
            });
        },
        hideModal() {
            this.modal = '';
        }
    }
}
</script>

<style scoped>
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
    user-select: none;

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
    justify-content: center;
    gap: 10px;
}

.red {
    border: 1px #ff6161 solid;
    color: #ff6161 !important;
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
    z-index: 1000;
    background-color: #00000094;
}
</style>