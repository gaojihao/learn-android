import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class SelectScreen extends Component {

  static navigationOptions = {
    title: '请选择',
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


