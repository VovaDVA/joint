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

// Profile
import ProfileBanner from "@/components/profile/ProfileBanner.vue";
import PageHeader from "@/components/main/page/PageHeader.vue";
import UserAvatar from "@/components/profile/UserAvatar.vue";
import Chat from "@/components/main/Chat.vue";
import ProfileContent from "./profile/ProfileContent.vue";

export default [
    PageHeader,
    FormBlock, UsernameTextField, PasswordTextField, EmailTextField,
    FullNameTextField, DateTextField, CountryTextField, CityTextField, InputTextField, NumberTextField,
    SideBar, SideBarMenuItem, 
    PageTitle, PageContent, ContentBlockTitle, ContentBlockText,
    BarChart, CalendarBlock,
    TokenCheck,
    ProfileBanner, UserAvatar, ProfileContent,
    Chat,
]