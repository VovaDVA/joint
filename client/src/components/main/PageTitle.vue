<script>
export default {
	name: 'page-title'
}
</script>

<template>
	<div class="page-title">
		<slot></slot>
	</div>
</template>

<style scoped>
.page-title {
	margin: 0 auto 15px;
	padding: 10px;
	max-width: 300px;

	font-size: 20px;
	text-align: center;

	border-bottom: 1px solid transparent;
	border-image: radial-gradient(#ffffff 50%, transparent);
	border-image-slice: 1;
}
</style>