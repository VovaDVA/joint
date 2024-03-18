<template>
    <div class="calendar-title"><slot></slot></div>
    <Calendar class="calendar" v-model="date" showIcon showButtonBar />
</template>

<script>
import Calendar from 'primevue/calendar';

export default {
    name: 'calendar-block',
    components: {
        Calendar,
    },
    data() {
        return {
            date: null
        };
    }
};
</script>
  
<style scoped>

.calendar-title {
    margin: 20px 0 10px;
}
.calendar {
    width: 100%;
}
</style>
  
  
  