<template>
    <div class='signup_form'>
        <form id="login" @submit.prevent="login">

            <page-title>Вход на сайт</page-title>
            <form-block>
                <username-text-field :value="username" @input="username = $event" inputId="username"/>
            </form-block>
            <form-block>
                <password-text-field :value="password" @input="password = $event" inputId="password">Пароль</password-text-field>
            </form-block>
            <input id="submit" class="submit_btn" type="submit" name="submit" value="Войти">

        </form>
        <div class="no_account">Нет аккаунта? - <router-link to="/register">Зарегистрироваться</router-link></div>
        <div class="lost_password"><a href="">Забыли пароль?</a></div>
    </div>
</template>

<script>
import { saveToken, checkToken } from '../modules/auth';

export default {
	data() {
		return {
			username: '',
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
				const response = await fetch('/auth/authorize', {
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
  
  
  