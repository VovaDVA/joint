<template>
	<form-input type="number" v-model="value"><slot></slot></form-input>
</template>

<script>
export default {
	name: 'number-input',
};
</script>

<style scoped>
</style>
