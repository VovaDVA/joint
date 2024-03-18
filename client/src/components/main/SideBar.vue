<script>
export default {
	name: 'side-bar'
}
</script>

<template>
	<div class="sidebar">
		<content-block-title>Меню</content-block-title>
		<div class="user_about_content">
			<content-block-text>
				<router-link to="/">
					<sidebar-menu-item>Подробная статистика</sidebar-menu-item>
				</router-link>
				<router-link to="/calendar">
					<sidebar-menu-item>Календарь</sidebar-menu-item>
				</router-link>
				<router-link to="/calendar">
					<sidebar-menu-item>Доходы</sidebar-menu-item>
				</router-link>
				<router-link to="/calendar">
					<sidebar-menu-item>Расходы</sidebar-menu-item>
				</router-link>
				<router-link to="/calendar">
					<sidebar-menu-item>Добавить статью дохода</sidebar-menu-item>
				</router-link>
				<router-link to="/add-expense">
					<sidebar-menu-item>Добавить статью расходов</sidebar-menu-item>
				</router-link>
				<router-link to="/calendar">
					<sidebar-menu-item>Инвестиции</sidebar-menu-item>
				</router-link>
				<router-link to="/profile-settings">
					<sidebar-menu-item>Настройки профиля</sidebar-menu-item>
				</router-link>
			</content-block-text>
		</div>
	</div>
</template>

<style scoped>
.sidebar {
	font-family: 'Montserrat', sans-serif;
	position: fixed;
	top: 200px;
	width: 300px;
	height: fit-content;
	min-height: 500px;
	padding: 10px 20px 50px;

	text-align: center;
	border-radius: 30px;
	color: #FFFFFF;
	background: rgba(0, 0, 0, 0.5);

	/* position: relative; */
}
</style>