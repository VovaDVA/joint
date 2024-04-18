<template>
	<form-input type="date" v-model="value"><slot></slot></form-input>
</template>

<script>
export default {
	name: 'date-input',
};
</script>

<style scoped>
</style>
