<template>
    <token-check></token-check>
    <div class='settings_page'>
        <page-title>Настройки профиля</page-title>

        <div class="settings_inner">
            <form id="settings_form" style="max-width: 500px" enctype="multipart/form-data">

                <form-block>
                    <username-text-field />
                </form-block>
                <form-block>
                    <fullname-text-field />
                </form-block>
                <form-block>
                    <date-text-field>Дата рождения</date-text-field>
                </form-block>
                <form-block>
                    <country-text-field />
                </form-block>
                <form-block>
                    <city-text-field />
                </form-block>
                <form-block>
                    <email-text-field /></form-block>
                <form-block>
                    <password-text-field>Пароль</password-text-field>
                </form-block>

                <div class="input_wrapper">
                    <input type="submit" id="submitbtn" class="submit_btn" name="submit" value="Сохранить изменения" />
                    <router-link to="/" class="settings_cancel">Отмена</router-link>
                </div>

            </form>

            <div class="settings_about">
                <content-block-title>Зачем указывать свои данные?</content-block-title>
                <div class="user_about_content">
                    <content-block-text>
                        <p>Указывать свои данные необязательно.</p>

                        <p>Персонал компании состоит всего из двух человек: отца и сына, которые захотели посвятить свое
                            свободное время созданию чего-то нового и интересного. Решили создать свою полноценную
                            браузерную стратегию, в которой смогли бы воплотить в жизнь то, чего нам не хватало в других
                            играх: больше локаций, больше персонажей, больше юнитов, больше ресурсов и, самое главное,
                            длинного и непредсказуемого сюжета о далеких мирах, о путешествии по незнакомым планетам, о
                            дикой неизвестной природе.</p>
                    </content-block-text>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script></script>
  
<style scoped>
.settings_page {
    width: 100%;
    margin-top: -100px;
    margin-bottom: 100px;
}

.settings_about {
    height: fit-content;
    min-height: 140px;
    padding: 10px 20px 50px;

    border-radius: 30px;
    background: rgba(0, 0, 0, 0.5);

    position: relative;
}

.settings_inner {
    display: flex;
    justify-content: space-between;
}

#settings_form {
    width: 45%;
    padding: 20px;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 30px;
}

.settings_about {
    width: 50%;
}

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

.settings_cancel:hover {
    background: #ffffff;
    color: #000000;

    transition: color, background .3s linear;
}

#settings_inner {
    display: flex;
}
</style>
  
  
  