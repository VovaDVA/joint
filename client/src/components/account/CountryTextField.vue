<script>
export default {
  name: 'country-text-field'
}
</script>

<template>
    <label for="country">Страна</label>
    <div class="input_wrapper">
      <input type="text" name="country" id="country" placeholder="Введите страну" value="" required>
    </div>
    <div id="country_err" class="msg"></div>
</template>

<style scoped>

</style>