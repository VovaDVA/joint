<template>
    <token-check></token-check>
    <content-block>
        <content-block-title>Статистика профиля</content-block-title>
        <bar-chart></bar-chart>
        <LineChart></LineChart>
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

<style scoped></style>