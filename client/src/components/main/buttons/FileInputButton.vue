<template>
    <label class="input-file" :class="$store.state.theme">
        <input type="file" accept="image/*">
        <div><slot></slot></div>
    </label>
</template>

<script>
export default {
    name: 'file-input-button',
}
</script>

<style scoped>
.input-file {
    width: 100%;
    position: relative;
    display: inline-block;
}

.input-file div {
    cursor: pointer;
    font-size: 15px;
    color: #000000;
    text-align: center;
    border-radius: 100px;
    background-color: #ffffff;
    padding: 5px 10px;
    border: none;
    transition: color, background .2s linear;
}

input[type=file] {
    position: absolute;
    z-index: -1;
    opacity: 0;
    display: block;
    width: 0;
    height: 0;
}

.input-file div:hover {
    background-color: #cecece;
    transition: color, background .2s linear;
}

.input-file.light-theme div {
    background: #d39000;
    color: #ffffff;
}

.input-file.light-theme div:hover {
    background: #b47b00;
    color: #ffffff;
}
</style>
