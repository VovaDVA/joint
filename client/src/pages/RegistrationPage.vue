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
			<input :class="$store.state.theme" type="submit" name="submit" value="Зарегистрироваться" />
			<div class="no_account">Уже есть аккаунт? - <router-link to="/login">Войти</router-link></div>
		</form>
	</auth-block>
</template>

<script>
import { checkToken } from '../modules/auth';

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

				if (data['id']) {
					this.$router.push('/login');
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