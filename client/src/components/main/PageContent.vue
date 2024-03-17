<template>
    <div class="page-content">
        <content-block-title><slot name="title"></slot></content-block-title>
        <content-block-text><slot></slot></content-block-text>
    </div>
</template>

<script>
export default {
    name: 'page-content',
};
</script>

<style scoped>
.page-content {
    font-family: 'Montserrat', sans-serif;
    position: relative;
    margin: 0 auto;
    /* margin-left: 360px; */
    margin-top: -50px;

    width: 800px;
    height: fit-content;
    padding: 10px 20px 50px;

    border-radius: 30px;
    color: #FFFFFF;
    background: rgba(0, 0, 0, 0.5);
}
</style>