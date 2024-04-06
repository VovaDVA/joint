<template>
    <div class="input-field">
        <label>
            <slot></slot>
        </label>
        <div class="input-wrapper">
            <input class="input-field" placeholder="Введите данные" v-bind="$attrs" v-model="value">
        </div>
    </div>
</template>

<script>
export default {
    name: 'form-input',
}
</script>

<style scoped>
.input-field {
    text-align: left;
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

    flex: 1;

    font-family: 'Montserrat', sans-serif;
    font-size: 14px;
}

label {
    padding: 20px;
    font-size: 15px;
}
</style>