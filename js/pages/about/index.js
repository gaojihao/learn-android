import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class AboutScreen extends Component {
  static navigationOptions = {
    title: '关于我们',
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


