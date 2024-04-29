<template>
    <content-block>
        <content-block-title>Об авторе</content-block-title>
        <content-block-text>{{ getUserDescription }}
            <p>Меня зовут Владимир и я занимаюсь разработкой соцсети Joint.</p>
        </content-block-text>
    </content-block>
    <feed-block></feed-block>
    <feed-block></feed-block>
    <feed-block></feed-block>
    <feed-block></feed-block>
</template>

<script>
import { getUserDescription } from '@/modules/auth';
export default {
    methods: {
        getDescription() {
            return getUserDescription();
        }
    }
}
</script>