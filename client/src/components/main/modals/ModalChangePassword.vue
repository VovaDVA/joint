<template>
    <div v-if="modal !== ''" class="dark-bg"></div>
    <auth-block v-if="modal == 'request'">
        <content-block-title>Изменить пароль?</content-block-title>
        <div class="modal-buttons">
            <div class="submit-btn" @click="hideModal">Нет</div>
            <div class="submit-btn" @click="sendCode">Да</div>
        </div>
    </auth-block>
    <auth-block v-if="modal == 'enter-code'">
        <content-block-title>Смена пароля</content-block-title>
        <form @submit.prevent="submitCode">
            <form-input v-model="verificationCode">На ваш email отправлен код</form-input>
            <input class="submit-btn" :class="$store.state.theme" type="submit" name="submit"
                value="Отправить">
        </form>
    </auth-block>
    <auth-block v-if="modal == 'confirm'">
        <content-block-title>Смена пароля</content-block-title>
        <form @submit.prevent="confirmChangePassword">
            <password-input v-model="password">Текущий пароль</password-input>
            <password-input v-model="newPassword">Новый пароль</password-input>
            <input class="submit-btn" :class="$store.state.theme" type="submit" name="submit"
                value="Подтвердить">
        </form>
    </auth-block>
</template>

<script>
import { getToken, getUser } from '@/modules/auth';
export default {
    name: 'modal-change-password',
    data() {
        return {
            user: {},
            modal: '',
            verificationCode: '',
            password: '',
            newPassword: ''
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
            try {
                const response = await fetch('/auth/change-password', {
                    method: 'post',
                    headers: {
                        'Authorization': 'Bearer ' + getToken(),
                        'Content-Type': 'application/json'
                    },
                });

                const data = await response.text();
                console.log(data);
                this.modal = 'enter-code';

            } catch (error) {
                console.error(error);
            }
        },
        async submitCode() {
            this.modal = 'confirm';
        },
        async confirmChangePassword() {
            try {
                const response = await fetch('/auth/confirm-change-password', {
                    method: 'post',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        userId: getUser().userId,
                        verificationCode: this.verificationCode,
                        currentPassword: this.password,
                        newPassword: this.newPassword
                    })
                });

                const data = await response.text();
                console.log(data);
                this.hideModal();

            } catch (error) {
                console.error(error);
            }
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
    justify-content: space-between;
    gap: 20px;
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