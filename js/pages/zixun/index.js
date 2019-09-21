import React, { Component } from 'react';
import {  View, Text } from 'react-native';

export default class ZiXunScreen extends Component {

  static navigationOptions = {
    title: '咨询',
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


