<template>
	<form-input type="password" v-model="value"><slot></slot></form-input>
</template>

<script>
export default {
	name: 'password-input',
};
</script>

<style scoped>
</style>
