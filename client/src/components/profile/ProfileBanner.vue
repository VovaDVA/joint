<template>
    <div class="banner" :class="$store.state.theme">
        <div class="banner-main">
            <div class="banner-inner">
                <img :src="url" alt="">
                <div class="banner-content">
                    <filled-icon-button @click="change">Изменить обложку</filled-icon-button>
                </div>
            </div>
            <user-info-banner></user-info-banner>
        </div>
        <div class="banner-promo">
            <div class="banner-ad"></div>
            <div class="banner-ad"></div>
        </div>
    </div>
</template>

<script>
import { getUserBanner } from '@/modules/auth';

export default {
    name: 'profile-banner',
    data() {
        return {
            url: null,
            // url: 'https://images.pexels.com/photos/1254140/pexels-photo-1254140.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940',
        }
    },
    mounted() {
        this.url = getUserBanner();

        this.emitter.on('confirm-change-banner', () => {
            this.url = getUserBanner();
        });
    },
    methods: {
        change() {
            this.emitter.emit('request-change-banner');
        }
    }
}
</script>

<style scoped>
.banner {
    display: flex;
    gap: 10px;
    width: 100%;
}

.banner-main {
    position: relative;
    flex: 1;
}

.banner-inner {
    aspect-ratio: 77/22 !important;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
    overflow: hidden;
}

.banner.light-theme .banner-inner {
    border: 1px #0000007c solid;
    background: #fff;
}

.banner.light-theme .banner-ad {
    border: 1px #0000007c solid;
    background: #fff;
}

.banner-promo {
    flex: 0 1 calc(22% - 8px);
    height: calc(100% - 40px);
    display: flex;
    gap: 10px;
    justify-content: space-between;
    flex-direction: column;
    border-radius: 20px;
}

.banner-ad {
    flex: 1;
    aspect-ratio: 2;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.banner-content {
    position: absolute;
    top: 0;
    right: 0;
    padding: 10px;
}

.banner img {
    width: 100%;
    height: 100%;
}

.icon_btn.change_banner {
    margin: 10px 10px 0 auto;
}

.icon_btn span {
    padding: 9px 0 9px 15px;
}


@media (max-width: 800px) {
    .banner {
        width: calc(100% + 20px);
        padding: 10px;
        margin-left: -10px;
        margin-top: -10px;

        flex-direction: column;
        border-bottom: 1px #ffffff7c solid;
        border-radius: 0 0 20px 20px;
        background: rgba(0, 0, 0, 0.5);
    }

    .banner.light-theme {
        border-bottom: 1px #0000007c solid;
        background: #ebebeb;
    }

    .banner-inner {
        aspect-ratio: 16/5 !important;
    }

    .banner * {
        margin: 0;
    }

    .banner-promo {
        flex-direction: row;
        margin-top: 20px;
    }

    .banner-ad {
        padding-top: 0;
        flex: 0 0 calc(50% - 5px);
        aspect-ratio: 16/9;
    }
}
</style>