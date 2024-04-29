<template>
    <div class="notification-preview-block" :class="$store.state.theme">
        <div class="avatar"></div>
        <div class="notification-info">
            <div class="notification-description">
                <div class="notification-text">Сообщество <div class="sourse">ТОПы ПИ</div> опубликовало новую запись
                    <div class="sourse">{{ name }}</div>
                </div>
            </div>
            <div class="notification-params">
                <div class="date">31 марта в 5:00</div>
            </div>
        </div>
        <!-- <div class="content"></div> -->
    </div>
</template>

<script>
export default {
    name: 'notification-preview-block',
    props: ['name'],
}
</script>

<style scoped>
.notification-preview-block {
    display: flex; 
    flex: 0 0 100px;
    width: auto;
    margin: 10px;
    padding: 10px;
    border-radius: 15px;
    transition: background .2s linear;
}

.notification-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.notification-preview-block.light-theme {
    color: #000;
}

.notification-preview-block:hover {
    background: rgba(255, 255, 255, 0.1);
    transition: background .2s linear;
}

.notification-preview-block.light-theme:hover {
    background: rgba(0, 0, 0, 0.1);
}

.avatar {
    width: 60px;
    flex: 0 0 60px;
    height: 60px;
    margin-right: 15px;
    border: 1px #ffffff2f solid;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
}

.notification-preview-block.light-theme .avatar {
    border: 1px #0000002f solid;
    background: rgba(0, 0, 0, 0.2);
}

.notification-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.notification-description {
    margin-top: 5px;
}

.notification-text {
    font-size: 15px;
}

.sourse {
    color: #ffbf6c;
    display: inline;
}

.notification-preview-block.light-theme .sourse {
    color: #965000;
}

.sourse:hover {
    text-decoration: underline;
    transition: text-decoration;
}

.notification-params {
    display: flex;
    justify-content: space-between;
    margin-bottom: 5px;
}

.content {
    width: 60px;
    flex: 0 0 60px;
    height: 60px;
    margin-right: 15px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 5px;
}

.date {
    display: flex;
    font-size: 13px;
    color: #969696;
}
</style>