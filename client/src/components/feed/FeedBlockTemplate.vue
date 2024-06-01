<template>
    <div v-if="postDeleted" class="feed-block">
        <div class="post-deleted">
            <single-icon class="red" iconName="trash"></single-icon>
            <div>Пост удален</div>
        </div>
    </div>
    <div v-if="!postDeleted" class="feed-block" :class="$store.state.theme">
        <div class="header">
            <div class="header-user" @click="goToProfile">
                <user-avatar :photo="avatar"></user-avatar>
                <div class="author">
                    <div class="username">{{ author }}</div>
                    <div class="date">{{ post ? $formatDate(post.created_at) : '-' }}</div>
                </div>
            </div>

            <icon-button class="date toggle" icon-name="ellipsis-vertical" @click="toggleMenu"></icon-button>
        </div>
        <slot></slot>
        <div v-if="menuVisible && profile && isProfilePage()" class="menu" :class="$store.state.theme">
            <div class="menu-item">В избранное</div>
            <div class="menu-item">Закрепить</div>
            <div class="menu-item">Редактировать</div>
            <div class="menu-item red" @click="deletePost">Удалить</div>
        </div>
        <div v-if="menuVisible && (!isProfilePage() || !profile)" class="menu" :class="$store.state.theme">
            <div class="menu-item">В избранное</div>
            <div class="menu-item">Скрыть</div>
            <div class="menu-item">Пожаловаться</div>
        </div>
    </div>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { getUserById, getUserId } from '@/modules/auth';

export default {
    name: 'feed-block-template',
    props: ['post'],
    data() {
        return {
            avatar: '',
            menuVisible: false,
            profile: false,
            postDeleted: false,
            author: '-'
        }
    },
    async mounted() {
        if (this.post) {
            this.profile = this.post.author_id == getUserId();

            try {
                const user = await getUserById(this.post.author_id);
                this.avatar = user.avatar;
                this.author = user.firstName + ' ' + user.lastName;
            } catch (error) {
                console.error("Error fetching user data:", error);
            }
        }
    },
    methods: {
        async deletePost() {
            await apiClient.content.deletePost({
                postId: this.post._id,
            }, () => {
                this.postDeleted = true;
            });
        },
        showMenu() {
            this.menuVisible = true;
        },
        hideMenu() {
            this.menuVisible = false;
        },
        toggleMenu() {
            this.menuVisible = !this.menuVisible;
        },
        isProfilePage() {
            return this.$route.path == '/';
        },
        goToProfile() {
            this.$router.push('/');
        }
    }
}
</script>

<style scoped>
.feed-block {
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 10px;
    height: fit-content;
    padding: 10px;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-block.light-theme {
    border: 1px #0000007c solid;
    background: rgba(255, 255, 255, 0.5);
    color: #000;
}

.header,
.header-user {
    height: 45px;
    display: flex;
    gap: 10px;
}

.header {
    justify-content: space-between;
}

.header-user {
    padding-right: 20px;
    border-radius: 50px;
    transition: background .2s linear;
}

.header-user:hover {
    cursor: pointer;
    background: #ffffff1f;
    transition: background .2s linear;
}

.feed-block.light-theme .header-user:hover {
    background: #0000001f;
}

.author {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    font-size: 15px;
}

.date {
    font-size: 12px;
    color: #ffffff7c;
}

.toggle {
    font-size: 15px;
}

.feed-block.light-theme .date {
    color: #0000007c;
}

.menu {
    display: flex;
    flex-direction: column;
    gap: 5px;
    position: absolute;
    top: 10px;
    right: 40px;
    padding: 5px;
    font-size: 15px;
    text-align: left;

    border-radius: 10px;

    background: #252d3a;
    box-shadow: 0 0 30px #0000002d;
}

.menu.light-theme {
    background: #ffffff;
}

.menu-item {
    padding: 10px;
    border-radius: 5px;
    transition: background .2s linear;
}

.menu-item:hover {
    background: #ffffff17;
    cursor: pointer;
    transition: background .2s linear;
}

.menu.light-theme .menu-item:hover {
    background: #00000017;
}

.red {
    color: #ff6d6d;
}

.post-deleted {
    display: flex;
    flex-direction: column;
    gap: 20px;
    text-align: center;
    padding: 20px;
    font-size: 25px;
}

.post-deleted .icon {
    font-size: 30px;
}


@media (max-width: 500px) {

    .header,
    .header-user {
        height: 40px;
    }

    .author {
        font-size: 15px;
    }
}
</style>