<template>
    <div v-if="modal !== ''" class="dark-bg"></div>
    <auth-block v-if="modal == 'delete'">
        <content-block-text>Вы действительно хотите удалить аккаунт?</content-block-text>
        <div class="modal-buttons">
            <div class="submit-btn" @click="hideModal">Нет</div>
            <div class="submit-btn red" @click="sendDeleteAccountCode">Да</div>
        </div>
    </auth-block>
    <auth-block v-if="modal == 'confirm-delete'">
        <content-block-title>Удаление аккаунта</content-block-title>
        <form @submit.prevent="confirmAccountDelete">
            <form-input v-model="verificationCode">На ваш email отправлен код</form-input>
            <input class="submit-btn red" :class="$store.state.theme" type="submit" name="submit"
                value="Удалить аккаунт">
        </form>
    </auth-block>
</template>

<script>
import { deleteSession, getToken, getUser } from '@/modules/auth';
export default {
    name: 'modal-delete-account',
    data() {
        return {
            user: {},
            modal: '',
            verificationCode: '',
        }
    },
    mounted() {
        this.user = getUser();
        this.emitter.on('delete-account-request', () => {
            this.modal = 'delete';
        });
    },
    methods: {
        async sendDeleteAccountCode() {
            try {
                const response = await fetch('/auth/delete', {
                    method: 'delete',
                    headers: {
                        'Authorization': 'Bearer ' + getToken(),
                        'Content-Type': 'application/json'
                    },
                });

                const data = await response.text();
                console.log(data);
                this.modal = 'confirm-delete';

            } catch (error) {
                console.error(error);
            }
        },
        async confirmAccountDelete() {
            try {
                const response = await fetch('/auth/confirm-delete', {
                    method: 'delete',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        userId: getUser().userId,
                        verificationCode: this.verificationCode
                    })
                });

                const data = await response.text();
                console.log(data);
                deleteSession();

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