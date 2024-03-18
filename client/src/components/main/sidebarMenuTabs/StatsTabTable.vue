<template>
    <table>
        <thead>
            <tr>
                <td>ID</td>
                <td>Категория</td>
                <td>Описание</td>
                <td>Дата создания</td>
                <td>Потрачено</td>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(spending, index) in spendings" :key="index">
                <td>{{ spending.Id }}</td>
                <td>{{ spending.Category }}</td>
                <td>{{ spending.Description }}</td>
                <td>{{ formatDate(spending.createdAt) }}</td>
                <td>{{ spending.Spent }}</td>
            </tr>
        </tbody>
    </table>
</template>

<script>
import { getToken } from '../../../modules/auth';
import moment from 'moment';

export default {
    data() {
        return {
            spendings: [],
        };
    },
    created() {
        this.getData();
    },
    methods: {
        async getData() {
            try {
                const response = await fetch('/spetr/getSpendings/all', {
                    headers: {
                        'Authorization': 'Bearer ' + getToken(),
                        'Content-Type': 'application/json'  // Пример добавления других заголовков
                    },
                });

                const data = await response.json();
                console.log(data);

                if (data['status'] == '200') {
                    this.spendings = data.item;
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
table {
    margin-top: 30px;
    width: 100%;
    border-collapse: collapse;
    border-spacing: 0;
    position: relative;

    z-index: 2;
}

thead td {
    color: #71FF90;
}

tbody {
    display: table-row-group;
    vertical-align: middle;
    border-color: inherit;
}

tbody tr {
    border: 2px solid #8A8A8A;
    border-width: 0 0 1px;
    display: table-row;
    vertical-align: inherit;
}

table td {
    font-weight: 400;
    padding: 0.5em 0.75em;
    vertical-align: top;
}

table tr:nth-last-child(-n+1) {
    border-width: 0;
}

table tr td:first-child {
    color: #ffc59b;
}
</style>
  
  
  