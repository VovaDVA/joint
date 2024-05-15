<template>
    <!-- <div class="modal-change-banner">
        <div class="window">
            <div class="content">
                <div class="head">
                    Редактирование обложки
                </div>
                <icon-button icon-name="close"></icon-button>
            </div>

            <div class="content">

                <div class="photo">
                    <div class="square">

                    </div>
                </div>
            </div>
            <div class="buttons">
                <div class="block">
                    <div class="button" id="load">
                        Загрузить обложку
                    </div>
                    <div class="button">
                        Сохранить
                    </div>
                </div>
                <div class="block">
                    <div class="button" id="back">
                        Отмена
                    </div>
                </div>
            </div>
        </div>
    </div> -->

    <modal-template v-if="modal == 'load'">
        <content-block-title>Изменение обложки</content-block-title>
        <content-block-text class="text">Привлекательная обложка в профиле поможет вам выделиться среди других
            пользователей Joint</content-block-text>
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
            <div class="title">Обложка профиля</div>
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
    name: 'modal-change-banner',
    props: ['name'],
    data() {
        return {
            modal: '',
            url: null,
        }
    },
    mounted() {
        this.emitter.on('request-change-banner', () => {
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
/* .modal-change-banner {
    display: flex;
    position: fixed;
    top: 0px;
    left: 0px;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.512);
    border-radius: 15px;
    transition: background .2s linear;
}

.window {
    display: flex;
    margin: auto;
    border: 1px #ffffff2f solid;
    background: rgb(36, 43, 54);
    border-radius: 20px;
    flex-direction: column;
    padding: 5px;
}

.content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 5px 10px 5px 10px;
    font-size: 15px;
} */

/* .head {
    font-size: 15px;
}

.photo {
    display: flex;
    align-items: center;
    height: 360px;
    width: 640px;
    background-color: rgba(0, 0, 0, 0.437);
    border-radius: 15px;
}

.buttons {
    display: flex;
    align-items: center;
    margin: 5px 10px 5px 10px;
    font-size: 15px;
    justify-content: space-between;
}

.block {
    display: flex;
}

.button {
    padding: 5px 10px 5px 10px;
    margin: 5px;
    border: 1px #ffffff2f solid;
    border-radius: 100px;
    background: rgb(238, 238, 238);
    color: rgb(11, 10, 10);
}

#back {

    color: rgb(239, 237, 237);
    background: rgb(66, 66, 76);
    margin: 5px 0px 5px 50px;
}

#load {
    margin: 5px 5px 5px 0px;
}

.square {
    width: 99%;
    height: 200px;
    margin: auto;
    border: 1px #adadad8a solid;
} */

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
    max-width: 1000px;
    /* border: 1px #ffffff2f solid; */
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