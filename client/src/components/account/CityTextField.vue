<script>
export default {
  name: 'city-text-field'
}
</script>

<template>
    <label for="city">Город</label>
    <div class="input_wrapper">
      <input type="text" name="city" id="city" placeholder="Введите город" value="" required>
    </div>
    <div id="city_err" class="msg"></div>
</template>

<style scoped>

</style>