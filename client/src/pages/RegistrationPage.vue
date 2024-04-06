<template>
	<auth-block>
		<content-block-title>Регистрация</content-block-title>

		<form @submit.prevent="register">
			<form-input v-model="firstName">Имя</form-input>
			<form-input v-model="lastName">Фамилия</form-input>
			<email-input v-model="email">Почта</email-input>
			<password-input v-model="password">Пароль</password-input>

			<div class="toggle-wrapper">
				<input name="terms" id="terms" type="checkbox" value="0" required>
				<label for="terms">Я согласен(-на) с условиями предоставления услуг</label>
			</div>
			<!-- <div class="form_block terms">
				<div class="input_wrapper">
					<input name="terms" id="terms" type="checkbox" value="0" required>
					<label for="terms">Я согласен(-на) с условиями предоставления услуг</label>
				</div>
			</div> -->
			<input type="submit" id="submitbtn" class="submit_btn" name="submit" value="Зарегистрироваться" />
			<div class="no_account">Уже есть аккаунт? - <router-link to="/login">Войти</router-link></div>
		</form>
	</auth-block>
</template>

<script>
import { saveToken, checkToken } from '../modules/auth';

export default {
	data() {
		return {
			firstName: '',
			lastName: '',
			email: '',
			password: '',
			confirmPassword: '',
			agreeTerms: true
		};
	},
	created() {
		checkToken();
	},
	methods: {
		async register(event) {
			event.preventDefault();

			console.log(this.firstName, this.lastName, this.email, this.password);
			try {
				const response = await fetch('/auth/register', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						firstName: this.firstName,
						lastName: this.lastName,
						email: this.email,
						password: this.password
					})
				});

				const data = await response.json();
				console.log(data);

				if (data['token']) {
					saveToken(data['token']);
					this.$router.push('/');
				}

			} catch (error) {
				console.error(error);
			}
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
	font-size: 17px;
	border: 1px #ffffff solid;
	border-radius: 30px;
	margin: 20px auto;
	padding: 5px;
	width: 100%;
	text-align: center;
	color: #ffffff;

	transition: color, background .3s linear;
}
</style>