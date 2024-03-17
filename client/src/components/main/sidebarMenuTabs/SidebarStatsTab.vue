<template>
    <page-content class="stats-content">
        <template v-slot:title>Подробная статистика</template>
        <div class="menu_inner">
            <router-link to="/table-view"><div class="menu_item">Таблица</div></router-link>
            <router-link to="/graphics-view"><div class="menu_item">График</div></router-link>
        </div>
        <router-view></router-view>
    </page-content>
</template>

<script></script>
  
<style scoped>
.menu_inner {
    height: 40px;
    display: flex;
    justify-content: center;
    font-size: 13px;
}

.menu_item {
    padding: 10px 30px;

    color: #fff;
    text-transform: uppercase;
    text-decoration: none;

    transition: color .3s linear;

    border-bottom: 1px #ffffff solid;
}

.menu_item:hover {
    border-bottom: 1px #30ff58 solid;
    color: #30ff58;
    transition: color .3s linear;
}
</style>
  
  
  