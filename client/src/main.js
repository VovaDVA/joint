import { createApp } from 'vue';
import mitt from 'mitt';
import { createStore } from 'vuex';
import PrimeVue from 'primevue/config';
import 'primevue/resources/themes/vela-green/theme.css'
import App from './App.vue';
import router from './router.js';
import pages from "@/pages";
import components from "@/components";
import './assets/index.css';

const emitter = mitt();
const app = createApp(App);

import { formatDate } from '@/modules/utils';
app.config.globalProperties.$formatDate = formatDate;

const store = createStore({
    state() {
        return {
            staticPanelVisible: false,
            theme: localStorage.getItem('ui_theme') ?? 'light-theme',
        }
    },
    mutations: {
        showStaticPanel(state) {
            state.staticPanelVisible = true;
        },
        hideStaticPanel(state) {
            state.staticPanelVisible = false;
        },
        changeTheme(state) {
            state.theme = (state.theme == 'dark-theme') ? 'light-theme' : 'dark-theme';
            localStorage.setItem('ui_theme', state.theme);
        }
    }
})

pages.forEach(page => app.component(page.name, page))
components.forEach(component => app.component(component.name, component))

app.use(PrimeVue);
app.use(router);
app.use(store);

app.config.globalProperties.emitter = emitter;
app.mount('#app');