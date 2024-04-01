import UsernameTextField from "@/components/account/UsernameTextField.vue";
import PasswordTextField from "@/components/account/PasswordTextField.vue";
import EmailTextField from "@/components/account/EmailTextField.vue";
import FormBlock from "@/components/account/FormBlock.vue";
import FullNameTextField from "@/components/account/FullNameTextField.vue";
import DateTextField from "@/components/account/DateTextField.vue";
import CountryTextField from "@/components/account/CountryTextField.vue";
import CityTextField from "@/components/account/CityTextField.vue";
import InputTextField from "@/components/account/InputTextField.vue";
import NumberTextField from "@/components/account/NumberTextField.vue";

import SideBar from "@/components/main/SideBar.vue";
import SideBarMenuItem from "@/components/main/SideBarMenuItem.vue";
import PageTitle from "@/components/main/PageTitle.vue";
import PageContent from "@/components/main/PageContent.vue";
import ContentBlockTitle from "@/components/main/ContentBlockTitle.vue";
import ContentBlockText from "@/components/main/ContentBlockText.vue";

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

export default [
    PageHeader,
    FormBlock, UsernameTextField, PasswordTextField, EmailTextField,
    FullNameTextField, DateTextField, CountryTextField, CityTextField, InputTextField, NumberTextField,
    SideBar, SideBarMenuItem, 
    PageTitle, PageContent, ContentBlockTitle, ContentBlockText,
    BarChart, CalendarBlock,
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
    StaticPanel, StaticPanelSearchBar, StaticPanelContent, StaticPanelHeader,
    // Chat
    ChatPreviewBlock,
    // Messenger
    MessengerActionIcon, Message, ChatTitle,
    // People
    AccountPreviewBlock,
]