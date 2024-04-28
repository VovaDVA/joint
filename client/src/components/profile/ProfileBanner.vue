<template>
    <div class="banner">
        <div class="banner-inner">
            <div class="banner-content">
                <filled-icon-button>Изменить обложку</filled-icon-button>
            </div>
            <user-avatar></user-avatar>
        </div>
        <div class="banner-promo">
            <div class="banner-ad">
            </div>
            <div class="banner-ad">
            </div>
        </div>
    </div>
    <!-- <profile-content></profile-content> -->
</template>

<script>
export default {
    name: 'profile-banner',
}
</script>

<style scoped>
.banner {
    display: flex;
    width: 100%;
}

.banner-inner {
    flex: 1;
    position: relative;
    height: 0;
    padding-top: 22%;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.banner-promo {
    flex: 0.3;
    /* height: 20vw; */
    /* padding-top: 22%; */
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    margin-left: 10px;
    /* border: 1px #ffffff7c solid; */
    border-radius: 20px;
}

.banner-ad {
    padding-top: calc(48% - 5px);
    margin-bottom: 10px;
    flex: 1;
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

    .banner-inner {
        aspect-ratio: 16/5;
        padding-top: 31%;
    }

    .banner * {
        margin: 0;
    }

    .banner-promo {
        flex-direction: row;
        margin-top: 80px;
    }

    .banner-ad {
        padding-top: 0;
        flex: 0 0 calc(50% - 5px);
        aspect-ratio: 16/9;
    }
}
</style>