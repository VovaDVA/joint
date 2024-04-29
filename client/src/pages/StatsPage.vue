<template>
    <token-check></token-check>
    <content-block>
        <content-block-title>Статистика профиля</content-block-title>
        <content-block-text>
            <table :class="$store.state.theme">
                <tbody>
                    <tr>
                        <td>Посты</td>
                        <td>15</td>
                    </tr>
                    <tr>
                        <td>Комментарии</td>
                        <td>15</td>
                    </tr>
                    <tr>
                        <td>Лайки</td>
                        <td>15</td>
                    </tr>
                    <tr>
                        <td>Просмотры</td>
                        <td>15</td>
                    </tr>
                    <tr>
                        <td>Репосты</td>
                        <td>15</td>
                    </tr>
                    <tr>
                        <td>Подписчики</td>
                        <td>15</td>
                    </tr>
                    <tr>
                        <td>Друзья</td>
                        <td>15</td>
                    </tr>
                    <!-- <tr v-for="(spending, index) in spendings" :key="index">
                        <td>{{ spending.Id }}</td>
                        <td>{{ spending.Category }}</td>
                        <td>{{ spending.Description }}</td>
                        <td>{{ formatDate(spending.createdAt) }}</td>
                        <td>{{ spending.Spent }}</td>
                    </tr> -->
                </tbody>
            </table>
            <bar-chart></bar-chart>
            <LineChart></LineChart>

        </content-block-text>
    </content-block>
</template>

<script>
import LineChart from '@/components/main/LineChart.vue';
export default {
    components: {
        LineChart,
    }
}
</script>

<style scoped>
.content-block-text {
    display: flex;
    flex-wrap: wrap;
}

table {
    margin-top: 30px;
    flex: calc(100%/3);
    width: 100%;
    /* max-width: 300px; */
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
    text-align: right;
}

table tr:nth-last-child(-n+1) {
    border-width: 0;
}

table tr td:first-child {
    color: #ffbf6c;
    text-align: left;
}

table.light-theme tr td:first-child {
    color: #965000;
}
</style>