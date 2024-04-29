<template>
	<auth-block>
		<content-block-title>Авторизация</content-block-title>
		<form id="login" @submit.prevent="login">
			<email-input v-model="email">Почта</email-input>
			<password-input v-model="password">Пароль</password-input>
			
			<input :class="$store.state.theme" type="submit" name="submit" value="Войти">
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