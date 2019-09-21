import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class YuYueScreen extends Component {

  static navigationOptions = {
    title: '预约',
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


