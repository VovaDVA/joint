<template>
    <div class="input-field" :class="$store.state.theme">
        <label>
            <slot></slot>
        </label>
        <div class="input-wrapper">
            <textarea class="input-field" :placeholder="data" v-bind="$attrs" v-model="value" rows="5"></textarea>
        </div>
    </div>
</template>

<script>
export default {
    name: 'form-text-area',
    props: ['data', 'type'],
    data() {
        return {
            message: '',
            height: '30px'
        }
    },
    // watch: {
    //     value() {
    //         this.getHeight();
    //     }
    // },
    // methods: {
    //     getHeight() {
    //         this.height = calcTextareaHeight(this.$refs.textarea, 1, null).height;
    //     }
    // }
}
</script>

<style scoped>
.input-field {
    text-align: left;
}

.input-field.light-theme textarea {
    color: #000000 !important;
    background-color: #ffffff;
    border: 1px #0000007c solid;
}

.input-field.light-theme textarea::-webkit-input-placeholder { /* WebKit, Blink, Edge */
    color:    #0000007e;
}
.input-field.light-theme textarea:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
   color:    #0000007e;
   opacity:  1;
}
.input-field.light-theme textarea::-moz-placeholder { /* Mozilla Firefox 19+ */
   color:    #0000007e;
   opacity:  1;
}
.input-field.light-theme textarea:-ms-input-placeholder { /* Internet Explorer 10-11 */
   color:    #0000007e;
}
.input-field.light-theme textarea::-ms-input-placeholder { /* Microsoft Edge */
   color:    #0000007e;
}
.input-field.light-theme textarea::placeholder { /* Most modern browsers support this now. */
   color:    #0000007e;
}

.input-wrapper {
    margin: 10px 0;
}

textarea {
    resize: none;
    width: 100%;
    padding: 10px 20px;
    font-size: 15px;
    line-height: 20px;
    border: 1px #ffffff7c solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
    outline: none;

    font-family: 'Montserrat', sans-serif;
}

label {
    padding: 20px;
    font-size: 15px;
}
</style>