<template>
    <page-content class="stats-content">
        <template v-slot:title>Добавление статьи расходов</template>
        <form @submit.prevent="addExpense">
            <form-block>
                <input-text-field :value="category" @input="category = $event" inputId="category">Категория</input-text-field>
            </form-block>
            <form-block>
                <input-text-field :value="description" @input="description = $event" inputId="description">Описание</input-text-field>
            </form-block>
            <form-block>
                <number-text-field :value="spent" @input="spent = $event" inputId="spent">Сумма трат</number-text-field>
            </form-block>
            <form-block>
                <date-text-field :value="creationDate" @input="creationDate = $event" inputId="creationDate">Дата и время траты</date-text-field>
            </form-block>

            <input type="submit" id="submitbtn" class="submit_btn" name="submit" value="Добавить статью расходов" />
        </form>
    </page-content>
</template>

<script>
import { getToken } from '../../../modules/auth';
import moment from 'moment';

export default {
    data() {
        return {
            category: '',
            description: '',
            spent: '',
            creationDate: '',
        };
    },
    methods: {
        async addExpense() {
            try {
                const response = await fetch('/spetr/addSpentItem', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + getToken(),
                        'Content-Type': 'application/json'  // Пример добавления других заголовков
                    },
					body: JSON.stringify({
						category: this.category,
						description: this.description,
                        spent: parseFloat(this.spent),
                        create_date: moment(this.creationDate + ':00Z').format("YYYY-MM-DDTHH:mm:ssZ"),
					})
                });

                const data = await response.json();
                // console.log(data);

                if (data['status'] == '200') {
                    console.log('Статья расходов успешно добвалена');
                } else {
                    alert(data['message']);
                }

            } catch (error) {
                console.error(error);
            }
        },
        formatDate(date) {
            return moment(date).locale('ru').format('LL LT');
        }
    }
};
</script>

<style scoped>
form {
    margin-top: 20px;
}
</style>
  