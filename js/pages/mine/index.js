import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class MineScreen extends Component {

  static navigationOptions = {
    title: '我的',
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


