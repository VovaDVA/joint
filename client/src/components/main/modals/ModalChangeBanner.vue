<template>
    <modal-template v-if="modal == 'load'">
        <content-block-title>Изменение обложки</content-block-title>
        <content-block-text class="text">Привлекательная обложка в профиле поможет вам выделиться среди других
            пользователей Joint</content-block-text>
        <div class="buttons">
            <form enctype="multipart/form-data" @submit.prevent="loadImage">
                <file-input-button @change="loadImage">Загрузить фото</file-input-button>
            </form>
            <modal-button class="cancel" @click="hideModal">Отмена</modal-button>
        </div>
    </modal-template>
    <modal-template v-if="modal == 'preview'">
        <div class="header">
            <div class="title">Обложка профиля</div>
            <icon-button icon-name="close" @click="hideModal"></icon-button>
        </div>
        <cropper ref="cropper" class="photo" :src="url" :stencil-props="{
            aspectRatio: 77 / 22,
            resizable: false,
        }" :default-size="defaultSize" />
        <div class="buttons">
            <modal-button @click="confirmChange">Сохранить</modal-button>
            <modal-button class="cancel" @click="returnToLoad">Назад</modal-button>
        </div>
    </modal-template>
</template>

<script>
import { BASE_URL } from '@/modules/ApiClient';
import { checkToken, getToken } from '@/modules/auth';
import { Cropper } from 'vue-advanced-cropper';
import 'vue-advanced-cropper/dist/style.css';
import 'vue-advanced-cropper/dist/theme.classic.css';

export default {
    name: 'modal-change-banner',
    props: ['name'],
    components: {
        Cropper
    },
    data() {
        return {
            modal: '',
            url: 'https://images.pexels.com/photos/1254140/pexels-photo-1254140.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940',
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
        defaultSize({ imageSize, visibleArea }) {
            return {
                width: (visibleArea || imageSize).width,
                height: (visibleArea || imageSize).height,
            };
        },
        async confirmChange() {
            const { canvas } = this.$refs.cropper.getResult();

            if (canvas) {
                const formData = new FormData();
                canvas.toBlob(async (blob) => {
                    formData.append('banner', blob);

                    const response = await fetch(`${BASE_URL}:8081/profile/update-banner`, {
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
                        this.emitter.emit('confirm-change-banner');
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
    max-width: 100%;
    background-color: rgba(0, 0, 0, 0.437);
    border-radius: 15px;
}

.photo {
    width: 100%;
    max-width: 1000px;
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