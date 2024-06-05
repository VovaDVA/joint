<template>
    <modal-template v-if="modal == 'load'">
        <content-block-title>Изменение фотографии</content-block-title>
        <content-block-text class="text">Загрузите вашу настоящую фотографию, чтобы пользователям было проще вас
            узнать</content-block-text>
        <div class="buttons">
            <form enctype="multipart/form-data" @submit.prevent="loadImage">
                <file-input-button @change="loadImage">Загрузить фото</file-input-button>
            </form>
            <modal-button class="cancel" @click="hideModal">Отмена</modal-button>
        </div>
    </modal-template>
    <modal-template v-if="modal == 'preview'">
        <div class="header">
            <div class="title">Аватар профиля</div>
            <icon-button icon-name="close" @click="hideModal"></icon-button>
        </div>
        <cropper ref="cropper" class="photo" :src="url" :stencil-component="stencil" />
        <div class="buttons">
            <modal-button @click="confirmChange">Сохранить</modal-button>
            <modal-button class="cancel" @click="returnToLoad">Назад</modal-button>
        </div>
    </modal-template>
</template>

<script>
import { BASE_URL } from '@/modules/ApiClient';
import { checkToken, getToken } from '@/modules/auth';
import { CircleStencil, Cropper } from 'vue-advanced-cropper';
import 'vue-advanced-cropper/dist/style.css';
import 'vue-advanced-cropper/dist/theme.classic.css';

export default {
    components: {
        Cropper
    },
    name: 'modal-change-avatar',
    props: ['name'],
    data() {
        return {
            modal: '',
            url: 'https://images.pexels.com/photos/1254140/pexels-photo-1254140.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940',
            stencil: CircleStencil,
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
            this.file = file;
            this.url = URL.createObjectURL(file);
            this.modal = 'preview';
        },
        returnToLoad() {
            this.modal = 'load';
        },
        hideModal() {
            this.modal = '';
        },
        async confirmChange() {
            const { canvas } = this.$refs.cropper.getResult();

            if (canvas) {
                const formData = new FormData();
                canvas.toBlob(async (blob) => {
                    formData.append('avatar', blob);

                    const response = await fetch(`${BASE_URL}:8081/profile/update-avatar`, {
                        method: 'PUT',
                        headers: {
                            'Authorization': 'Bearer ' + getToken(),
                        },
                        body: formData
                    });

                    console.log(response);
                    if (response.ok) {
                        await checkToken();
                        this.hideModal();
                        this.emitter.emit('confirm-change-avatar');
                    }
                });
            }

            // await apiClient.profile.updateAvatar(formData, () => {
            //     console.log('avatar changed');
            // })
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
    max-height: 400px;
    background: #444444;
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

.text {
    max-width: 500px;
    text-align: center;
}

form {
    width: 100%;
    max-width: 200px;
}
</style>
