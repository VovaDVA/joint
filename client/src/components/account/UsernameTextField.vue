<script>
export default {
  name: 'username-text-field',
  props: ['value', 'inputId'],
  methods: {
    updateValue(newValue) {
      this.$emit('input', newValue);
    }
  }
}
</script>

<template>
  <label for="username">Логин</label>
  <div class="input_wrapper">
    <input :id="inputId" type="text" placeholder="Введите логин" :value="value" @input="updateValue($event.target.value)">
  </div>
</template>

<style scoped>
label {
  line-height: 25px;
}
</style>