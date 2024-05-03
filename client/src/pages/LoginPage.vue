<template>
	<auth-block v-if="!resetForm">
		<content-block-title>Авторизация</content-block-title>
		<form @submit.prevent="login">
			<email-input v-model="email">Почта</email-input>
			<password-input v-model="password">Пароль</password-input>

			<input :class="$store.state.theme" type="submit" name="submit" value="Войти">
		</form>
		<div class="no_account">Нет аккаунта? - <router-link to="/register">Зарегистрироваться</router-link></div>
		<div class="lost-password" @click="resetPassword">Забыли пароль?</div>
	</auth-block>

	<auth-block v-if="resetForm">
		<content-block-title>Восстановление пароля</content-block-title>
		<form v-if="form == 'send-code'" @submit.prevent="sendResetCode">
			<email-input v-model="email">E-mail для восстановления пароля</email-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Отправить код">
		</form>
		<form v-if="form == 'enter-code'" @submit.prevent="enterResetCode">
			<form-input v-model="resetPasswordCode">{{ resetPasswordMessage }}</form-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Восстановить пароль">
		</form>
		<form v-if="form == 'enter-new-password'" @submit.prevent="confirmResetPassword">
			<password-input v-model="newPassword">Новый пароль</password-input>
			<input :class="$store.state.theme" type="submit" name="submit" value="Подтвердить">
		</form>
	</auth-block>

	<auth-block v-if="form == 'two-factor'">
		<content-block-title>Авторизация</content-block-title>
		<form @submit.prevent="confirmTwoFactor">
			<email-input v-model="email">На Ваш email отправлен код</email-input>
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
			resetPasswordCode: ''
		};
	},
	created() {
		checkToken();
	},
	methods: {
		async login(event) {
			event.preventDefault();
			await apiClient.auth.login({ email: this.email, password: this.password }, (data) => {
				// this.form = 'two-factor';
				saveToken(data['token']);
				this.$router.push('/');
			});
		},
		resetPassword() {
			this.resetForm = true;
			this.email = '';
		},
		async sendResetCode() {
			await apiClient.auth.sendPasswordResetCode({ email: this.email }, (data) => {
				this.resetPasswordMessage = data;
				this.form = 'enter-code';
			});
			// try {
			// 	const response = await fetch('/auth/request-reset-password?email=' + this.email, {
			// 		method: 'POST',
			// 		headers: {
			// 			'Content-Type': 'application/json'
			// 		},
			// 		body: JSON.stringify({
			// 			email: this.email,
			// 		})
			// 	});

			// 	const data = await response.text();
			// 	this.resetPasswordMessage = data;
			// 	if (data !== 'Пользователь с таким email не найден') {
			// 		this.form = 'enter-code';
			// 	}

			// } catch (error) {
			// 	console.error(error);
			// }
		},
		enterResetCode() {
			this.form = 'enter-new-password';
		},
		async confirmResetPassword() {
			await apiClient.auth.confirmPasswordReset({
				verificationCode: this.resetPasswordCode,
				newPassword: this.newPassword
			}, () => {});
		},
		confirmTwoFactor() {

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

.lost-password {
	text-align: center;
	margin: 10px auto;
	max-width: 300px;
	color: #ff6767;
	user-select: none;
}

.lost-password:hover {
	cursor: pointer;
}
</style>