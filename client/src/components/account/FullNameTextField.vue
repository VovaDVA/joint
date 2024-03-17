<script>
export default {
  name: 'fullname-text-field'
}
</script>

<template>
    <label for="fullname">ФИО</label>
    <div class="input_wrapper">
      <input type="text" name="fullname" id="fullname" placeholder="Введите ФИО" value="" required>
    </div>
    <div id="fullname_err" class="msg"></div>
</template>

<style scoped>

</style>