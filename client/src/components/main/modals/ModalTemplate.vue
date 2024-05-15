<template>
    <div class="modal-container" v-if="modal !== ''">
        <div class="dark-bg"></div>
        <div class="modal">
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    name: 'modal-template',
    data() {
        return {
            modal: 'g',
        }
    },
    methods: {
        hideModal() {
            this.modal = '';
        }
    }
}
</script>

<style scoped>
.modal {
    display: flex;
    flex-direction: column;
    gap: 15px;
    height: fit-content;
    width: fit-content;
    min-width: 400px;
    position: fixed;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
    margin: auto;
    z-index: 10000;

    padding: 15px 20px;
    border: 1px #ffffff4f solid;
    border-radius: 20px;
    /* background: #191e27; */
    background: #252d3a;
}

.dark-bg {
    width: 100vw;
    height: 100vh;
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background-color: #00000094;
}
</style>