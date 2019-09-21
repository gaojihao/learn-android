import React, { Component } from 'react';
import { WebView } from 'react-native-webview';
import PropTypes from 'prop-types';

export default class WebViewScreen extends Component {
  static navigationOptions = {
    title: '修改手机号',
  };

  constructor(props) {
    super(props);
    this.state = {
        path:''
    };
  }

  componentDidMount(){
      
  }

  render() {
      let {path} = this.state
    return (
        <WebView
        source={{ uri: path }}
        style={{ marginTop: 20 }}
      />
    );
  }
}

WebViewScreen.propTypes = {
    url:PropTypes.string,
    path:PropTypes.string,
};


