<script>
export default {
  name: 'content-block-text'
}
</script>

<template>
  <div class="content-block-text">
    <slot></slot>
  </div>
</template>

<style scoped>
.content-block-text {
  padding: 0 5px 10px;
  background: none;
  height: fit-content;
  font-weight: 500;
  font-size: 17px;
  line-height: 25px;
  user-select: none;
}
</style>