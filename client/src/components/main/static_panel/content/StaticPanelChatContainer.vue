<template>
    <component :is="currentSectionComponent" :key="currentSection" :chat="selectedChat" @open-chat="onOpenChat"
        @open-chat-list="onOpenChatList" />
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
            selectedChat: null,
        }
    },
    mounted() {
        this.emitter.on('open-messenger', (data) => {
            this.onOpenChat(data);
        });
    },
    computed: {
        currentSectionComponent() {
            // this.currentSection = 'chats';
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
        onOpenChat(chat) {
            console.log('chat opened')
            this.selectedChat = chat;
            this.currentSection = 'messenger';
        },
        onOpenChatList() {
            this.currentSection = 'chats';
            this.emitter.emit('close-chat');
        }
    }
}
</script>

<style scoped></style>