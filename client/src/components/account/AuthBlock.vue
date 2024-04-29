<template>
	<div class='auth-block' :class="$store.state.theme">
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
	margin: 50px auto;

	padding: 20px;
	border: 1px #ffffff7c solid;
	border-radius: 20px;
	background: rgba(0, 0, 0, 0.5);
}

.auth-block.light-theme {
	border: 1px #0000007c solid;
	background: #ebebeb;
	color: #000;
}

@media (max-width: 600px) {
	.auth-block, .auth-block.light-theme {
		border: none;
		background: none;
		margin: auto;
		margin-top: -50px;
	}
}
</style>