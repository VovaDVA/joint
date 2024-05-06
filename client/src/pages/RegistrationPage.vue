<template>
	<auth-block>
		<content-block-title>Регистрация</content-block-title>
		<div class="error">{{ errorMessage }}</div>

		<form @submit.prevent="register">
			<form-input v-model="firstName" required>Имя</form-input>
			<form-input v-model="lastName" required>Фамилия</form-input>
			<email-input v-model="email" required>Почта</email-input>
			<password-input v-model="password" required>Пароль</password-input>

			<div class="toggle-wrapper">
				<input name="terms" id="terms" type="checkbox" value="0" required>
				<label for="terms">Я согласен(-на) с условиями предоставления услуг</label>
			</div>
			<input :class="$store.state.theme" type="submit" name="submit" value="Зарегистрироваться" />
			<div class="no_account">Уже есть аккаунт? - <router-link to="/login">Войти</router-link></div>
		</form>
	</auth-block>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { checkToken } from '../modules/auth';

export default {
	data() {
		return {
			firstName: '',
			lastName: '',
			email: '',
			password: '',
			confirmPassword: '',
			errorMessage: ''
		};
	},
	created() {
		checkToken();
	},
	methods: {
		async register(event) {
			event.preventDefault();

			await apiClient.auth.register({
				firstName: this.firstName,
				lastName: this.lastName,
				email: this.email,
				password: this.password
			}, () => {
				this.$router.push('/login');
			}, (data) => {
				this.errorMessage = data['message'];
			});	
		}
	}
};
</script>

<style scoped>
.toggle-wrapper {
	margin-top: 20px;
	display: flex;
}

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

.error {
	text-align: center;
	color: #ff8686;
}
</style>