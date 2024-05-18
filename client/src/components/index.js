import FormBlock from "@/components/account/FormBlock.vue";

import ContentBlock from "@/components/main/ContentBlock.vue";
import ContentBlockTitle from "@/components/main/ContentBlockTitle.vue";
import ContentBlockText from "@/components/main/ContentBlockText.vue";

import ContentGrid from "@/components/main/page/ContentGrid.vue";
import PostGrid from "@/components/main/page/PostGrid.vue";
// Modals
import ModalTemplate from "@/components/main/modals/ModalTemplate.vue";
import ModalChangeBanner from "@/components/main/modals/ModalChangeBanner.vue";
import ModalChangeAvatar from "@/components/main/modals/ModalChangeAvatar.vue";
import ModalDeleteAccount from "@/components/main/modals/ModalDeleteAccount.vue";
import ModalChangePassword from "@/components/main/modals/ModalChangePassword.vue";
// Crop
import { CircleStencil } from "vue-advanced-cropper";

// Input
import FormInput from '@/components/account/FormInput.vue';
import FormTextArea from '@/components/account/FormTextArea.vue';
import AuthBlock from '@/components/account/AuthBlock.vue';
import PasswordInput from "./account/PasswordInput.vue";
import EmailInput from "./account/EmailInput.vue";
import NumberInput from "./account/NumberInput.vue";
import DateInput from "./account/DateInput.vue";
import SubmitButton from "./main/buttons/SubmitButton.vue";

import BarChart from "@/components/main/BarChart.vue";
import CalendarBlock from "@/components/main/CalendarBlock.vue";
import TokenCheck from "@/components/account/TokenCheck.vue";
// Buttons
import IconButton from "./main/buttons/IconButton.vue";
import SingleIcon from "./main/buttons/SingleIcon.vue";
import FilledIconButton from "./main/buttons/FilledIconButton.vue";
import ContentStatsButton from "./main/buttons/ContentStatsButton.vue";
import StrokeIconButton from "./main/buttons/StrokeIconButton.vue";
import ModalButton from "./main/buttons/ModalButton.vue";
import FileInputButton from "./main/buttons/FileInputButton.vue";
// Profile
import UserAvatar from "./profile/UserAvatar.vue";
import ProfileBanner from "@/components/profile/ProfileBanner.vue";
import PageHeader from "@/components/main/page/PageHeader.vue";
import UserInfoBanner from "@/components/profile/UserInfoBanner.vue";
import ProfileContent from "./profile/ProfileContent.vue";
// Feed
import FeedBlock from "./feed/FeedBlock.vue";
import FeedBlockTemplate from "./feed/FeedBlockTemplate.vue";
import FeedBlockPost from "./feed/FeedBlockPost.vue";
import FeedBlockMedia from "./feed/FeedBlockMedia.vue";
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
import MessageContextMenu from "@/components/main/static_panel/content/messenger/MessageContextMenu.vue";
// People
import AccountPreviewBlock from "@/components/main/static_panel/content/people/AccountPreviewBlock.vue";
// Content
import ContentPreviewBlock from "./main/static_panel/content/common/ContentPreviewBlock.vue";
// Music
import MusicPreviewBlock from "./main/static_panel/content/music/MusicPreviewBlock.vue";
// Notifications
import NotificationPreviewBlock from "./main/static_panel/content/notifications/NotificationPreviewBlock.vue";

export default [
    PageHeader, ContentGrid, PostGrid, 
    // Modals
    ModalTemplate, ModalDeleteAccount, ModalChangePassword, ModalChangeAvatar, ModalChangeBanner,
    // Crop 
    CircleStencil,
    FormBlock,
    ContentBlockTitle, ContentBlockText,
    BarChart, CalendarBlock,
    ContentBlock, FormInput, FormTextArea, AuthBlock, PasswordInput, EmailInput, NumberInput, DateInput, SubmitButton,
    TokenCheck,
    // Buttons
    SingleIcon, IconButton, FilledIconButton, StrokeIconButton, ContentStatsButton, ModalButton, FileInputButton,
    // Profile
    UserAvatar, ProfileBanner, UserInfoBanner, ProfileContent,
    // Feed
    FeedBlock, FeedBlockTemplate, FeedBlockPost, FeedBlockMedia, FeedBlockMusic,
    // News
    NewsBlock,
    // Static Panel
    StaticPanel, StaticPanelSearchBar, StaticPanelContent, StaticPanelHeader, StaticPanelHeaderMenu,
    // Chat
    ChatPreviewBlock,
    // Messenger
    MessengerActionIcon, Message, ChatTitle, MessageContextMenu,
    // People
    AccountPreviewBlock,
    // Music
    MusicPreviewBlock,
    // Content
    ContentPreviewBlock,
    // Notifications
    NotificationPreviewBlock,
]