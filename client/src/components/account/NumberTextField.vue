<script>
export default {
    name: 'number-text-field',
    props: ['value', 'inputId'],
    methods: {
        updateValue(newValue) {
            this.$emit('input', newValue);
        }
    }
}
</script>

<template>
    <label>
        <slot></slot>
    </label>
    <div class="input_wrapper">
        <input :id="inputId" type="number" placeholder="Введите данные" :value="value"
            @input="updateValue($event.target.value)" required>
    </div>
</template>

<style scoped></style>