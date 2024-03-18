<script>
export default {
  name: 'sidebar-menu-item'
}
</script>

<template>
    <div class="sidebar-menu-item"><slot></slot></div>
</template>

<style scoped>

.sidebar-menu-item {
  padding: 15px;
  color: #ffffff;
  font-size: 15px;
  border-bottom: 1px solid transparent;
  border-image: radial-gradient(#FFFFFF 60%, transparent);
  border-image-slice: 1;
}

.sidebar-menu-item:hover {
  border-bottom: 1px solid transparent;
  border-image: radial-gradient(#6dff72 60%, transparent);
  border-image-slice: 1;

  color: #6dff72;
}
</style>