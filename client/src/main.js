import { createApp } from 'vue';
import PrimeVue from 'primevue/config';
import 'primevue/resources/themes/vela-green/theme.css'
import App from './App.vue';
import router from './router.js';
import pages from "@/pages";
import components from "@/components";
import './assets/index.css'

const app = createApp(App);

pages.forEach(page => app.component(page.name, page))
components.forEach(component => app.component(component.name, component))

app.use(PrimeVue);
app.use(router);

app.mount('#app');