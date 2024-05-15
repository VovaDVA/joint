<template>
    <modal-template v-if="modal == 'load'">
        <content-block-title>Изменение фотографии</content-block-title>
        <content-block-text class="text">Загрузите вашу настоящую фотографию, чтобы пользователям было проще вас
            узнать</content-block-text>
        <div class="buttons">
            <form enctype="multipart/form-data" @submit.prevent="loadImage">
                <label class="input-file">
                    <input type="file" accept="image/*" @change="loadImage">
                    <div>Загрузить фото</div>
                </label>
            </form>
            <div class="button back" @click="hideModal">Отмена</div>
        </div>
    </modal-template>
    <modal-template v-if="modal == 'preview'">
        <div class="header">
            <div class="title">Аватар профиля</div>
            <icon-button icon-name="close" @click="hideModal"></icon-button>
        </div>
        <img v-if="url" class="photo" :src="url" />
        <div class="buttons">
            <div class="button">Сохранить</div>
            <div class="button back" @click="returnToLoad">Назад</div>
        </div>
    </modal-template>
</template>

<script>
export default {
    name: 'modal-change-avatar',
    props: ['name'],
    data() {
        return {
            modal: '',
            url: null,
        }
    },
    mounted() {
        this.emitter.on('request-change-avatar', () => {
            this.modal = 'load';
        });
    },
    methods: {
        loadImage(e) {
            const file = e.target.files[0];
            this.url = URL.createObjectURL(file);
            this.modal = 'preview';
        },
        returnToLoad() {
            this.modal = 'load';
        },
        hideModal() {
            this.modal = '';
        }
    }
}
</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.content {
    position: relative;
    display: flex;
    align-items: center;
    height: 400px;
    width: 400px;
    background-color: rgba(0, 0, 0, 0.437);
    border-radius: 15px;
}

.photo {
    max-width: 600px;
    /* border-radius: 10px; */
}

.buttons {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
}

.button-container {
    display: flex;
    justify-content: center;
    gap: 10px;
}

.button {
    width: 100%;
    max-width: 200px;
    padding: 5px 10px;
    font-size: 15px;
    font-family: 'Montserrat', sans-serif;
    border: 1px #ffffff2f solid;
    border-radius: 100px;
    background: #e4e4e4;
    color: #000000;
    text-align: center;
    cursor: pointer;

    transition: color, background .2s linear;
}

.text {
    max-width: 500px;
    text-align: center;
}

form {
    width: 100%;
    max-width: 200px;
}

.button.back {
    color: #ffffff;
    background: rgb(66, 66, 76);
}

.crop-square {
    width: 300px;
    height: 300px;
    margin: auto;
    border: 1px #adadad8a solid;
}

.input-file {
    width: 100%;
    position: relative;
    display: inline-block;
}

.input-file div {
    cursor: pointer;
    font-size: 15px;
    color: #000000;
    text-align: center;
    border-radius: 100px;
    background-color: #e7e7e7;
    padding: 5px 10px;
    border: none;
    transition: color, background .2s linear;
}

input[type=file] {
    position: absolute;
    z-index: -1;
    opacity: 0;
    display: block;
    width: 0;
    height: 0;
}

.input-file div:hover {
    background-color: transparent;
    color: #ffffff !important;
    border: 1px #ffffff solid;

    transition: color, background .2s linear;
}
</style>
