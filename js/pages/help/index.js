import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class HelpScreen extends Component {
  static navigationOptions = {
    title: '帮助',
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


