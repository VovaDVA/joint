<template>
    <div class="input-wrapper">
        <input type="submit" class="submit-button" :class="$store.state.theme" name="submit" :value="data" />
    </div>
</template>

<script>
export default {
    name: 'submit-button',
    props: ['data']
}
</script>

<style scoped>
input {
    padding: 5px 30px;

    border: 1px #ffffff solid;
    border-radius: 50px;

    font-family: 'Montserrat', sans-serif;
    font-size: 15px;
    text-decoration: none;
    color: #dff4ff;
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
    color: #ffffff !important;
    background: #000;
}
</style>