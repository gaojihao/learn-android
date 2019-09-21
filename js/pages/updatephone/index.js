import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class UpdatePhoneScreen extends Component {
  static navigationOptions = {
    title: '修改手机号',
  };

  constructor(props) {
    super(props);
    this.state = {
    };
  }

  render() {
    return (
      <View>
        <Text> componentText </Text>
      </View>
    );
  }
}


