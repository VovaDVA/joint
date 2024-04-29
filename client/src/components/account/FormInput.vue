<template>
    <div class="input-field" :class="$store.state.theme">
        <label>
            <slot></slot>
        </label>
        <div class="input-wrapper">
            <input :type="type" class="input-field" :placeholder="data ?? 'Введите данные'" v-bind="$attrs" v-model="value">
        </div>
    </div>
</template>

<script>
export default {
    name: 'form-input',
    props: ['data', 'type'],
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

.input-field.light-theme input::-webkit-input-placeholder { /* WebKit, Blink, Edge */
    color:    #0000007e;
}
.input-field.light-theme input:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
   color:    #0000007e;
   opacity:  1;
}
.input-field.light-theme input::-moz-placeholder { /* Mozilla Firefox 19+ */
   color:    #0000007e;
   opacity:  1;
}
.input-field.light-theme input:-ms-input-placeholder { /* Internet Explorer 10-11 */
   color:    #0000007e;
}
.input-field.light-theme input::-ms-input-placeholder { /* Microsoft Edge */
   color:    #0000007e;
}
.input-field.light-theme input::placeholder { /* Most modern browsers support this now. */
   color:    #0000007e;
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
</style>