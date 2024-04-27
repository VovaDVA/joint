Здесь надо сделать шаблон для всех модальных окон с общими стилями
Выравнивание по центру экрана, стили прямоугольника и тд.
В других двух скриптах уже сделаешь содержимое и стили конкретно под их цели
<template>
    <div class="modal"> 
    <div class="window">
        <div class="content">
            <div class="head">
            Заголовок
             </div>
             <icon-button icon-name="close"></icon-button>
        </div>
       
        <div class="content">    
            <div class="photo">
                <!-- <div class="square">
                </div> -->
             </div>
        </div>
        <div class="content">
            <div class="block-buttons" >
                <div class="button">
                    Кнопка 1
                </div>
                <div class="button">
                    Кнопка 2
                </div> 
            </div>
            <div class="block-buttons" >    
                <div class="button">
                Кнопка 3
                </div>
            </div>        
        </div>
    </div>
    </div>
</template>

<style scoped>
.modal {
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
.photo{
    display: flex;
    align-items: center;
    height: 360px;
    width: 640px;
    background-color: rgba(0, 0, 0, 0.437);
    border-radius: 15px;
}
.block-buttons{
    display: flex;
}
.button{
    padding: 5px 10px 5px 10px;
    margin: 5px;
    border: 1px #ffffff2f solid;
    border-radius: 100px;
    background: rgb(238, 238, 238);
    color: rgb(11, 10, 10);
}
.square{
    width: 99%;
    height: 200px;
    margin: auto;
    border: 1px #adadad8a solid;
}
</style>