<template>
	<div class='auth-block'>
        <slot></slot>
	</div>
</template>

<script>
export default {
    name: 'auth-block',
}
</script>

<style scoped>
.auth-block {
	max-width: 500px;
    height: fit-content;
	/* padding: 0 15px; */
	position: absolute;
	right: 0;
	left: 0;
	margin: auto;

	padding: 20px 30px;
    border: 1px #ffffff7c solid;
    border-radius: 20px;
    background: rgba(0, 0, 0, 0.5);
}
</style>