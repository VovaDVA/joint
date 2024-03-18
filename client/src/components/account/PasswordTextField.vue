<script>
export default {
  name: 'password-text-field',
  props: ['value', 'inputId'],
  methods: {
    updateValue(newValue) {
      this.$emit('input', newValue);
    }
  }
}
</script>

<template>
    <label><slot></slot></label>
    <div class="input_wrapper">
      <input :id="inputId" type="password" placeholder="Введите пароль" :value="value" @input="updateValue($event.target.value)" required>
    </div>
</template>

<style scoped>

</style>