<template>
    <div class="news-block">
        <div class="news-block-inner">
            <div class="news-info">
                <div class="news-title">Важное обновление!</div>
                <p>Это очень важная носовть, которую нужно обязательно всем рассказать. Это очень важная носовть,
                    которую нужно обязательно всем рассказать. Это очень важная носовть, которую нужно обязательно всем
                    рассказать.
                </p>
                <img src="../../assets/bg.jpg" alt="Изображение не загрузилось">
                <p>Это очень важная носовть, которую нужно обязательно всем рассказать. Это очень важная носовть,
                    которую нужно обязательно всем рассказать. Это очень важная носовть, которую нужно обязательно всем
                    рассказать.
                </p>
                <p>Это очень важная носовть, которую нужно обязательно всем рассказать. Это очень важная носовть,
                    которую нужно обязательно всем рассказать. Это очень важная носовть, которую нужно обязательно всем
                    рассказать.
                </p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'news-block',
}
</script>

<style scoped>
.news-block {
    display: flex;
    width: 100%;
    height: fit-content;
    margin-bottom: 20px;
}

.news-preview {
    flex: 0 0 24vw;
    margin-right: 10px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.news-block-inner {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.news-info {
    text-align: center;
    position: relative;
    flex: 1;
    height: calc(100% - 45px);
    margin-bottom: 10px;
    padding: 20px 30px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}

.news-title {
    text-align: center;
    color: #ffbf6c;
    font-size: 22px;
    margin-bottom: 10px;
    padding: 10px;

    border-bottom: 1px solid transparent;
    border-image: radial-gradient(#ffbf6c 50%, transparent);
    border-image-slice: 1;
}

p {
    text-align: justify;
    font-size: 15px;
}

img {
    margin: 10px auto;
    width: 70%;
    height: auto;
    border-radius: 15px;
}

.news-stats {
    display: flex;
    /* justify-content: flex-end; */
}

.news-stats * {
    margin-right: 10px;
}
</style>