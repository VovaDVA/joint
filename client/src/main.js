import { createApp } from 'vue';
import { createStore } from 'vuex';
import PrimeVue from 'primevue/config';
import 'primevue/resources/themes/vela-green/theme.css'
import App from './App.vue';
import router from './router.js';
import pages from "@/pages";
import components from "@/components";
import './assets/index.css';

const app = createApp(App);

const store = createStore({
    state() {
        return {
            staticPanelVisible: false,
            theme: 'light-theme',
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
        }
    }
})

pages.forEach(page => app.component(page.name, page))
components.forEach(component => app.component(component.name, component))

app.use(PrimeVue);
app.use(router);
app.use(store);

app.mount('#app');