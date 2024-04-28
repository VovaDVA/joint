import FormBlock from "@/components/account/FormBlock.vue";

import ContentBlock from "@/components/main/ContentBlock.vue";
import ContentBlockTitle from "@/components/main/ContentBlockTitle.vue";
import ContentBlockText from "@/components/main/ContentBlockText.vue";

// import ModalChangeBanner from "@/components/main/modals/ModalChangeBanner.vue";
// import ModalChangeAvatar from "@/components/main/modals/ModalChangeAvatar.vue";

// Input
import FormInput from '@/components/account/FormInput.vue';
import AuthBlock from '@/components/account/AuthBlock.vue';
import PasswordInput from "./account/PasswordInput.vue";
import EmailInput from "./account/EmailInput.vue";
import NumberInput from "./account/NumberInput.vue";
import DateInput from "./account/DateInput.vue";

import BarChart from "@/components/main/BarChart.vue";
import CalendarBlock from "@/components/main/CalendarBlock.vue";
import TokenCheck from "@/components/account/TokenCheck.vue";
// Buttons
import IconButton from "./main/buttons/IconButton.vue";
import SingleIcon from "./main/buttons/SingleIcon.vue";
import FilledIconButton from "./main/buttons/FilledIconButton.vue";
import ContentStatsButton from "./main/buttons/ContentStatsButton.vue";
import StrokeIconButton from "./main/buttons/StrokeIconButton.vue";
// Profile
import ProfileBanner from "@/components/profile/ProfileBanner.vue";
import PageHeader from "@/components/main/page/PageHeader.vue";
import UserAvatar from "@/components/profile/UserAvatar.vue";
import ProfileContent from "./profile/ProfileContent.vue";
// Feed
import FeedBlock from "./feed/FeedBlock.vue";
import FeedBlockMusic from "@/components/feed/FeedBlockMusic.vue";
// News
import NewsBlock from "@/components/news/NewsBlock.vue";
// Static Panel
import StaticPanel from "@/components/main/static_panel/StaticPanel.vue";
import StaticPanelHeader from "./main/static_panel/components/StaticPanelHeader.vue";
import StaticPanelHeaderMenu from "./main/static_panel/components/StaticPanelHeaderMenu.vue";
import StaticPanelSearchBar from "./main/static_panel/components/StaticPanelSearchBar.vue";
import StaticPanelContent from "./main/static_panel/components/StaticPanelContent.vue";
// Chat
import ChatPreviewBlock from "@/components/main/static_panel/content/chat/ChatPreviewBlock.vue";
// Messenger
import MessengerActionIcon from "@/components/main/static_panel/content/messenger/MessengerActionIcon.vue";
import Message from "@/components/main/static_panel/content/messenger/Message.vue";
import ChatTitle from "@/components/main/static_panel/content/messenger/ChatTitle.vue";
// People
import AccountPreviewBlock from "@/components/main/static_panel/content/people/AccountPreviewBlock.vue";
// Content
import ContentPreviewBlock from "./main/static_panel/content/common/ContentPreviewBlock.vue";
// Music
import MusicPreviewBlock from "./main/static_panel/content/music/MusicPreviewBlock.vue";
// Notifications
import NotificationPreviewBlock from "./main/static_panel/content/notifications/NotificationPreviewBlock.vue";

export default [
    PageHeader,//ModalChangeAvatar, ModalChangeBanner,
    FormBlock,
    ContentBlockTitle, ContentBlockText,
    BarChart, CalendarBlock,
    ContentBlock, FormInput, AuthBlock, PasswordInput, EmailInput, NumberInput, DateInput,
    TokenCheck,
    // Buttons
    SingleIcon, IconButton, FilledIconButton, StrokeIconButton, ContentStatsButton,
    // Profile
    ProfileBanner, UserAvatar, ProfileContent,
    // Feed
    FeedBlock, FeedBlockMusic,
    // News
    NewsBlock,
    // Static Panel
    StaticPanel, StaticPanelSearchBar, StaticPanelContent, StaticPanelHeader, StaticPanelHeaderMenu,
    // Chat
    ChatPreviewBlock,
    // Messenger
    MessengerActionIcon, Message, ChatTitle,
    // People
    AccountPreviewBlock,
    // Music
    MusicPreviewBlock,
    // Content
    ContentPreviewBlock,
    // Notifications
    NotificationPreviewBlock,
]