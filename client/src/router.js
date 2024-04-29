import RegistrationPage from './pages/RegistrationPage.vue';
import LoginPage from './pages/LoginPage.vue';
import ProfileSettingsPage from './pages/ProfileSettingsPage.vue';
import AboutPage from './pages/AboutPage.vue';
import GuidePage from './pages/GuidePage.vue';
import NewsPage from './pages/NewsPage.vue';
import ProfilePage from './pages/ProfilePage.vue';
import FeedPage from './pages/FeedPage.vue';
import CalendarPage from './pages/CalendarPage.vue';
import StatsPage from './pages/StatsPage.vue';

import ProfileContentAbout from './components/profile/content/ProfileContentAbout.vue';
import ProfileContentImages from './components/profile/content/ProfileContentImages.vue';

import { createRouter, createWebHistory } from 'vue-router';
import ProfileContentMusic from './components/profile/content/ProfileContentMusic.vue';

const routes = [
  { path: '/register', component: RegistrationPage },
  { path: '/login', component: LoginPage },

  { path: '/profile-settings', component: ProfileSettingsPage },
  { path: '/about', component: AboutPage },
  { path: '/guide', component: GuidePage },
  { path: '/news', component: NewsPage },
  { path: '/feed', component: FeedPage },
  { path: '/calendar', component: CalendarPage },
  { path: '/stats', component: StatsPage },
  {
    path: '/', component: ProfilePage, children: [
      { path: '/', component: ProfileContentAbout },
      { path: '/images', component: ProfileContentImages },
      { path: '/videos', component: ProfileContentImages },
      { path: '/music', component: ProfileContentMusic },
      { path: '/books', component: ProfileContentImages },
    ]
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;