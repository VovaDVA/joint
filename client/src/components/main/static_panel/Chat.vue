<template>
    <div class="chat">
        <div class="chat-inner">
            <component :is="currentSectionComponent" :key="currentSection" />
            <!-- <div class="chat-block search-bar"></div>
            <div class="chat-block chat-content"></div>
            <div class="chat-block chat-input"></div> -->
        </div>
    </div>
    <div class="icon-container left">
        <router-link to="/">
            <filled-icon-button icon-name="house">Моя страница</filled-icon-button>
        </router-link>
        <router-link to="/feed">
            <filled-icon-button icon-name="newspaper">Лента</filled-icon-button>
        </router-link>
        <router-link to="/profile-settings">
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
        <filled-icon-button icon-name="user-group" @click="changeSection('people')">Люди</filled-icon-button>
        <filled-icon-button icon-name="message" @click="changeSection('chat')">Мессенджер</filled-icon-button>
        <filled-icon-button icon-name="image">Изображения</filled-icon-button>
        <filled-icon-button icon-name="video">Видео</filled-icon-button>
        <filled-icon-button icon-name="music">Музыка</filled-icon-button>
        <filled-icon-button icon-name="book">Книги</filled-icon-button>
    </div>
</template>

<script>
import StaticPanelPeople from './content/StaticPanelPeople.vue';
import StaticPanelMessenger from './content/StaticPanelMessenger.vue';

export default {
    name: 'side-chat',
    components: {
        StaticPanelPeople,
        StaticPanelMessenger,
    },
    data() {
        return {
            currentSection: 'chat'
        }
    },
    computed: {
        isProfilePage() {
            return ['/', '/user-about', '/images', '/videos', '/music', '/books'].includes(this.$route.path);
        },

        currentSectionComponent() {
            // Возвращаем компонент для текущего выбранного раздела
            switch (this.currentSection) {
                case 'people':
                    return 'StaticPanelPeople';
                case 'chat':
                    return 'StaticPanelMessenger';
                default:
                    return 'StaticPanelMessenger';
            }
        }
    },
    methods: {
        changeSection(section) {
            this.currentSection = section;
        }
    }
}
</script>

<style scoped>
.chat {
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

.chat-inner {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    padding: 10px;
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