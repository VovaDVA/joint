<template>
    <token-check></token-check>
    <content-block>
        <content-block-title>Зачем указывать свои данные?</content-block-title>
        <content-block-text>
            <p>Указывать свои данные необязательно.</p>

            <p>Персонал компании состоит всего из двух человек: отца и сына, которые захотели посвятить свое
                свободное время созданию чего-то нового и интересного. Решили создать свою полноценную
                браузерную стратегию, в которой смогли бы воплотить в жизнь то, чего нам не хватало в других
                играх: больше локаций, больше персонажей, больше юнитов, больше ресурсов и, самое главное,
                длинного и непредсказуемого сюжета о далеких мирах, о путешествии по незнакомым планетам, о
                дикой неизвестной природе.</p>
        </content-block-text>
    </content-block>

    <content-block>
        <content-block-title>Настройки</content-block-title>

        <form class="settings-form" enctype="multipart/form-data">
			<form-input v-model="firstName">Имя</form-input>
			<form-input v-model="firstName">Фамилия</form-input>
			<date-input v-model="firstName">Дата рождения</date-input>
			<form-input v-model="firstName">Страна</form-input>
			<form-input v-model="firstName">Город</form-input>
			<email-input v-model="firstName">Почта</email-input>
			<password-input v-model="firstName">Пароль</password-input>

            <div class="input_wrapper">
                <input type="submit" id="submitbtn" class="submit_btn" name="submit" value="Сохранить изменения" />
                <router-link to="/" class="settings_cancel">Отмена</router-link>
            </div>
        </form>
    </content-block>
</template>

<script></script>

<style scoped>
.settings-form {
    flex: 2;
    margin-right: 10px;
    max-width: 600px;
    margin: auto;
}

input,
.settings_cancel {
    line-height: 28px;
    font-size: 13px;
    border: 1px #ffffff solid;
    border-radius: 30px;
    max-height: 30px;
    margin: 5px 0 15px 10px;
    padding: 0 20px;
    width: 49%;
    text-align: center;
    color: #ffffff;

    transition: color, background .3s linear;
}

input {
    margin-left: 0;
}

.settings_cancel:hover {
    background: #ffffff;
    color: #000000;

    transition: color, background .3s linear;
}
</style>