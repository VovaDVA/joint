<template>
    <div class="static-panel">
        <component :is="currentSectionComponent" :key="currentSection" />
    </div>
    <div class="icon-container left">
        <router-link to="/">
            <filled-icon-button icon-name="house">Моя страница</filled-icon-button>
        </router-link>
        <router-link to="/feed">
            <filled-icon-button icon-name="newspaper">Лента</filled-icon-button>
        </router-link>
        <router-link to="/calendar">
            <filled-icon-button icon-name="calendar">Календарь</filled-icon-button>
        </router-link>
    </div>
    <div class="icon-container profile" v-if="isProfilePage">
        <router-link to="/">
            <filled-icon-button icon-name="user">Об авторе</filled-icon-button>
        </router-link>
        <router-link to="/images">
            <filled-icon-button icon-name="image">Изображения</filled-icon-button>
        </router-link>
        <router-link to="/videos">
            <filled-icon-button icon-name="video">Видео</filled-icon-button>
        </router-link>
        <router-link to="/music">
            <filled-icon-button icon-name="music">Музыка</filled-icon-button>
        </router-link>
        <router-link to="/books">
            <filled-icon-button icon-name="book">Книги</filled-icon-button>
        </router-link>
    </div>
    <div class="icon-container">
        <filled-icon-button icon-name="bell" @click="changeSection('notifications')">Уведомления</filled-icon-button>
        <filled-icon-button icon-name="user-group" @click="changeSection('people')">Люди</filled-icon-button>
        <filled-icon-button icon-name="message" @click="changeSection('chat')">Мессенджер</filled-icon-button>
        <filled-icon-button icon-name="image" @click="changeSection('images')">Изображения</filled-icon-button>
        <filled-icon-button icon-name="video" @click="changeSection('videos')">Видео</filled-icon-button>
        <filled-icon-button icon-name="music" @click="changeSection('music')">Музыка</filled-icon-button>
        <filled-icon-button icon-name="book" @click="changeSection('books')">Книги</filled-icon-button>
    </div>
</template>

<script>
import StaticPanelPeople from './content/StaticPanelPeople.vue';
import StaticPanelChat from './content/StaticPanelChat.vue';
import StaticPanelMessenger from './content/StaticPanelMessenger.vue';
import StaticPanelImages from './content/StaticPanelImages.vue';
import StaticPanelMusic from './content/StaticPanelMusic.vue';
import StaticPanelNotifications from './content/StaticPanelNotifications.vue';

export default {
    name: 'side-chat',
    components: {
        StaticPanelPeople,
        StaticPanelChat,
        StaticPanelMessenger,
        StaticPanelImages,
        StaticPanelMusic,
        StaticPanelNotifications,
    },
    data() {
        return {
            currentSection: 'chat',
            socket: null,
            messages: [],
            newMessage: ''
        }
    },
    computed: {
        isProfilePage() {
            return ['/', '/user-about', '/images', '/videos', '/music', '/books'].includes(this.$route.path);
        },

        currentSectionComponent() {
            // Возвращаем компонент для текущего выбранного раздела
            switch (this.currentSection) {
                case 'notifications':
                    return 'StaticPanelNotifications';
                case 'people':
                    return 'StaticPanelPeople';
                case 'chat':
                    return 'StaticPanelChat';
                case 'images':
                    return 'StaticPanelImages';
                case 'videos':
                    return 'StaticPanelImages';
                case 'music':
                    return 'StaticPanelMusic';
                case 'books':
                    return 'StaticPanelImages';
                default:
                    return 'StaticPanelMessenger';
            }
        }
    },
    mounted() {
        // this.socket = io('http://127.0.0.1:3000');
        // this.socket.on('chat message', (msg) => {
        //     this.messages.push({ id: this.messages.length + 1, text: msg });
        // });
    },
    methods: {
        changeSection(section) {
            this.currentSection = section;
        }
    }
}
</script>

<style scoped>
.static-panel {
    display: flex;
    flex-direction: column;
    padding: 10px;

    position: fixed;
    top: 70px;
    right: 0;
    width: 30vw;
    height: calc(100vh - 90px);
    margin: 10px;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.5);
}

.icon-container {
    width: 35px;
    position: fixed;
    top: 80px;
    right: calc(30vw + 20px);
    margin-top: 10px;
    display: flex;
    justify-content: flex-start;
    align-items: start;
    flex-direction: column;
}

.icon-container.left {
    left: 10px;
}

.icon-container.profile {
    left: 10px;
    top: 40vh;
}

.icon-container .filled-icon-button {
    margin-bottom: 10px;
}

a {
    color: white;
}
</style>