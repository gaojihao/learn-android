import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class MyOrderScreen extends Component {
  static navigationOptions = {
    title: '我的预约',
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


