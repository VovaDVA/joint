<template>
    <static-panel-header>
        <messenger-action-icon icon-name="arrow-left"></messenger-action-icon>
    </static-panel-header>
    <chat-title>Владимир Двойнишников</chat-title>
    <static-panel-content>
        <single-message class="you">Привет</single-message>
        <single-message>Привет</single-message>
        <single-message class="you">Как дела?</single-message>
        <single-message>Нормально, твои как?</single-message>
        <single-message class="you">Хорошо, чем занимаешься?</single-message>
        <single-message>Пишу микросервис авторизации для учебного проекта</single-message>
        <single-message class="you">Интересно, что за проект?</single-message>
        <single-message>Соцсеть</single-message>
        <single-message class="you">О, круто. А в чем ее преимущество?</single-message>
        <single-message>Она дает пользователям зарабатывать на созданном ими же контенте</single-message>
        <single-message class="you">Прикольно, а какой контент можно будет публиковать?</single-message>
        <single-message>Разный. Например, видео, изображения, музыку, книги и тд.</single-message>
        <single-message class="you">Я уже хочу зарегистрироваться</single-message>
        <single-message>Мы только начали работу, но получится очень классно</single-message>
        <single-message class="you">Буду ждать релиза</single-message>
        <single-message>Я сообщу об этом</single-message>
        <single-message class="you">Отлично, тогда до встречи</single-message>
        <single-message>Давай, удачи</single-message>
        <single-message class="you">Пока</single-message>
    </static-panel-content>
    <div class="chat-input">
        <!-- <messenger-action-icon icon-name="file"></messenger-action-icon> -->
        <messenger-action-icon icon-name="face-smile"></messenger-action-icon>
        <input class="message-input" type="text" placeholder="Напишите сообщение...">
        <messenger-action-icon class="right" icon-name="paper-plane"></messenger-action-icon>
        <messenger-action-icon class="right" icon-name="microphone"></messenger-action-icon>
    </div>
</template>

<style scoped>
.chat-input {
    flex: 0 0 50px;
    margin-top: 10px;
    display: flex;
}

.message-input {
    flex: 1;
    padding: 10px 20px;
    font-size: 15px;
    border: 1px #ffffff7c solid;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
    outline: none;
}
</style>