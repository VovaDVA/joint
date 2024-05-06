<template>
	<form-input type="password" v-model="value" @input="handleInput">
		<slot></slot>
	</form-input>
	<div v-if="errorMessage" class="error" :class="{ 'active': !isValid }">{{ errorMessage }}</div>
</template>

<script>
export default {
	name: 'password-input',
	data() {
		return {
			errorMessage: '',
			isValid: true
		}
	},
	methods: {
		handleInput(event) {
			const value = event.target.value;
			this.isValid = false;

			if (value.length < 8) {
				this.errorMessage = 'Пароль должен содержать не менее 8 символов';
			} else if (!/[A-Z]/.test(value)) {
				this.errorMessage = 'Пароль должен содержать хотя бы одну заглавную букву';
			} else if (!/[!@#$%^&*()_,.?":{}|<>]/.test(value)) {
				this.errorMessage = 'Пароль должен содержать хотя бы один специальный символ';
			} else if (!/\d/.test(value)) {
				this.errorMessage = 'Пароль должен содержать хотя бы одну цифру';
			} else if (/[а-яА-Я]/.test(value)) {
				this.errorMessage = 'Пароль не должен содержать русские буквы';
			} else {
				this.errorMessage = '';
				this.isValid = true;
			}

			this.$emit('input', value);
		}
	}
};
</script>

<style scoped>
.error {
    display: none;
    visibility: hidden;
    font-size: 14px;
    padding: 0 15px 10px 15px;
    color: #ff8686;
    transition: visibility .3s, margin-top .3s ease-out;
}

.error.active {
    display: block;
    visibility: visible;
    margin-top: 0;
    transition: visibility .3s, margin-top .3s, display ease-out;
}</style>
