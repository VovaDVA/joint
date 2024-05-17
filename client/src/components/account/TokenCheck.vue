<template>
    <div></div>
</template>

<script>
import { checkToken } from '../../modules/auth';

export default {
    name: 'token-check',
    async mounted() {
        if (!['/login', '/register'].includes(this.$route.path)) {
            await checkToken();
        }
    },
}
</script>