<script>
export default {
  name: 'content-block-title'
}
</script>

<template>
    <div class="content-block-title"><slot></slot></div>
</template>

<style scoped>

.content-block-title {
  text-align: center;
  text-transform: uppercase;
  padding: 10px 0 10px;

  border-bottom: 1px solid transparent;
  border-image: radial-gradient(#FFFFFF 60%, transparent);
  border-image-slice: 1;
}
</style>