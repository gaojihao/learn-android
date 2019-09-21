import React, { Component } from 'react';
import {StyleSheet, View, Text } from 'react-native';
import AsyncStorage from '@react-native-community/async-storage';

export default class UserInfoScreen extends Component {
  static navigationOptions = {
    title: '个人信息',
  };

  constructor(props) {
    super(props);
    this.state = {
    };
  }


  _signOutAsync = async () => {
    await AsyncStorage.clear();
    this.props.navigation.navigate('Auth');
  };

  render() {
    return (
      <View>
        <Text> componentText </Text>
      </View>
    );
  }
}


