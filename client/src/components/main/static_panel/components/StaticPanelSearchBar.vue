<template>
    <div class="search-bar" :class="$store.state.theme">
        <div class="icon">
            <font-awesome-icon icon="search" />
        </div>
        <input class="message-input" type="text" placeholder="Поиск..." @input="search">
    </div>
</template>

<script>
import { FontAwesomeIcon } from '@/fontawesome';

export default {
    name: 'static-panel-search-bar',
    components: {
        FontAwesomeIcon
    },
    props: {
        iconName: {
            type: String, // Ожидаем строковое значение для имени иконки
            default: 'pen' // Значение по умолчанию
        }
    },
    methods: {
        search(event) {
            this.$emit('input', event.target.value); // Генерируем событие при изменении текста поиска
        }
    }
}
</script>

<style scoped>
.search-bar {
    display: flex;
    flex: 1;
    padding: 12px;

    line-height: 14px;
    color: #aaaaaa;
    align-items: center;

    border: 1px #ffffff4f solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
}

.search-bar.light-theme {
    background: #fff;
    border: 1px #0000004f solid;
}

.search-bar.light-theme input {
    color: #000 !important;
}

.search-bar.light-theme input::-webkit-input-placeholder { /* WebKit, Blink, Edge */
    color:    #0000007e;
}
.search-bar.light-theme input:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
   color:    #0000007e;
   opacity:  1;
}
.search-bar.light-theme input::-moz-placeholder { /* Mozilla Firefox 19+ */
   color:    #0000007e;
   opacity:  1;
}
.search-bar.light-theme input:-ms-input-placeholder { /* Internet Explorer 10-11 */
   color:    #0000007e;
}
.search-bar.light-theme input::-ms-input-placeholder { /* Microsoft Edge */
   color:    #0000007e;
}
.search-bar.light-theme input::placeholder { /* Most modern browsers support this now. */
   color:    #0000007e;
}

.icon {
    width: 14px;
    height: 14px;
    margin-right: 10px;
}

.svg-inline--fa {
    height: auto;
    vertical-align: 0;
}

input {
    padding: 0;
    border: none;
    outline: none;
    background-color: transparent;
    flex: 1;

    font-family: 'Montserrat', sans-serif;
    font-size: 14px;
}
</style>