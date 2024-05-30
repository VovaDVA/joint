<template>
    <!-- Bottom buttons panel (screen width < 1000px) **At the top so that static-panel can be affected in CSS** -->
    <div class="bottom-container" :class="[{ 'active': isBottomActive, 'hidden': bottomHidden }, $store.state.theme]">
        <div class="bottom-inner">
            <router-link to="/feed" class="nav-item" :class="{ selected: selectedTab === 0 }"
                @click="$store.commit('hideStaticPanel')">
                <icon-button icon-name="home"></icon-button>
            </router-link>
            <icon-button icon-name="user-group" @click="changeSection('people')"></icon-button>
            <icon-button icon-name="message" @click="changeSection('chat')"></icon-button>
            <icon-button icon-name="bell" @click="changeSection('notifications')"></icon-button>
            <icon-button icon-name="grip" @click="toggleBottom()"></icon-button>
        </div>
        <div class="bottom-menu">
            <filled-icon-button icon-name="image" @click="changeSection('images')">Изображения</filled-icon-button>
            <filled-icon-button icon-name="video" @click="changeSection('videos')">Видео</filled-icon-button>
            <filled-icon-button icon-name="music" @click="changeSection('music')">Музыка</filled-icon-button>
            <filled-icon-button icon-name="book" @click="changeSection('books')">Книги</filled-icon-button>
        </div>
    </div>

    <!-- Static Panel -->
    <div class="static-panel" :class="[{ 'active': $store.state.staticPanelVisible }, $store.state.theme]">
        <component :is="currentSectionComponent" :key="currentSection" />
    </div>
    <div class="icon-container left">
        <router-link to="/feed">
            <filled-icon-button icon-name="house">Главная</filled-icon-button>
        </router-link>
        <!-- <router-link to="/feed">
            <filled-icon-button icon-name="newspaper">Лента</filled-icon-button>
        </router-link> -->
        <router-link to="/calendar">
            <filled-icon-button icon-name="calendar">Календарь</filled-icon-button>
        </router-link>
    </div>
    <div class="icon-container profile" v-if="isProfilePage">
        <router-link to="/post-creation">
            <filled-icon-button icon-name="plus">Создать пост</filled-icon-button>
        </router-link>
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
import StaticPanelMessenger from './content/StaticPanelMessenger.vue';
import StaticPanelImages from './content/StaticPanelImages.vue';
import StaticPanelMusic from './content/StaticPanelMusic.vue';
import StaticPanelNotifications from './content/StaticPanelNotifications.vue';
import StaticPanelChatContainer from './content/StaticPanelChatContainer.vue';

export default {
    name: 'static-panel',
    props: ['isActive'],
    components: {
        StaticPanelPeople,
        StaticPanelChatContainer,
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
            newMessage: '',
            isBottomActive: false,
            bottomHidden: false,
        }
    },
    created() {
        this.emitter.on('create-chat', (data) => {
            this.currentSection = 'chat';
            setTimeout(this.emitter.emit('open-messenger', data), 100);
        });

        this.emitter.on('open-messenger', () => {
            this.bottomHidden = true;
        });

        this.emitter.on('close-chat', () => {
            this.bottomHidden = false;
        });
    },
    computed: {
        isProfilePage() {
            return ['/', '/user-about', '/images', '/videos', '/music', '/books'].includes(this.$route.path);
        },

        currentSectionComponent() {
            switch (this.currentSection) {
                case 'notifications':
                    return 'StaticPanelNotifications';
                case 'people':
                    return 'StaticPanelPeople';
                case 'chat':
                    return 'StaticPanelChatContainer';
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
    methods: {
        changeSection(section) {
            this.currentSection = section;
            this.$store.commit('showStaticPanel');
            this.bottomHidden = false;
        },
        toggleBottom() {
            this.isBottomActive = !this.isBottomActive;
        }
    },
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
    height: calc(100% - 90px);
    margin: 10px;
    z-index: 30;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.5);
}

.static-panel.light-theme {
    border: 1px #0000007c solid;
    background-color: #ebebeb;
}

.static-panel.active {
    display: flex;
}

.icon-container {
    width: 35px;
    position: fixed;
    top: 80px;
    right: calc(30vw + 20px);
    margin-top: 10px;
    display: flex;
    gap: 10px;
    justify-content: flex-start;
    align-items: start;
    flex-direction: column;
    z-index: 31;
}

.icon-container.left {
    left: 10px;
}

.icon-container.profile {
    left: 10px;
    top: 40vh;
}

.bottom-container {
    display: none;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: fit-content;
    min-height: 70px;

    position: fixed;
    bottom: -50px;
    right: 0;
    left: 0;
    margin: auto;
    z-index: 50;

    border-top: 1px solid #ffffff7c;
    border-radius: 25px 25px 0 0;

    background: #000000;
    transition: bottom .2s ease-out;
}

.bottom-container.active {
    bottom: 0;
    transition: bottom .2s ease-out;
}

.bottom-container.light-theme {
    border-top: 1px solid #0000007c;
    background: #fff;
}

.bottom-inner,
.bottom-menu {
    display: flex;
    justify-content: space-around;
    width: 100%;
    max-width: 300px;
    margin-top: 15px;
}

.bottom-menu {
    margin-bottom: 15px;
}

.icon-button {
    font-size: 20px;
}

a {
    color: white;
}


@media (max-width: 1300px) {
    .static-panel {
        display: none;
    }

    .icon-container {
        right: 10px;
    }
}

@media (max-width: 1000px) {
    .static-panel {
        position: fixed;
        top: 70px;
        right: 0;
        left: 0;
        margin: auto;

        padding-bottom: 80px;

        width: 100%;
        height: calc(100% - 70px);
        background: #2d3844;
        border: none;
        border-radius: 0;
    }

    .static-panel.light-theme {
        border: none;
    }

    .icon-container {
        display: none;
    }

    .bottom-container {
        display: flex;
    }

    .bottom-container.hidden {
        display: none;
    }

    .bottom-container.hidden+.static-panel {
        padding-bottom: 10px;
    }
}
</style>