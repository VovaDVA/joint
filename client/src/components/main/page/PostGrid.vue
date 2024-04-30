<template>
    <div class="content-grid">
        <slot></slot>
    </div>
</template>

<script>
export default {
    name: 'post-grid',
}
</script>

<style scoped>
.content-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
    grid-gap: 10px;
}

@media (max-width: 500px) {
    .content-grid {
        grid-template-columns: repeat(auto-fill, minmax(100%, 100%));
    }
}
</style>
