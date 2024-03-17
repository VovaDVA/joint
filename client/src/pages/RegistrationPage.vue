<template>
	<div class='signup_form'>

		<form id="wp_signup_form" @submit.prevent="register">
			<page-title>Регистрация</page-title>
			<form-block>
				<username-text-field :value="username" @input="username = $event" inputId="username"/>
			</form-block>
			<form-block>
				<email-text-field />
			</form-block>
			<form-block>
				<password-text-field :value="password" @input="password = $event" inputId="password">Введите пароль</password-text-field>
			</form-block>
			<form-block>
				<password-text-field>Подтвердите пароль</password-text-field>
			</form-block>

			<div class="form_block terms">
				<div class="input_wrapper">
					<input name="terms" id="terms" type="checkbox" value="0" required>
					<label for="terms">Я согласен(-на) с условиями предоставления услуг</label>
				</div>
			</div>
			<input type="submit" id="submitbtn" class="submit_btn" name="submit" value="Зарегистрироваться" />

			<div class="no_account">Уже есть аккаунт? - <router-link to="/login">Войти</router-link></div>
		</form>

	</div>
</template>

<script>
import { saveToken, checkToken } from '../modules/auth';

export default {
	data() {
		return {
			username: '',
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

			try {
				const response = await fetch('/auth/register', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						username: this.username,
						password: this.password
					})
				});

				const data = await response.json();
				console.log(data);

				if (data['status'] == '200') {
					saveToken(data['token']);
					this.$router.push('/');
				} else {
					alert(data['message']);
				}

			} catch (error) {
				console.error(error);
			}
		}
	}
};
</script>
  
<style scoped></style>