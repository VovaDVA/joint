<template>
    <bar-chart :chartData="chartData" :chartOptions="chartOptions" />
    <bar-chart :chartData="chartData" :chartOptions="chartOptions" />
    <bar-chart :chartData="chartData" :chartOptions="chartOptions" />
</template>

<script>
import BarChart from "@/components/main/BarChart.vue";

export default {
    components: {
        BarChart,
    },
};
</script>
  
<style scoped></style>
  
  
  