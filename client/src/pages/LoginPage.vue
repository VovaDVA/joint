<template>
	<auth-block>
		<content-block-title>Авторизация</content-block-title>
		<form id="login" @submit.prevent="login">
			<email-input v-model="email">Почта</email-input>
			<password-input v-model="password">Пароль</password-input>
			
			<input id="submit" class="submit_btn" type="submit" name="submit" value="Войти">
		</form>
		<div class="no_account">Нет аккаунта? - <router-link to="/register">Зарегистрироваться</router-link></div>
		<div class="lost_password"><a href="">Забыли пароль?</a></div>
	</auth-block>
</template>

<script>
import { saveToken, checkToken } from '../modules/auth';

export default {
	data() {
		return {
			email: '',
			password: '',
		};
	},
	created() {
		checkToken();
	},
	methods: {
		async login(event) {
			event.preventDefault();
			try {
				const response = await fetch('/auth/login', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
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