<script>
export default {
  name: 'email-text-field'
}
</script>

<template>
    <label for="email">Email</label>
    <div class="input_wrapper">
      <input type="text" name="email" id="email" placeholder="Введите E-mail" value="" required>
    </div>
    <div id="email_err" class="msg"></div>
</template>

<style scoped>

</style>