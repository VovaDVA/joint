<template>
    <token-check></token-check>
    <profile-banner></profile-banner>
    <div class="tabs" :class="$store.state.theme">
        <div class="tabs-container">
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
    </div>
    <profile-content></profile-content>
</template>

<script></script>

<style scoped>
.tabs {
    display: none;
    width: 100%;
    max-width: calc(100vw - 20px);
    height: fit-content;
    margin-top: 10px;
    padding: 10px;

    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
    overflow-x: scroll;
}

.tabs.light-theme {
    border: 1px #0000007c solid;
    background: #ebebeb;
}

.tabs-container {
    display: flex;
    gap: 10px;
    width: fit-content;
    min-width: 100%;
    align-items: center;
}

.tabs::-webkit-scrollbar {
    width: 0;
    display: none;
}

a {
    color: #ffffff;
}


@media (max-width: 1000px) {
    .tabs {
        display: block;
        margin-top: 40px;
    }
}

@media (max-width: 800px) {
    .tabs {
        margin-top: 10px;
    }
}
</style>