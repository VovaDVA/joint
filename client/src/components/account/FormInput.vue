<template>
    <div class="input-field" :class="$store.state.theme">
        <label>
            <slot></slot>
        </label>
        <div class="input-wrapper">
            <input :type="type" class="input-field" :placeholder="data ?? 'Введите данные'" v-bind="$attrs"
                v-model="value" @input="handleInput">
        </div>
        <div v-if="errorMessage" class="error" :class="{ 'active': !isValid }">{{ errorMessage }}</div>
    </div>
</template>

<script>
export default {
    name: 'form-input',
    props: ['data', 'type'],
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

            switch (this.type) {
                case 'password':
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
                    break;
                default:
                    if (!/[а-яА-Я]/.test(value)) {
                        this.errorMessage = 'Имя и фамилия могут содержать только русские буквы';
                    } else {
                        this.errorMessage = '';
                        this.isValid = true;
                    }
                    break;
                case 'email':
                    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                        this.errorMessage = 'Некорректный формат адреса электронной почты';
                    } else {
                        this.errorMessage = '';
                        this.isValid = true;
                    }
                    break;
            }

            this.$emit('input', value);
        }
    }
}
</script>

<style scoped>
.input-field {
    text-align: left;
}

.input-field.light-theme input {
    color: #000000 !important;
    background-color: #ffffff;
    border: 1px #0000007c solid;
}

.input-field.light-theme input::-webkit-input-placeholder {
    /* WebKit, Blink, Edge */
    color: #0000007e;
}

.input-field.light-theme input:-moz-placeholder {
    /* Mozilla Firefox 4 to 18 */
    color: #0000007e;
    opacity: 1;
}

.input-field.light-theme input::-moz-placeholder {
    /* Mozilla Firefox 19+ */
    color: #0000007e;
    opacity: 1;
}

.input-field.light-theme input:-ms-input-placeholder {
    /* Internet Explorer 10-11 */
    color: #0000007e;
}

.input-field.light-theme input::-ms-input-placeholder {
    /* Microsoft Edge */
    color: #0000007e;
}

.input-field.light-theme input::placeholder {
    /* Most modern browsers support this now. */
    color: #0000007e;
}

.input-wrapper {
    height: 50px;
    margin: 10px 0;
}

input {
    width: 100%;
    height: 100%;
    padding: 10px 20px;
    font-size: 15px;
    border: 1px #ffffff7c solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
    outline: none;

    font-family: 'Montserrat', sans-serif;
}

label {
    padding: 20px;
    font-size: 15px;
}

.error {
    display: none;
    visibility: hidden;
    /* margin-top: -45px; */
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
}
</style>