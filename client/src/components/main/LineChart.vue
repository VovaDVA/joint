<template>
    <Line :data="chartData" :options="chartOptions" />
</template>

<script>
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js'
import { Line } from 'vue-chartjs'
// import chartConfig from './chartConfig'

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
)

export default {
    components: {
        Line
    },
    data() {
        return {
            chartData: {
                labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                datasets: [
                    {
                        label: 'Data One',
                        backgroundColor: '#f87979',
                        data: [40, 39, 10, 40, 39, 80, 40]
                    }
                ]
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
                            color: '#ffffff', 
                        },
                    },
                    y: {
                        ticks: {
                            color: '#ffffff', 
                        },
                    },
                },
            }
        }
    }
}
</script>