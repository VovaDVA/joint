<template>
    <div class="modal-change-avatar">
        <div class="window">
            <div class="content">
                <div class="head">
                    Фотография на вашей странице
                </div>
                <icon-button icon-name="close"></icon-button>
            </div>

            <div class="content">

                <div class="photo">
                    <div class="square">

                    </div>
                </div>
            </div>
            <div class="buttons">
                <div class="block">
                    <div class="button" id="load">
                        Загрузить фото
                    </div>
                    <div class="button">
                        Сохранить
                    </div>
                </div>
                <div class="block">

                    <div class="button" id="back">
                        Назад
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'modal-change-avatar',
    props: ['name'],
}
</script>

<style scoped>
.modal-change-avatar {
    display: flex;
    position: fixed;
    top: 0px;
    left: 0px;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.512);
    border-radius: 15px;
    transition: background .2s linear;
}

.window {
    display: flex;
    margin: auto;
    border: 1px #ffffff2f solid;
    background: rgb(36, 43, 54);
    border-radius: 20px;
    flex-direction: column;
    padding: 5px;
}

.content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 5px 10px 5px 10px;
    font-size: 15px;


}

.head {
    font-size: 15px;
}

.photo {
    display: flex;
    align-items: center;
    height: 360px;
    width: 400px;
    background-color: rgba(0, 0, 0, 0.437);
    border-radius: 15px;
}

.buttons {
    display: flex;
    align-items: center;
    margin: 5px 10px 5px 10px;
    font-size: 15px;
    justify-content: space-between;
}

.block {
    display: flex;
}

.button {
    padding: 5px 10px 5px 10px;
    margin: 5px;
    border: 1px #ffffff2f solid;
    border-radius: 100px;
    background: rgb(238, 238, 238);
    color: rgb(11, 10, 10);
}

#back {

    color: rgb(239, 237, 237);
    background: rgb(66, 66, 76);
    margin: 5px 0px 5px 50px;
}

#load {
    margin: 5px 5px 5px 0px;
}

.square {
    width: 300px;
    height: 300px;
    margin: auto;
    border: 1px #adadad8a solid;
}
</style>
