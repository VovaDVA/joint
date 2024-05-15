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
	position: fixed;
	top: 0;
	bottom: 0;
	right: 0;
	left: 0;
	margin: auto;
	z-index: 10000;

	padding: 20px;
	border: 1px #ffffff4f solid;
	border-radius: 20px;
	background: #151c25;
	background: #252d3a;
}

.auth-block.light-theme {
	border: 1px #0000007c solid;
	background: rgb(243, 243, 243);
	color: #000;
}

@media (max-width: 600px) {
	.auth-block,
	.auth-block.light-theme {
		border: none;
		margin: auto;
	}
}
</style>