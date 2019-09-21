import { createStackNavigator } from 'react-navigation';
import HomeScreen from './home';
import AboutScreen from './about';
import EducationScreen from './education';
import FollowScreen from './followup';
import ForgetPasswordScreen from './forget';
import HelpScreen from './help';
import MineScreen from './mine';
import MyOrderScreen from './myorder';
import NickNameScreen from './nickname';
import OrderScreen from './order';
import QuestionScreen from './questionnaire';
import ReportScreen from './report';
import RecoveryScreen from './revovery';
import ScaleScreen from './scale';
import SelectScreen from './select';
import TimeLineScreen from './timeline';
import UpdatePhoneScreen from './updatephone';
import UpdatePWDScreen from './updatepwd';
import UserInfoScreen from './userinfo';
import WenZhenScreen from './wenzhen';
import YuYueScreen from './yuyue';
import ZiXunScreen from './zixun';
import WebViewScreen from './web';

import React from 'react';
import {View} from 'react-native';

const AppStack = createStackNavigator(
    { 
        home: HomeScreen,
        about: AboutScreen,
        education: EducationScreen,
        follow: FollowScreen,
        findpwd: ForgetPasswordScreen,
        help: HelpScreen,
        mine: MineScreen,
        myOrder: MyOrderScreen,
        nickName: NickNameScreen,
        order: OrderScreen,
        question: QuestionScreen,
        report: ReportScreen,
        recovery: RecoveryScreen,
        scale: ScaleScreen,
        select: SelectScreen,
        timeLine: TimeLineScreen,
        resetMobile: UpdatePhoneScreen,
        resetPwd: UpdatePWDScreen,
        userInfo: UserInfoScreen,
        wenzhen: WenZhenScreen,
        yuyue: YuYueScreen,
        zixun: ZiXunScreen,
        web:WebViewScreen,
    },{
    defaultNavigationOptions: {
        headerRight:(<View/>),
        headerTitleStyle:{
          textAlign: 'center',
          flex:1,
        },
        headerStyle:{elevation: 0,backgroundColor:'white'},
    },
  });

export default AppStack;