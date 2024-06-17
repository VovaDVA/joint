<template>
	<auth-block v-if="!resetForm && form !== 'two-factor'">
		<content-block-title>Авторизация</content-block-title>
		<div class="lost-password">{{ errorMessage }}</div>
		<form @submit.prevent="login">
			<email-input v-model="email" required>E-mail</email-input>
			<password-input v-model="password" required>Пароль</password-input>

			<input :class="$store.state.theme" type="submit" name="submit" value="Войти">
		</form>
		<div class="no_account">Нет аккаунта? - <router-link to="/register">Зарегистрироваться</router-link></div>
		<div class="lost-password" @click="resetPassword">Забыли пароль?</div>
	</auth-block>

	<auth-block v-if="resetForm">
		<content-block-title>Восстановление пароля</content-block-title>
		<div class="lost-password">{{ errorMessage }}</div>
		<form v-if="form == 'send-code'" @submit.prevent="sendResetCode">
			<email-input v-model="email">E-mail для восстановления пароля</email-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Отправить код">
		</form>
		<form v-if="form == 'enter-code'" @submit.prevent="enterResetCode">
			<form-input v-model="resetPasswordCode" data="Код из E-mail">{{ resetPasswordMessage }}</form-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Восстановить пароль">
		</form>
		<form v-if="form == 'enter-new-password'" @submit.prevent="confirmResetPassword">
			<password-input v-model="newPassword" data="Новый пароль">Новый пароль</password-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Подтвердить">
		</form>
		<div class="lost-password recall" @click="returnToLogin">Вспомнили пароль?</div>
	</auth-block>

	<auth-block v-if="form == 'two-factor'">
		<content-block-title>Авторизация</content-block-title>
		<div class="lost-password">{{ errorMessage }}</div>
		<form @submit.prevent="confirmTwoFactor">
			<form-input v-model="verificationCode">На Ваш email отправлен код</form-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Подтвердить">
		</form>
	</auth-block>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { checkToken, saveToken } from '../modules/auth';

export default {
	data() {
		return {
			email: '',
			password: '',
			newPassword: '',
			resetForm: false,
			form: 'send-code',
			resetPasswordMessage: '',
			resetPasswordCode: '',
			errorMessage: ''
		};
	},
	created() {
		checkToken();
	},
	methods: {
		async login(event) {
			event.preventDefault();
			await apiClient.auth.login({ email: this.email, password: this.password }, (data) => {
				if (data['twoFactorVerified']) {
					this.form = 'two-factor';
				} else {
					this.confirmLogin(data);
				}
			}, (data) => {
				this.errorMessage = data['message'];
			});
		},
		resetPassword() {
			this.resetForm = true;
			this.email = '';
			this.errorMessage = '';
		},
		async sendResetCode() {
			await apiClient.auth.sendPasswordResetCode({ email: this.email }, (data) => {
				this.resetPasswordMessage = data['message'];
				this.form = 'enter-code';
			});
		},
		enterResetCode() {
			this.form = 'enter-new-password';
		},
		async confirmResetPassword() {
			await apiClient.auth.confirmPasswordReset({
				verificationCode: this.resetPasswordCode,
				newPassword: this.newPassword
			}, () => checkToken(), (data) => {
				this.errorMessage = data['message'];
			});
		},
		async confirmTwoFactor() {
			await apiClient.auth.verifyCode({
				code: this.verificationCode,
			}, (data) => {
				this.confirmLogin(data);
			}, (data) => {
				this.errorMessage = data['message'];
			});
		},
		async confirmLogin(data) {
			await saveToken(data['token']);
			this.$router.push('/');
		},
		returnToLogin() {
			this.resetForm = false;
			this.errorMessage = '';
		}
	}
};
</script>

<style scoped>
input {
	line-height: 28px;
	padding: 5px;
	width: 100%;
	margin: 20px auto;

	border: 1px #ffffff solid;
	border-radius: 30px;

	font-family: 'Montserrat', sans-serif;
	font-size: 17px;
	text-decoration: none;
	color: #ffffff;
	background: none;

	transition: color, background .3s linear;
}

input.light-theme {
	border: 1px #000000 solid;
	color: #000 !important;
}

input:hover {
	background-color: #ffffff;
	color: #000000 !important;

	transition: color, background .3s linear;
}

input.light-theme:hover {
	background-color: #000000;
	color: #ffffff !important;

	transition: color, background .3s linear;
}
</style>
