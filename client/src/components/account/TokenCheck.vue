<template>
    <div></div>
</template>

<script>
// import { checkToken } from '../../modules/auth';

export default {
    name: 'token-check',
    mounted() {
        console.log('token check', this.$route.path);
        // if (!['/login', '/register'].includes(this.$route.path)) {
        //     checkToken();
        // }
    },
}
</script>