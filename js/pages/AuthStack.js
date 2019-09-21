import React from 'react';
import { createStackNavigator } from 'react-navigation';
import SignInScreen from './signIn';
import RegisterScreen from './register';
import ForgetPasswordScreen from './forget';
import {View} from 'react-native';

const AuthStack = createStackNavigator({ 
    SignIn: SignInScreen,
    Register: RegisterScreen,
    FindPWD: ForgetPasswordScreen,
 },{
    defaultNavigationOptions: {
        headerRight:(<View/>),
        headerTitleStyle:{
          textAlign: 'center',
          flex:1,
        },
        headerStyle:{elevation: 0},
    },
  });

export default AuthStack;