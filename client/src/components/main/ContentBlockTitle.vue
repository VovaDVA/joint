<script>
export default {
  name: 'content-block-title'
}
</script>

<template>
  <div class="content-block-title">
    <slot></slot>
  </div>
</template>

<style scoped>
.content-block-title {
  text-align: center;
  color: #ffbf6c;
  font-size: 22px;
  margin-bottom: 10px;
  padding: 10px;

  border-bottom: 1px solid transparent;
  border-image: radial-gradient(#ffbf6c 50%, transparent);
  border-image-slice: 1;
}
</style>