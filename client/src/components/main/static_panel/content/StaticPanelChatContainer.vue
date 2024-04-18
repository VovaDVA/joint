<template>
    <component :is="currentSectionComponent" :key="currentSection" @open-chat="onOpenChat" @open-chat-list="onOpenChatList"/>
</template>

<script>
import StaticPanelChats from './StaticPanelChats.vue';
import StaticPanelMessenger from './StaticPanelMessenger.vue';

export default {
    components: {
        StaticPanelChats,
        StaticPanelMessenger,
    },
    data() {
        return {
            currentSection: 'chats',
        }
    },
    computed: {
        currentSectionComponent() {
            // Возвращаем компонент для текущего выбранного раздела
            switch (this.currentSection) {
                case 'messenger':
                    return 'StaticPanelMessenger';
                default:
                    return 'StaticPanelChats';
            } 
        }
    },
    methods: {
        changeSection(section) {
            this.currentSection = section;
        },
        onOpenChat() {
            this.currentSection = 'messenger';
        },
        onOpenChatList() {
            this.currentSection = 'chats';
        }
    }
}
</script>

<style scoped></style>