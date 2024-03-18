import RegistrationPage from './pages/RegistrationPage.vue';
import LoginPage from './pages/LoginPage.vue';
// import HomePage from './pages/HomePage.vue';
import ProfileSettingsPage from './pages/ProfileSettingsPage.vue';
import AboutPage from './pages/AboutPage.vue';
import GuidePage from './pages/GuidePage.vue';
import NewsPage from './pages/NewsPage.vue';
import ProfilePage from './pages/ProfilePage.vue';

// import SidebarStatsTab from './components/main/sidebarMenuTabs/SidebarStatsTab.vue';
// import StatsTabTable from './components/main/sidebarMenuTabs/StatsTabTable.vue';
// import StatsTabGraphics from './components/main/sidebarMenuTabs/StatsTabGraphics.vue';

import SidebarCalendarTab from './components/main/sidebarMenuTabs/SidebarCalendarTab.vue';
import SidebarAddExpenseTab from './components/main/sidebarMenuTabs/SidebarAddExpenseTab.vue';

import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  { path: '/register', component: RegistrationPage },
  { path: '/login', component: LoginPage },
  { path: '/profile', component: ProfilePage },
  { path: '/profile-settings', component: ProfileSettingsPage },
  { path: '/about', component: AboutPage },
  { path: '/guide', component: GuidePage },
  { path: '/news', component: NewsPage },
  {
    path: '/', component: ProfilePage, children: [
      // {
      //   path: '/', component: SidebarStatsTab, redirect: '/table-view', children: [
      //     { path: '/table-view', component: StatsTabTable },
      //     { path: '/graphics-view', component: StatsTabGraphics }
      //   ]
      // },
      { path: '/calendar', component: SidebarCalendarTab },
      { path: '/add-expense', component: SidebarAddExpenseTab }
    ]
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;