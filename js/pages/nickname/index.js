import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class NickNameScreen extends Component {
  static navigationOptions = {
    title: '修改昵称',
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


