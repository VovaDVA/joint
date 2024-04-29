<template>
    <Bar class="chart" id="my-chart-id" :options="chartOptions" :data="chartData" />
</template>
  
<script>
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

export default {
    name: 'bar-chart',
    components: { Bar },
    data() {
        return {
            chartData: {
                labels: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
                datasets: [{
                    label: 'Данные за год',
                    data: [40, 20, 12, 34, 5, 73, 45, 2, 63, 78, 9, 2],
                    backgroundColor: ['rgba(98, 216, 100, 1)'],
                    // borderColor: ['rgba(178, 222, 179, 1)'],
                    // textColor: '#CACACA',
                }]
            },
            chartOptions: {
                responsive: true,
                plugins: {
                    title: {
                        display: false,
                        // text: 'Bar Chart',
                        // color: 'red', 
                    },
                },
                scales: {
                    x: {
                        type: 'category',
                        ticks: {
                            color: '#8A8A8A', 
                        },
                    },
                    y: {
                        ticks: {
                            color: '#8A8A8A', 
                        },
                    },
                },
            }
        }
    },
};
</script>

<style scoped>
.chart {
    margin-top: 30px;
}
</style>
  